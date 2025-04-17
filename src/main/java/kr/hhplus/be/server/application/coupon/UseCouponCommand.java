package kr.hhplus.be.server.application.coupon;

import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.coupon.UserCoupon;

import java.util.List;
import java.util.stream.Collectors;

public record UseCouponCommand (
	User user,
	List<Coupon> coupons
){
	public static UseCouponCommand of(User user, List<Coupon> coupons) {
		return new UseCouponCommand(user, coupons);
	}
	public List<UserCoupon> getUserCoupons() {
		return coupons.stream().map(c->UserCoupon.of(user,c)).collect(Collectors.toList());
	}
}
