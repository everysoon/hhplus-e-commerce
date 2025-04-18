package kr.hhplus.be.server.domain.coupon;

import java.util.List;
import java.util.Optional;

public interface CouponRepository {
	Optional<Coupon> findById(String id);
	Coupon issue(Coupon coupon);
	void updateAll(List<Coupon> coupons);
	List<Coupon> validateCoupons(List<String> couponIds);
	List<Coupon> findAll();
}
