package kr.hhplus.be.server.domain.coupon;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CouponRepository {
	Optional<Coupon> findById(UUID id);
	Coupon issue(Coupon coupon);
	List<Coupon> validateCoupons(List<UUID> couponIds);
}
