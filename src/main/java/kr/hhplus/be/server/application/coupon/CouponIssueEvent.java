package kr.hhplus.be.server.application.coupon;

import org.springframework.context.ApplicationEvent;

public class CouponIssueEvent extends ApplicationEvent {
	private final Long userId;
	private final String couponId;
	public CouponIssueEvent(Object source,Long userId,String couponId) {
		super(source);
		this.userId = userId;
		this.couponId = couponId;
	}
}
