package kr.hhplus.be.server.application.user;

import static kr.hhplus.be.server.support.config.swagger.ErrorCode.COUPON_EXPIRED;
import static kr.hhplus.be.server.support.config.swagger.ErrorCode.COUPON_SOLD_OUT;
import static kr.hhplus.be.server.support.config.swagger.ErrorCode.DUPLICATE_COUPON_CLAIM;

import java.util.List;
import kr.hhplus.be.server.application.coupon.CouponService;
import kr.hhplus.be.server.application.coupon.IssueCouponCommand;
import kr.hhplus.be.server.application.coupon.IssuedCouponResult;
import kr.hhplus.be.server.application.order.service.OrderHistoryService;
import kr.hhplus.be.server.application.order.service.OrderItemService;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.order.OrderDetail;
import kr.hhplus.be.server.domain.order.OrderHistory;
import kr.hhplus.be.server.domain.order.OrderItem;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserCoupon;
import kr.hhplus.be.server.support.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserFacade {
	private final UserService userService;
	private final OrderHistoryService orderHistoryService;
	private final OrderItemService orderItemService;
	private final UserCouponService userCouponService;
	private final CouponService couponService;

	public OrderDetail getOrders(Long userId) {
		List<OrderHistory> histories = orderHistoryService.findByUserId(userId);
		List<OrderItem> orderItems = histories.stream()
			.map(OrderHistory::getOrderId)
			.map(orderItemService::findByOrderId)
			.flatMap(List::stream)
			.toList();

		return OrderDetail.from(userId, orderItems, histories);
	}

	public IssuedCouponResult issueCoupon(IssueCouponCommand command) {
		long issuedCoupons = userCouponService.countCouponByUserId(command);
		if (issuedCoupons >= 0) {
			throw new CustomException(DUPLICATE_COUPON_CLAIM);
		}
		Coupon coupon = couponService.findById(command.couponId());
		if(coupon.isOutOfSock()){
			throw new CustomException(COUPON_SOLD_OUT);
		}
		if(coupon.isExpired()){
			throw new CustomException(COUPON_EXPIRED);
		}
		User user = userService.get(command.userId());
		UserCoupon userCoupon = UserCoupon.of(user, coupon);
		userCouponService.save(userCoupon);

		return IssuedCouponResult.of(userCoupon);
	}

	public List<UserCoupon> getUserCoupons(Long userId) {
		return userCouponService.findByUserId(userId);
	}
}
