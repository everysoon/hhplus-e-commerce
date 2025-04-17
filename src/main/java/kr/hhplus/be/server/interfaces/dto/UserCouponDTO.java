package kr.hhplus.be.server.interfaces.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import kr.hhplus.be.server.application.coupon.UserCouponDetailResult;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponStatus;
import kr.hhplus.be.server.domain.coupon.CouponType;

public class UserCouponDTO {

	public record Response(
		Long userId,
		List<CouponDTO.IssuedResponse> coupons
	) {
		public static Response of(Long userId, List<Coupon> coupons,CouponStatus status) {
			List<CouponDTO.IssuedResponse> couponDTOs = coupons.stream()
				.map(c->CouponDTO.IssuedResponse.from(c,status)) // Coupon → CouponResponseDTO
				.toList();

			return new Response(userId, couponDTOs);
		}
	}

	public record CouponDetailResponse(Long userId,
								 String couponId,
								 CouponStatus status,
								 LocalDateTime issuedAt,
								 CouponType type,
								 String description,
								 BigDecimal discount,
								 LocalDateTime expired) {

		public static CouponDetailResponse from(UserCouponDetailResult result) {
			return new CouponDetailResponse(
				result.userId(),
				result.couponId(),
				result.status(),
				result.issuedAt(),
				result.type(),
				result.description(),
				result.discount(),
				result.expired()
			);
		}
	}

}
