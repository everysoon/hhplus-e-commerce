package kr.hhplus.be.server.application.coupon;

import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.support.utils.LockKeyPrefix;

import java.util.List;
import java.util.stream.Collectors;

public class CouponCommand {
	public record Issue(
		Long userId,
		String couponId
	) {
		public static Issue of(Long userId, String couponId) {
			return new Issue(userId, couponId);
		}
		public UnitCouponValid toUnitCouponValid(Coupon coupon) {
			return new CouponCommand.UnitCouponValid(userId, coupon);
		}
		public String getLockKey(){
			return LockKeyPrefix.COUPON.createKey(couponId);
		}
	}

	public record Restore(
		Long userId,
		List<Coupon> coupons
	) {
		public static Restore of(Long userId, List<Coupon> coupons) {
			return new Restore(userId, coupons);
		}

		public List<String> couponIds() {
			return coupons.stream().map(Coupon::getId).collect(Collectors.toList());
		}
	}

	public record UnitCouponValid(
		Long userId,
		Coupon coupon
	) {
		public static UnitCouponValid of(Long userId, Coupon coupon) {
			return new UnitCouponValid(
				userId,
				coupon
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
