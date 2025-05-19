package kr.hhplus.be.server.application.coupon;

public record CouponIssuedEvent(
	Long userId,
	String couponId
) {

}
