package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.application.coupon.CouponCommand;
import kr.hhplus.be.server.application.coupon.CouponService;
import kr.hhplus.be.server.application.coupon.UseCouponInfo;
import kr.hhplus.be.server.application.payment.PaymentService;
import kr.hhplus.be.server.application.point.PointCommand;
import kr.hhplus.be.server.application.point.PointService;
import kr.hhplus.be.server.application.product.ProductCommand;
import kr.hhplus.be.server.application.product.ProductService;
import kr.hhplus.be.server.application.user.UserService;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderItem;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.support.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

import static kr.hhplus.be.server.support.config.swagger.ErrorCode.FAIL_ORDER_CANCEL;
import static kr.hhplus.be.server.support.config.swagger.ErrorCode.FAIL_PLACE_ORDER;

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

	private final ApplicationEventPublisher applicationEventPublisher;

	public OrderResult.InfoByUser getOrders(Long userId) {
		logger.info("### getOrders parameter : {}", userId);
		List<OrderResult.DetailByOrder> orderDetails = orderService.findOrderByUserId(userId)
			.stream()
			.map(OrderResult.DetailByOrder::from)
			.toList();
		return OrderResult.InfoByUser.from(userId, orderDetails);
	}

	@Retryable(
		value = {ObjectOptimisticLockingFailureException.class},
		backoff = @Backoff(delay = 100)
	)

	public OrderResult.Cancel cancel(OrderCriteria.Cancel criteria) {
		logger.info("### cancel parameter : {}", criteria.toString());
		Order order = orderService.findByIdAndUserId(criteria.orderId(), criteria.userId());

		User user = userService.get(criteria.userId());
		try {
			// 주문상태 변경 및 저장 - 취소
			orderService.cancel(order);
			applicationEventPublisher.publishEvent(CancelOrderEvent.of(user.getId(),order));
		} catch (Exception e) {
			throw new CustomException(FAIL_ORDER_CANCEL);
		}
		return OrderResult.Cancel.of(
			order
		);
	}

	@Retryable(
		value = {ObjectOptimisticLockingFailureException.class},
		backoff = @Backoff(delay = 100)
	)

	public OrderResult.Place placeOrder(OrderCriteria.Request criteria) {
		logger.info("### placeOrder parameter : {}", criteria.toString());
		User user = userService.get(criteria.userId());
		UseCouponInfo couponInfo = null;
		List<OrderItem> orderItems = null;
		Order order = null;

		try {
			couponInfo = couponService.use(CouponCommand.Use.of(
				user.getId(), criteria.couponIds()
			));
			orderItems = createOrderItems(criteria.orderItems());
			order = orderService.create(OrderCommand.Create.of(orderItems, couponInfo));
			applicationEventPublisher.publishEvent(OrderPlacedEvent.of(user.getId(), order, criteria.paymentMethod()));
		} catch (Exception e) {
			if (couponInfo != null)
				couponService.restore(CouponCommand.Restore.of(user.getId(), couponInfo.couponIds())); // 쿠폰 원복
			if (orderItems != null) productService.increaseStock(ProductCommand.Refund.of(orderItems)); // 상품 재고 원복
			pointService.refund(PointCommand.Refund.of(user.getId(), order != null ? order.getTotalPrice() : BigDecimal.ZERO)); // 포인트 원복

			if (order != null) paymentService.cancel(order.getId(), order.getTotalPrice()); // 결제 취소
			throw new CustomException(FAIL_PLACE_ORDER);
		}

		return OrderResult.Place.of(
			user.getId(),
			orderItems.stream().map(OrderItem::getProductId).toList(),
			order
		);
	}

	private List<OrderItem> createOrderItems(List<OrderCriteria.Request.Item> orderItems) {
		logger.info("### createOrderItems parameter : {}", orderItems.toString());
		return orderItems.stream()
			.map(itemCommand -> {
				Product product = productService.decreaseStock(itemCommand.productId(),
					itemCommand.quantity());
				return new OrderItem(product, itemCommand.quantity());
			})
			.toList();
	}
}
