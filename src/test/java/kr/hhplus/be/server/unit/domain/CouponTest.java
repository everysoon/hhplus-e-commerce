package kr.hhplus.be.server.unit.domain;

import static kr.hhplus.be.server.utils.TestFixture.toBigDecimal;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponType;
import kr.hhplus.be.server.domain.order.Order;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Coupon Domain Test")
public class CouponTest {

	@Test
	public void 쿠폰_FIX_정상_할인() {
		// given
		Coupon coupon = new Coupon(
			UUID.randomUUID(),
			CouponType.FIXED,
			"Test FIX Coupon",
			toBigDecimal(1000),
			10,
			LocalDateTime.now().plusDays(7),
			LocalDateTime.now()
		);
		Order order = new Order(
			null,
			1L,
			null,
			null,
			toBigDecimal(10000),
			BigDecimal.ZERO,
			LocalDateTime.now()
		);
		// when
		order.applyCoupon(coupon);
		// then
		assertEquals(toBigDecimal(9000), order.getTotalPrice());
		assertEquals(toBigDecimal(1000), order.getTotalDiscount());
	}

	@Test
	public void 쿠폰_PERCENT_정상_할인() {
		// given
		Coupon coupon = new Coupon(
			UUID.randomUUID(),
			CouponType.PERCENT,
			"Test PERCENT Coupon",
			toBigDecimal(10),
			10,
			LocalDateTime.now().plusDays(7),
			LocalDateTime.now()
		);
		Order order = new Order(
			null,
			1L,
			null,
			null,
			toBigDecimal(10000),
			BigDecimal.ZERO,
			LocalDateTime.now()
		);
		// when
		order.applyCoupon(coupon);
		// then
		assertEquals(toBigDecimal(9000), order.getTotalPrice());
		assertEquals(toBigDecimal(1000), order.getTotalDiscount());
	}

	@Test
	public void 쿠폰_만료일_지남으로_사용_불가() {
		// given
		Coupon coupon = new Coupon(
			UUID.randomUUID(),
			CouponType.FIXED,
			"Test FIX Coupon",
			toBigDecimal(1000),
			10,
			LocalDateTime.now().minusDays(7),
			LocalDateTime.now()
		);
		// when
		boolean isExpired = coupon.isExpired();
		// then
		assertEquals(isExpired, true);
	}
}
