package kr.hhplus.be.server.domain.coupon.event;

public record CouponIssueDLQEvent(
	Long userId,
	String couponId,
	Exception exception
) {

}
