package kr.hhplus.be.server.domain.coupon;

import java.util.UUID;
import kr.hhplus.be.server.infra.coupon.entity.CouponEntity;

public interface CouponRepository {
	Coupon findById(UUID id);
}
