package kr.hhplus.be.server.utils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponType;

public class CouponTestFixture {
	public static Coupon createDBCoupon(String couponId){
		return new Coupon(
			couponId,
			CouponType.PERCENT,
			"Quia perspiciatis qui enim sapiente aut amet nihil.",
			BigDecimal.valueOf(3590.00),
			458,
			749,
			LocalDateTime.of(2025,04,24,22,32,49,119947),
			LocalDateTime.of(2025,04,17,22,32,49,119947)
		);
	}
}
