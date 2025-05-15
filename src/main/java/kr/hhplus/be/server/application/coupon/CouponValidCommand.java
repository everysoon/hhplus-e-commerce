package kr.hhplus.be.server.application.coupon;

import java.util.List;

public record CouponValidCommand (
	Long userId,
	List<String> couponIds
){
	public static CouponValidCommand of(Long userId, List<String> couponIds) {
		return new CouponValidCommand(
			userId,
			couponIds
		);
	}
}
