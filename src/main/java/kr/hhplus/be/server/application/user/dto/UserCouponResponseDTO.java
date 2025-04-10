package kr.hhplus.be.server.application.user.dto;

import java.util.List;
import kr.hhplus.be.server.application.coupon.dto.CouponResponseDTO;
import kr.hhplus.be.server.domain.user.UserCoupon;


public record UserCouponResponseDTO(
	Long userId,
	List<CouponResponseDTO> coupons
) {
	public static UserCouponResponseDTO of(Long userId, List<UserCoupon> userCoupons) {
		List<CouponResponseDTO> couponDTOs = userCoupons.stream()
			.map(CouponResponseDTO::from) // Coupon → CouponResponseDTO
			.toList();

		return new UserCouponResponseDTO(userId, couponDTOs);
	}
}
