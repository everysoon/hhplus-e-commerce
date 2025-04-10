package kr.hhplus.be.server.domain.coupon;

import java.util.UUID;

public interface CouponRepository {
	Coupon findById(UUID id);
}
