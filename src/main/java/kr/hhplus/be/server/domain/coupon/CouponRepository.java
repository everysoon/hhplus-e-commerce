package kr.hhplus.be.server.domain.coupon;

import java.time.LocalDateTime;
import java.util.List;

public interface CouponRepository {
	Coupon findById(String id);
	Coupon issue(String id);
	void updateAll(List<Coupon> coupons);
	List<Coupon> findAll();
	List<Coupon> findExpiredAll(LocalDateTime expiredAt);
	List<Coupon> findNotExpiredAll(LocalDateTime expiredAt);
	Coupon save(Coupon coupon);
	List<Coupon> findAllByIdIn(List<String> couponIds);
}
