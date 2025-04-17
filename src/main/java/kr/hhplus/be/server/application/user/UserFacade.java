package kr.hhplus.be.server.application.user;

import static kr.hhplus.be.server.support.config.swagger.ErrorCode.DUPLICATE_COUPON_CLAIM;

import jakarta.transaction.Transactional;
import java.util.List;
import kr.hhplus.be.server.application.coupon.CouponService;
import kr.hhplus.be.server.application.coupon.IssueCouponCommand;
import kr.hhplus.be.server.application.coupon.IssuedCouponResult;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.support.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserFacade {
	private final UserService userService;
	private final CouponService couponService;

	@Transactional
	public IssuedCouponResult issueCoupon(IssueCouponCommand command) {
		// 쿠폰 중복 발급인지 확인
		long issuedCoupons = couponService.countCouponByUserId(command);
		if (issuedCoupons > 0) {
			throw new CustomException(DUPLICATE_COUPON_CLAIM);
		}

		Coupon coupon = couponService.findCouponById(command.couponId());
		coupon.issue();

		User user = userService.get(command.userId());

		UserCoupon userCoupon = UserCoupon.of(user, coupon);
		couponService.saveUserCoupon(userCoupon);

		return IssuedCouponResult.of(userCoupon);
	}

	public List<UserCoupon> getUserCoupons(Long userId) {
		return couponService.findMineByUserId(userId);
	}
}
