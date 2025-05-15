package kr.hhplus.be.server.application.order;

import java.math.BigDecimal;
import java.util.List;
import kr.hhplus.be.server.application.coupon.UseCouponInfo;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.order.OrderItem;

public record CreateOrderCommand(

	List<OrderItem> orderItems,
	UseCouponInfo couponInfo
) {

	public static CreateOrderCommand of(List<OrderItem> orderItems, UseCouponInfo couponInfo) {
		return new CreateOrderCommand(orderItems, couponInfo);
	}

	public List<String> couponIdsToString() {
		if (couponInfo == null || couponInfo.coupons() == null ||couponInfo.coupons().isEmpty()) {
			return null;
		}
		return couponInfo.coupons().stream().map(Coupon::getId).toList();
	}
	public BigDecimal getDiscountAmount(BigDecimal totalPrice) {
		if (couponInfo == null || couponInfo.coupons() == null || couponInfo.coupons().isEmpty()) {
			return BigDecimal.ZERO;
		}
		return couponInfo.coupons().stream().map(
			c -> c.calculateDiscountAmount(totalPrice)
		).reduce(BigDecimal.ZERO, BigDecimal::add);
	}
}
