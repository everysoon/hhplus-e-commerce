package kr.hhplus.be.server.utils;

import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class CouponTestFixture {
	public static Coupon create(Integer quantity) {
		return new Coupon(
			UUID.randomUUID().toString(),
			CouponType.FIXED,
			"TEST Coupon DESC",
			BigDecimal.valueOf(1000),
			quantity,
			quantity,
			LocalDateTime.now().plusDays(7),
			LocalDateTime.now()
		);
	}

	public static Coupon create(String couponId) {
		return new Coupon(
			couponId,
			CouponType.FIXED,
			"TEST Coupon DESC",
			BigDecimal.valueOf(1000),
			100,
			100,
			LocalDateTime.now().plusDays(7),
			LocalDateTime.now()
		);
	}
}
