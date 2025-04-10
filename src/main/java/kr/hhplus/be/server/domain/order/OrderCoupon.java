package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.domain.coupon.Coupon;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class OrderCoupon {
	private final Order order;
	private final Coupon coupon;

	public BigDecimal getDiscountAmount() {
		return coupon.getDiscountAmount(order.getTotalPrice());
	}
	public static OrderCoupon of(Order order, Coupon coupon) {
		return new OrderCoupon(order,coupon);
	}
}
