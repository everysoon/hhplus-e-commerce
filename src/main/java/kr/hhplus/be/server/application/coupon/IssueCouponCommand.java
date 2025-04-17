package kr.hhplus.be.server.application.coupon;

import java.util.List;

public record IssueCouponCommand(
	Long userId,
	String couponId
){
	public static IssueCouponCommand of(Long userId, String couponId) {
		return new IssueCouponCommand(userId,couponId);
	}
	public CouponValidCommand toCouponValidCommand() {
		return CouponValidCommand.of(userId, List.of(couponId));
	}
}
