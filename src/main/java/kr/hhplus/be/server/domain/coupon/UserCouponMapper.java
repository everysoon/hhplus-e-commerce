package kr.hhplus.be.server.domain.coupon;

import kr.hhplus.be.server.infra.coupon.entity.UserCouponEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserCouponMapper {
	private final CouponRepository couponRepository;
	public UserCouponEntity toEntity(UserCoupon domain) {
		return new UserCouponEntity(
			domain.getId(),
			domain.getUserId(),
			domain.getCoupon().getId(),
			domain.getStatus(),
			domain.getIssuedAt()
		);
	}
	public UserCoupon toDomain(UserCouponEntity entity){
		Coupon coupon = couponRepository.findById(entity.getCouponId());
		return new UserCoupon(
			entity.getId(),
			entity.getUserId(),
			coupon,
			entity.getStatus(),
			entity.getIssuedAt()
		);
	}
}
