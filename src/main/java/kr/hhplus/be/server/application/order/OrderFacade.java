package kr.hhplus.be.server.application.order;

import jakarta.transaction.Transactional;
import java.util.List;
import kr.hhplus.be.server.application.coupon.CouponCommand;
import kr.hhplus.be.server.application.coupon.CouponService;
import kr.hhplus.be.server.application.coupon.UseCouponInfo;
import kr.hhplus.be.server.application.order.service.OrderService;
import kr.hhplus.be.server.application.payment.PaymentCommand;
import kr.hhplus.be.server.application.payment.PaymentService;
import kr.hhplus.be.server.application.point.PointCommand;
import kr.hhplus.be.server.application.point.PointService;
import kr.hhplus.be.server.application.product.ProductService;
import kr.hhplus.be.server.application.user.UserService;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderItem;
import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.infra.lock.RedisLock;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderFacade {

	private final Logger logger = LoggerFactory.getLogger(OrderFacade.class);
	private final OrderService orderService;
	private final PaymentService paymentService;
	private final ProductService productService;
	private final PointService pointService;
	private final CouponService couponService;
	private final UserService userService;

	public OrderResult.InfoByUser getOrders(Long userId) {
		logger.info("### getOrders parameter : {}", userId);
		List<OrderResult.DetailByOrder> orderDetails = orderService.findOrderByUserId(userId).stream()
			.map(OrderResult.DetailByOrder::from).toList();
		return OrderResult.InfoByUser.from(userId, orderDetails);
	}

	@Retryable(
		value = {ObjectOptimisticLockingFailureException.class},
		maxAttempts = 3,
		backoff = @Backoff(delay = 100)
	)
	@Transactional
	@RedisLock(lockKey = "order:{#criteria.orderId()}", params = "#criteria.orderId()")
	public OrderResult.Cancel cancel(OrderCriteria.Cancel criteria) {
		logger.info("### cancel parameter : {}", criteria.toString());
		Order order = orderService.findByIdAndUserId(criteria.orderId(), criteria.userId());

		User user = userService.get(criteria.userId());
		// 결제 취소
		Payment payment = paymentService.cancel(order);
		// 포인트 환불
		pointService.refund(PointCommand.Refund.of(user.getId(), order.getTotalPrice()));
		// 쿠폰 상태 복원 (쿠폰 사용했으면)
		couponService.restore(CouponCommand.Restore.of(user.getId(), order.getCoupons()));
		// 재고 복원
		productService.increaseStock(order.getOrderItems());

		// 주문상태 변경 및 저장 - 취소
		orderService.cancel(order);
		return OrderResult.Cancel.of(
			order,
			payment
		);
	}

	@Retryable(
		value = {ObjectOptimisticLockingFailureException.class},
		maxAttempts = 3,
		backoff = @Backoff(delay = 100)
	)
	@Transactional
	public OrderResult.Place placeOrder(OrderCriteria.Request criteria) {
		logger.info("### placeOrder parameter : {}", criteria.toString());

		User user = userService.get(criteria.userId());
		// 쿠폰 사용 - 상태 변환
		// 쿠폰 유효성 확인
		// - 만료 되진 않았는지, 쿠폰이 유저 소유가 맞는지
		UseCouponInfo couponInfo = couponService.use(CouponCommand.Use.of(
			user.getId(), criteria.couponIds()
		));
		// 상품 조회
		List<OrderItem> orderItems = createOrderItems(criteria.orderItems());

		Order order = orderService.create(OrderCommand.Create.of(orderItems, couponInfo));
		// 유저 포인트 사용
		pointService.use(PointCommand.Use.of(user.getId(), order.getTotalPrice()));

		// 주문 저장
		Order save = orderService.save(order);
		logger.info("### save order : {}", save.getId());
		// 결제 시도
		Payment payment = paymentService.pay(
			PaymentCommand.Request.of(save, criteria.paymentMethod())
		);
		return OrderResult.Place.of(
			user.getId(),
			orderItems.stream().map(OrderItem::getProduct).toList(),
			payment,
			order
		);
	}

	private List<OrderItem> createOrderItems(List<OrderCriteria.Request.Item> orderItems) {
		logger.info("### createOrderItems parameter : {}", orderItems.toString());
		return orderItems.stream()
			.map(itemCommand -> {
				Product product = productService.decreaseStock(itemCommand.productId(),
					itemCommand.quantity());
				return OrderItem.create(product, itemCommand.quantity());
			})
			.toList();
	}
}
