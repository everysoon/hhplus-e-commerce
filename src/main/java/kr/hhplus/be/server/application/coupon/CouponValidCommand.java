package kr.hhplus.be.server.application.coupon;

import java.util.List;
import java.util.UUID;

public record CouponValidCommand (
	Long userId,
	List<UUID> couponIds
){
	public static CouponValidCommand of(Long userId, List<UUID> couponIds) {
		return new CouponValidCommand(
			userId,
			couponIds
		);
	}
}
