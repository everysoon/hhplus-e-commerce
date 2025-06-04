package kr.hhplus.be.server.domain.coupon.event;

public record CouponIssuedEvent(
	Long userId,
	String couponId
) {
	public String getIdempotentKey() {
		return "coupon.issued:" + userId + couponId;
	}
}
