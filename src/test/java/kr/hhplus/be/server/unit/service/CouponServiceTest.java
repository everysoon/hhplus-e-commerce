package kr.hhplus.be.server.unit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;
import java.util.UUID;
import kr.hhplus.be.server.application.coupon.CouponService;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponRepository;
import kr.hhplus.be.server.domain.coupon.CouponType;
import kr.hhplus.be.server.utils.TestFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CouponServiceTest {

	@Mock
	private CouponRepository couponRepository;

	@InjectMocks
	private CouponService couponService;

	@Test
	void 쿠폰_ID로_조회_정상작동() {
		// given
		UUID couponId = UUID.randomUUID();
		Coupon expectedCoupon = new Coupon(
			couponId,
			CouponType.FIXED,
			"TEST FIX COUPON",
			TestFixture.toBigDecimal(1000),
			10,
			LocalDateTime.now().plusDays(7),
			LocalDateTime.now()
		);
		Mockito.when(couponRepository.findById(couponId)).thenReturn(expectedCoupon);

		// when
		Coupon result = couponService.findById(couponId);

		// then
		assertNotNull(result);
		assertEquals(expectedCoupon, result);
	}
}
