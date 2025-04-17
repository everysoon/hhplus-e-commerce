package kr.hhplus.be.server.application.coupon;

import java.util.List;

public record UseCouponCommand (
	Long userId,
	List<String> couponIds
){
//	public static UseCouponCommand of(User user, List<Coupon> coupons) {
//		return new UseCouponCommand(user, coupons);
//	}
 	public static UseCouponCommand of(Long userId, List<String> couponIds) {
		return new UseCouponCommand(userId, couponIds);
	}
//	public List<UserCoupon> getUserCoupons() {
//		return coupons.stream().map(c->UserCoupon.of(user,c)).collect(Collectors.toList());
//	}
}
