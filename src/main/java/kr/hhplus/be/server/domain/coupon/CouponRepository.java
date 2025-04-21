package kr.hhplus.be.server.domain.coupon;

import java.util.List;

public interface CouponRepository {
	Coupon findById(String id);
	Coupon issue(Coupon coupon);
	void updateAll(List<Coupon> coupons);
	List<Coupon> findAll();
	List<Coupon> findAllByIdIn(List<String> couponIds);
}
