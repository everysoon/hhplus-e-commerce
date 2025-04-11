package kr.hhplus.be.server.unit.domain;

import static kr.hhplus.be.server.utils.TestFixture.toBigDecimal;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.UUID;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponStatus;
import kr.hhplus.be.server.domain.coupon.CouponType;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserCoupon;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

@DisplayName("User Coupon Domain Test")
public class UserCouponTest {

	@Test
	public void 유저_쿠폰_사용_상태_변환() {
		// given
		User user = Mockito.mock(User.class);
		Coupon coupon = Mockito.mock(Coupon.class);
		UserCoupon userCoupon = new UserCoupon(
			null,
			user,
			coupon,
			CouponStatus.ISSUED,
			LocalDateTime.now()
		);
		// when
		userCoupon.use();
		// then
		assertEquals(CouponStatus.USED, userCoupon.getStatus());
	}

	@Test
	public void 쿠폰_유효성_체크() {
		// given
		User user = Mockito.mock(User.class);
		Coupon coupon = new Coupon(
			UUID.randomUUID(),
			CouponType.FIXED,
			"TEST FIX COUPON",
			toBigDecimal(1000),
			10,
			LocalDateTime.now().plusDays(7),
			LocalDateTime.now()
		);
		UserCoupon userCoupon = new UserCoupon(
			null,
			user,
			coupon,
			CouponStatus.EXPIRED,
			LocalDateTime.now()
		);
		// when
		boolean valid = userCoupon.isValid();
		// then
		assertEquals(valid, false);
	}
}
