package kr.hhplus.be.server.interfaces.dto;

import kr.hhplus.be.server.domain.user.UserCoupon;

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

}
