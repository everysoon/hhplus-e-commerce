package kr.hhplus.be.server.domain.coupon;

import java.util.List;

public interface CouponRepository {
	Coupon findById(String id);
	Coupon issue(String id);
	Coupon findByIdWithLock(String id);
	void updateAll(List<Coupon> coupons);
	List<Coupon> findAll();
	Coupon save(Coupon coupon);
	List<Coupon> findAllByIdIn(List<String> couponIds);
}
