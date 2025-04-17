package kr.hhplus.be.server.application.coupon;

import jakarta.transaction.Transactional;
import java.util.List;
import kr.hhplus.be.server.application.user.UserService;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import kr.hhplus.be.server.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CouponFacade {

	private final UserService userService;
	private final CouponService couponService;

	public List<UserCouponDetailResult> getUserCoupons(Long userId) {
		return couponService.findUserCouponByUserId(userId).stream().map(uc -> {
			Coupon coupon = couponService.findCouponById(uc.getCouponId());
			return UserCouponDetailResult.of(uc, coupon);
		}).toList();
	}

	@Transactional
	public UserCouponDetailResult issueCoupon(IssueCouponCommand command) {
		// 쿠폰 중복 발급인지 확인
		couponService.validateDuplicateIssued(command);

		Coupon coupon = couponService.findCouponById(command.couponId());
		coupon.issue();

		User user = userService.get(command.userId());

		UserCoupon userCoupon = couponService.issueByUser(UserCoupon.of(user, coupon));

		return UserCouponDetailResult.of(userCoupon, coupon);
	}

}
