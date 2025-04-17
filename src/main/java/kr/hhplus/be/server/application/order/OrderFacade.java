package kr.hhplus.be.server.application.order;

import static kr.hhplus.be.server.support.config.swagger.ErrorCode.INVALID_QUANTITY;

import java.util.List;
import kr.hhplus.be.server.application.coupon.CouponService;
import kr.hhplus.be.server.application.coupon.UseCouponCommand;
import kr.hhplus.be.server.application.coupon.UseCouponInfo;
import kr.hhplus.be.server.application.order.service.OrderItemService;
import kr.hhplus.be.server.application.order.service.OrderService;
import kr.hhplus.be.server.application.payment.PaymentService;
import kr.hhplus.be.server.application.payment.RequestPaymentCommand;
import kr.hhplus.be.server.application.point.PointService;
import kr.hhplus.be.server.application.point.UpdatePointCommand;
import kr.hhplus.be.server.application.product.ProductService;
import kr.hhplus.be.server.application.user.UserService;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderItem;
import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.support.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class OrderFacade {

	private final Logger logger = LoggerFactory.getLogger(OrderFacade.class);
	private final OrderService orderService;
	private final PaymentService paymentService;
	private final ProductService productService;
	private final PointService pointService;
	private final CouponService couponService;
	private final OrderItemService orderItemService;
	private final UserService userService;

	public OrderInfoResult getOrders(Long userId) {
		logger.info("### getOrders parameter : {}", userId);
		List<OrderDetailResult> orderDetails = orderService.findHistoryByUserId(userId)
			.stream().map(oh -> {
				Long orderId = oh.getOrder().getId();
				List<Product> products = productService.orderItemToProduct(
					oh.getOrder().getOrderItems());
				List<Coupon> coupons = oh.getOrder().getCouponIds().stream()
					.map(couponService::findCouponById)
					.toList();
				return new OrderDetailResult(orderId, products, coupons);
			}).toList();
		return OrderInfoResult.from(userId, orderDetails);
	}

	public CancelOrderResult cancel(CancelOrderCriteria criteria) {
		logger.info("### cancel parameter : {}", criteria.toString());
		Order order = orderService.findByIdAndUserId(criteria.orderId(), criteria.userId());
		User user = userService.get(criteria.userId());
		// 결제 취소
		Payment payment = paymentService.cancel(order);
		// 포인트 환불
		pointService.refund(UpdatePointCommand.Refund.of(user, order.getTotalPrice()));
		// 쿠폰 상태 복원 (쿠폰 사용했으면)
		if (!order.getCouponIds().isEmpty()) {
			couponService.restore(UseCouponCommand.of(user.getId(), order.getCouponIds()));
		}
		// 재고 복원
		productService.increaseStock(order.getOrderItems());

		// 주문상태 변경 및 저장 - 취소
		orderService.cancel(order);
		return CancelOrderResult.from(
			order,
			payment
		);
	}

	@Transactional
	public PlaceOrderResult placeOrder(RequestOrderCriteria criteria) {
		logger.info("### placeOrder parameter : {}", criteria.toString());

		User user = userService.get(criteria.userId());
		// 상품 조회
		List<OrderItem> orderItems = createOrderItems(criteria);

		// 쿠폰 사용 - 상태 변환
		// 쿠폰 유효성 확인
		// - 만료 되진 않았는지, 쿠폰이 유저 소유가 맞는지
		UseCouponInfo couponInfo = couponService.use(UseCouponCommand.of(
			user.getId(), criteria.couponIds()
		));

		Order order = orderService.create(CreateOrderCommand.of(orderItems, couponInfo));
		// 유저 포인트 사용
		pointService.use(UpdatePointCommand.Use.of(user, order.getTotalPrice()));
		// 결제 시도
		Payment payment = paymentService.pay(
			RequestPaymentCommand.of(order, criteria.paymentMethod())
		);
		// 재고 차감
		List<Product> products = productService.decreaseStock(orderItems);
		// 주문 저장
		orderService.save(order);
		return PlaceOrderResult.of(
			user.getId(),
			products,
			payment,
			order
		);
	}

	private List<OrderItem> createOrderItems(RequestOrderCriteria command) {
		logger.info("### createOrderItems parameter : {}", command.toString());
		List<OrderItem> orderItems = command.orderItems().stream()
			.map(itemCommand -> {
				if (itemCommand.quantity() <= 0) {
					throw new CustomException(INVALID_QUANTITY);
				}
				Product product = productService.findById(itemCommand.productId());
				product.validateOrderable();
				return OrderItem.create(product, itemCommand.quantity());
			})
			.toList();
		return orderItemService.saveAll(orderItems);
	}
}
