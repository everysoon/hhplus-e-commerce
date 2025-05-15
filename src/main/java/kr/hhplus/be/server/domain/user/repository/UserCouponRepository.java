package kr.hhplus.be.server.domain.user.repository;

import kr.hhplus.be.server.application.coupon.CouponCommand;
import kr.hhplus.be.server.domain.coupon.UserCoupon;

import java.util.List;

public interface UserCouponRepository {
	List<UserCoupon> findAll();
	List<UserCoupon> findByUserId(Long userId);
	UserCoupon save(CouponCommand.UnitCouponValid command);
	List<UserCoupon> findByUserIdAndCouponIds(Long userId, List<String> couponIds);
	void updateAll(List<UserCoupon> userCoupons);
	List<UserCoupon> saveAll(List<UserCoupon> userCoupons);
}
