package kr.hhplus.be.server.application.coupon;

import java.util.UUID;
import kr.hhplus.be.server.domain.coupon.Coupon;

public interface CouponUseCase {
	Coupon findById(UUID id);
}
