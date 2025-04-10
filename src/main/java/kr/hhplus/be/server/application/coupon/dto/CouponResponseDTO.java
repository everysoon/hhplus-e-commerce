package kr.hhplus.be.server.application.coupon.dto;

import java.time.LocalDateTime;
import java.util.UUID;
import kr.hhplus.be.server.domain.coupon.CouponStatus;
import kr.hhplus.be.server.domain.coupon.CouponType;
import kr.hhplus.be.server.domain.user.UserCoupon;

public record CouponResponseDTO(
	UUID couponId,
	CouponType couponType,
	String description,
	Integer remainingStock,
	CouponStatus couponStatus,
	LocalDateTime issuedAt,
	LocalDateTime expiredAt
) {
	public static CouponResponseDTO from(UserCoupon userCoupon) {
		return new CouponResponseDTO(
			userCoupon.getCoupon().getId(),
			userCoupon.getCoupon().getType(),
			userCoupon.getCoupon().getDescription(),
			userCoupon.getRemainingStock(),
			userCoupon.getStatus(),
			userCoupon.getIssuedAt(),
			userCoupon.getCoupon().getExpiredAt()
		);
	}
}
