package kr.hhplus.be.server.domain.user.repository;

import kr.hhplus.be.server.application.coupon.CouponValidCommand;
import kr.hhplus.be.server.application.coupon.IssueCouponCommand;
import kr.hhplus.be.server.domain.coupon.UserCoupon;

import java.util.List;
import java.util.UUID;

public interface UserCouponRepository {
	List<UserCoupon> findByUserId(Long userId);
	void validateUserCoupons(CouponValidCommand command);
	void validateDuplicateIssued(IssueCouponCommand command);
	UserCoupon save(UserCoupon coupon);
	List<UserCoupon> findByUserIdAndCouponIds(Long userId, List<UUID> couponIds);
}
