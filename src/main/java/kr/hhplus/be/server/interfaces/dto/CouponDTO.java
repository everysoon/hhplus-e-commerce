package kr.hhplus.be.server.interfaces.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import kr.hhplus.be.server.domain.coupon.CouponStatus;
import kr.hhplus.be.server.domain.coupon.CouponType;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

public class CouponDTO {
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	@Builder
	public static class CreateRequest{
		private CouponType couponType;
		private String description;
		private Integer remainingStock;
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
		private LocalDateTime expiredAt;
	}
	public record IssuedResponse(
		UUID couponId,
		CouponType couponType,
		String description,
//		Integer remainingStock,
		CouponStatus couponStatus,
		LocalDateTime issuedAt,
		LocalDateTime expiredAt
	){
		public static IssuedResponse from(UserCoupon userCoupon) {
			return new IssuedResponse(
				userCoupon.getCoupon().getId(),
				userCoupon.getCoupon().getType(),
				userCoupon.getCoupon().getDescription(),
				userCoupon.getStatus(),
				userCoupon.getIssuedAt(),
				userCoupon.getCoupon().getExpiredAt()
			);
		}
	}
}
