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
public class OrderCoupon {
	private final Order order;
	private final Coupon coupon;

	public BigDecimal getDiscountAmount() {
		return coupon.getDiscountAmount(order.getTotalPrice());
	}
}
