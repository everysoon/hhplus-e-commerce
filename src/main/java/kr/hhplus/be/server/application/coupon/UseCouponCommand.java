package kr.hhplus.be.server.application.coupon;

import kr.hhplus.be.server.domain.coupon.Coupon;

import java.util.List;

public record UseCouponCommand (
	Long userId,
	List<Coupon> coupons
){
	public static UseCouponCommand of(Long userId, List<Coupon> coupons) {
		return new UseCouponCommand(userId, coupons);
	}
}
