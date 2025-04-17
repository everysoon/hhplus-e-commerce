package kr.hhplus.be.server.application.user;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.application.coupon.CouponService;
import kr.hhplus.be.server.application.coupon.IssueCouponCommand;
import kr.hhplus.be.server.application.coupon.UserCouponDetailResult;
import kr.hhplus.be.server.application.point.PointService;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import kr.hhplus.be.server.domain.point.Point;
import kr.hhplus.be.server.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserFacade {
	private final UserService userService;
	private final CouponService couponService;
	private final PointService pointService;

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

	public Point getUserPoint(Long userId) {
		return pointService.getUserPoint(userId);
	}

	public List<UserCouponDetailResult> getUserCoupons(Long userId) {
		return couponService.findUserCouponByUserId(userId).stream().map(uc -> {
			Coupon coupon = couponService.findCouponById(uc.getCouponId());
			return UserCouponDetailResult.of(uc, coupon);
		}).toList();
	}
}
