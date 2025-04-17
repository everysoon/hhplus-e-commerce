package kr.hhplus.be.server.application.coupon;

import java.util.List;
import java.util.UUID;

public record IssueCouponCommand(
	Long userId,
	UUID couponId
){
	public static IssueCouponCommand of(Long userId, UUID couponId) {
		return new IssueCouponCommand(userId,couponId);
	}
	public CouponValidCommand toCouponValidCommand() {
		return CouponValidCommand.of(userId, List.of(couponId));
	}
}
