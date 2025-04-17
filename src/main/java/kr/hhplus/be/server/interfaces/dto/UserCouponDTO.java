package kr.hhplus.be.server.interfaces.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import kr.hhplus.be.server.application.coupon.IssuedCouponResult;
import kr.hhplus.be.server.domain.coupon.CouponStatus;
import kr.hhplus.be.server.domain.coupon.CouponType;
import kr.hhplus.be.server.domain.coupon.UserCoupon;

import java.util.List;

public class UserCouponDTO {

	public record Response(
		Long userId,
		List<CouponDTO.IssuedResponse> coupons
	) {

		public static Response of(Long userId, List<UserCoupon> userCoupons) {
			List<CouponDTO.IssuedResponse> couponDTOs = userCoupons.stream()
				.map(CouponDTO.IssuedResponse::from) // Coupon → CouponResponseDTO
				.toList();

			return new Response(userId, couponDTOs);
		}
	}

	public record IssuedResponse(Long userId,
								 UUID couponId,
								 CouponStatus status,
								 LocalDateTime issuedAt,
								 CouponType type,
								 String description,
								 BigDecimal discount,
								 LocalDateTime expired) {

		public static IssuedResponse from(IssuedCouponResult ucr) {
			return new IssuedResponse(
				ucr.userId(),
				ucr.couponId(),
				ucr.status(),
				ucr.issuedAt(),
				ucr.type(),
				ucr.description(),
				ucr.discount(),
				ucr.expired()
			);
		}
	}

}
