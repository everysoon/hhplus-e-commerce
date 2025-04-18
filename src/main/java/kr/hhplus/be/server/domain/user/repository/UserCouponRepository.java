package kr.hhplus.be.server.domain.user.repository;

import kr.hhplus.be.server.application.coupon.CouponValidCommand;
import kr.hhplus.be.server.application.coupon.IssueCouponCommand;
import kr.hhplus.be.server.domain.coupon.UserCoupon;

import java.util.List;

public interface UserCouponRepository {
	List<UserCoupon> findAll();
	List<UserCoupon> findByUserId(Long userId);
	void validateUserCoupons(CouponValidCommand command);
	void validateDuplicateIssued(IssueCouponCommand command);
	UserCoupon save(IssueCouponCommand command);
	List<UserCoupon> findByUserIdAndCouponIds(Long userId, List<String> couponIds);
	void updateAll(List<UserCoupon> userCoupons);
	void saveAll(List<UserCoupon> userCoupons);
}
