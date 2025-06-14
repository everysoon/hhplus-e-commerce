package kr.hhplus.be.server.application.coupon;

import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponStatus;
import kr.hhplus.be.server.domain.coupon.CouponType;
import kr.hhplus.be.server.domain.coupon.UserCoupon;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record UserCouponDetailResult(
	Long userId,
	String couponId,
	CouponStatus status,
	LocalDateTime issuedAt,
	CouponType type,
	String description,
	BigDecimal discount,
	LocalDateTime expired
) {
	public static UserCouponDetailResult of(UserCoupon uc, Coupon coupon) {
		return new UserCouponDetailResult(
			uc.getUserId(),
			coupon.getId(),
			uc.getStatus(),
			uc.getIssuedAt(),
			coupon.getType(),
			coupon.getDescription(),
			coupon.getDiscountAmount(),
			coupon.getExpiredAt()
		);
	}

}
