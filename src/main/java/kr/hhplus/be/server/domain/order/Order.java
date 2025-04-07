package kr.hhplus.be.server.domain.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.infra.coupon.entity.CouponEntity;
import kr.hhplus.be.server.infra.order.entity.OrderCouponEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Order {
	private final Long id;
	private final Long userId;
	private final Long paymentId;
	private final List<OrderCoupon> orderCoupons = new ArrayList<>();
	private BigDecimal totalPrice;
	private BigDecimal totalDiscount;
	private final LocalDateTime orderedAt;

	public BigDecimal getTotalPrice() {
		return this.totalPrice.subtract(this.totalDiscount);
	}

	public void applyCoupon(Coupon coupon) {
		this.orderCoupons.add(new OrderCoupon(this, coupon));
		calculateTotalDiscount();
	}

	public void calculateTotalDiscount() {
		this.totalDiscount = orderCoupons.stream()
			.map(OrderCoupon::getDiscountAmount)
			.reduce(BigDecimal.ZERO, BigDecimal::add);
	}
}
