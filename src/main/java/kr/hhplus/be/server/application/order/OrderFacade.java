package kr.hhplus.be.server.application.order;

import static kr.hhplus.be.server.support.config.swagger.ErrorCode.INVALID_COUPON;
import static kr.hhplus.be.server.support.config.swagger.ErrorCode.INVALID_QUANTITY;
import static kr.hhplus.be.server.support.config.swagger.ErrorCode.OUT_OF_STOCK;
import static kr.hhplus.be.server.support.config.swagger.ErrorCode.PAYMENT_FAIL;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import kr.hhplus.be.server.application.coupon.CouponService;
import kr.hhplus.be.server.application.coupon.UseCouponCommand;
import kr.hhplus.be.server.application.order.service.OrderHistoryService;
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
import kr.hhplus.be.server.domain.order.OrderHistory;
import kr.hhplus.be.server.domain.order.OrderItem;
import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.payment.PaymentStatus;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductStatus;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import kr.hhplus.be.server.support.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderFacade {

	private final OrderService orderService;
	private final PaymentService paymentService;
	private final ProductService productService;
	private final PointService pointService;
	private final CouponService couponService;
	private final OrderItemService orderItemService;
	private final UserService userService;
	private final OrderHistoryService orderHistoryService;

	public OrderInfoResult getOrders(Long userId) {
		List<OrderHistory> histories = orderHistoryService.findByUserId(userId);
		List<OrderItem> orderItems = histories.stream()
			.map(OrderHistory::getOrderId)
			.map(orderItemService::findByOrderId)
			.flatMap(List::stream)
			.toList();

		return OrderInfoResult.from(userId, orderItems, histories);
	}

	public PlaceOrderResult order(RequestOrderCriteria criteria) {
		User user = userService.get(criteria.userId());
		// 상품 조회
		List<OrderItem> orderItems = createOrderItems(criteria);
		// 쿠폰 유효성 확인
		// - 만료 되진 않았는지, 쿠폰이 유저 소유가 맞는지
		List<Coupon> coupons = validateCoupons(user.getId(), criteria.couponIds());
		// 주문 생성 + price 쿠폰 적용
		Order order = orderService.create(
			CreateOrderCommand.of(criteria.userId(), orderItems, coupons));
		// 쿠폰 사용 상태 변환
		if (!coupons.isEmpty() && coupons != null) {
			couponService.use(UseCouponCommand.of(user, coupons));
		}
		// 유저 포인트 사용
		pointService.use(UpdatePointCommand.Use.of(user, order.getTotalPrice()));
		// 결제 시도
		Payment payment = paymentService.pay(
			RequestPaymentCommand.of(order.getTotalPrice(), order, criteria.paymentMethod()));
		if (payment.getStatus() == PaymentStatus.FAILED) {
			throw new CustomException(PAYMENT_FAIL);
		}
		// 재고 차감
		productService.decreaseStock(orderItems);
		// 주문 저장
		orderService.save(order);
		return PlaceOrderResult.of(
			user.getId(),
			orderItems,
			payment,
			order,
			coupons
		);
	}

	private List<OrderItem> createOrderItems(RequestOrderCriteria command) {

		List<OrderItem> orderItems = command.orderItems().stream()
			.map(itemCommand -> {
				if (itemCommand.quantity() <= 0) {
					throw new CustomException(INVALID_QUANTITY);
				}
				Product product = productService.findById(itemCommand.productId());
				if (product.getStatus() == ProductStatus.OUT_OF_STOCK || product.getStock() <= 0) {
					throw new CustomException(OUT_OF_STOCK);
				}
				return OrderItem.create(product, itemCommand.quantity());
			})
			.toList();
		return orderItemService.saveAll(orderItems);
	}

	private List<Coupon> validateCoupons(Long userId, List<UUID> couponIds) {
		if (couponIds == null || couponIds.isEmpty()) {
			return List.of();
		}
		List<UserCoupon> userCoupons = couponService.findMineByUserId(userId).stream()
			.filter(UserCoupon::isValid).toList();
		if (couponIds.size() != userCoupons.size()) {
			throw new CustomException(INVALID_COUPON);
		}
		return userCoupons.stream().map(UserCoupon::getCouponId)
			.map(couponService::findCouponById)
			.collect(Collectors.toList());
	}
}
