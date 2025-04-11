package kr.hhplus.be.server.application.coupon;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import kr.hhplus.be.server.domain.coupon.CouponStatus;
import kr.hhplus.be.server.domain.coupon.CouponType;
import kr.hhplus.be.server.domain.user.UserCoupon;

public record IssuedCouponResult (
	Long userId,
	UUID couponId,
	CouponStatus status,
	LocalDateTime issuedAt,
	CouponType type,
	String description,
	BigDecimal discount,
	LocalDateTime expired
){
	public static IssuedCouponResult of(UserCoupon uc) {
		return new IssuedCouponResult(
			uc.getUser().getId(),
			uc.getCoupon().getId(),
			uc.getStatus(),
			uc.getIssuedAt(),
			uc.getCoupon().getType(),
			uc.getCoupon().getDescription(),
			uc.getCoupon().getDiscount(),
			uc.getCoupon().getExpiredAt()
		);
	}
}
