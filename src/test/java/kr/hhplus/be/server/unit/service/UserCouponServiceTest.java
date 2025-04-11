package kr.hhplus.be.server.unit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import kr.hhplus.be.server.application.coupon.CouponValidCommand;
import kr.hhplus.be.server.application.coupon.UseCouponCommand;
import kr.hhplus.be.server.application.user.UserCouponService;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponStatus;
import kr.hhplus.be.server.domain.coupon.CouponType;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserCoupon;
import kr.hhplus.be.server.domain.user.repository.UserCouponRepository;
import kr.hhplus.be.server.utils.TestFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserCouponServiceTest {

	@Mock
	private UserCouponRepository userCouponRepository;

	@InjectMocks
	private UserCouponService userCouponService;

	@Test
	void 유저_ID로_조회하면_해당_유저의_쿠폰을_반환한다() {
		// given
		Long userId = 1L;
		Coupon coupon = new Coupon(
			UUID.randomUUID(),
			CouponType.FIXED,
			"TEST FIX COUPON",
			TestFixture.toBigDecimal(1000),
			10,
			LocalDateTime.now().plusDays(7),
			LocalDateTime.now()
		);
		User user = mock(User.class);
		UserCoupon userCoupon = new UserCoupon(userId,user,coupon, CouponStatus.ISSUED,LocalDateTime.now());
		when(userCouponRepository.findByUserId(userId)).thenReturn(List.of(userCoupon));
		when(user.getId()).thenReturn(userId);
		// when
		List<UserCoupon> result  = userCouponService.findByUserId(userId);

		// then
		assertNotNull(result);
		assertEquals(userCoupon, result.get(0));
		assertEquals(userId, result.get(0).getUser().getId());
		assertEquals(coupon, result.get(0).getCoupon());
	}

	@Test
	void 해당_UUID_쿠폰아이디_리스트가_해당_유저의_것인지_유효성_체크() {
		// given
		Long userId = 1L;
		List<UUID> couponIds = List.of(UUID.randomUUID());
		CouponValidCommand command = new CouponValidCommand(userId,couponIds);
		Mockito.when(userCouponRepository.existsByUserIdAndCouponId(command))
			.thenReturn(true);

		// when
		Boolean exists = userCouponService.existsByUserIdAndCouponId(command);

		// then
		assertTrue(exists);
		verify(userCouponRepository).existsByUserIdAndCouponId(command);
	}
	@Test
	void 유저_쿠폰_사용_테스트(){
		// given
		UserCoupon userCoupon1 = mock(UserCoupon.class);
		UserCoupon userCoupon2 = mock(UserCoupon.class);
		List<UserCoupon> userCoupons = List.of(userCoupon1, userCoupon2);

		UseCouponCommand command = mock(UseCouponCommand.class);
		when(command.getUserCoupons()).thenReturn(userCoupons);

		// when
		List<UserCoupon> result = userCouponService.use(command);

		// then
		assertEquals(userCoupons, result);
		verify(userCoupon1).use();
		verify(userCoupon2).use();
	}
}
