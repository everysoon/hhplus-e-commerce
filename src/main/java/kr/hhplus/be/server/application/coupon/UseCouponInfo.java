package kr.hhplus.be.server.application.coupon;

import java.util.List;
import kr.hhplus.be.server.domain.coupon.Coupon;

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
}
