package kr.hhplus.be.server.application.coupon;

public record CouponIssueDLQEvent(
	Long userId,
	String couponId,
	Exception exception
) {

}
