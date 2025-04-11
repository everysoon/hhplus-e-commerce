package kr.hhplus.be.server.domain.coupon;

import java.util.Optional;
import java.util.UUID;

public interface CouponRepository {
	Optional<Coupon> findById(UUID id);
	Coupon save(Coupon coupon);
}
