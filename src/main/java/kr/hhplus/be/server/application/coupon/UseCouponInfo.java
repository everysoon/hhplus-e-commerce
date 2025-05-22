package kr.hhplus.be.server.application.coupon;

import kr.hhplus.be.server.domain.coupon.Coupon;

import java.util.List;

public record UseCouponInfo (
	Long userId,
	List<Coupon> coupons
){
	public static UseCouponInfo from(Long userId,List<Coupon> coupons) {
		return new UseCouponInfo(
			userId,
			coupons
		);
	}
	public List<String> couponIds(){
		if(coupons == null || coupons.isEmpty()) return null;
		return coupons.stream().map(Coupon::getId).toList();
	}
}
