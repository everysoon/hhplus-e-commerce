package kr.hhplus.be.server.application.coupon;

import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.event.CouponIssuedEvent;
import kr.hhplus.be.server.support.aop.lock.LockKeyPrefix;

import java.util.List;

public class CouponCommand {
	public record Issue(
		Long userId,
		String couponId
	) {
		public static Issue of(Long userId, String couponId) {
			return new Issue(userId, couponId);
		}
		public UnitCouponValid toUnitCouponValid(String couponId) {
			return new CouponCommand.UnitCouponValid(userId, couponId);
		}
		public String getLockKey(){
			return LockKeyPrefix.COUPON.createKey(couponId);
		}
		public CouponIssuedEvent toEvent(){
			return new CouponIssuedEvent(userId, couponId);
		}
	}

	public record Restore(
		Long userId,
		List<String> couponIds
	) {
		public static Restore of(Long userId, List<String> couponIds) {
			return new Restore(userId, couponIds);
		}

	}
	public record MultiCouponValid(
		List<Long> userIds,
		String couponId
	) {
		public static UnitCouponValid of(Long userId, String couponId) {
			return new UnitCouponValid(
				userId,
				couponId
			);
		}
	}

	public record UnitCouponValid(
		Long userId,
		String couponId
	) {
		public static UnitCouponValid of(Long userId, String couponId) {
			return new UnitCouponValid(
				userId,
				couponId
			);
		}
	}

	public record Use(
		Long userId,
		List<String> couponIds
	) {
		public static Use of(Long userId, List<String> couponIds) {
			return new Use(userId, couponIds);
		}
	}

	public record CouponValid(
		Long userId,
		List<Coupon> coupons
	) {
		public static CouponValid of(Long userId, List<Coupon> coupons) {
			return new CouponValid(
				userId,
				coupons
			);
		}
	}
}
