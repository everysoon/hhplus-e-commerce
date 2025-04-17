package kr.hhplus.be.server.interfaces.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponStatus;
import kr.hhplus.be.server.domain.coupon.CouponType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
	public record OrderCouponResponse(
		String couponId,
		CouponType couponType,
		String couponDescription,
		BigDecimal discountAmount,
		LocalDateTime expiredAt
	){
		public static OrderCouponResponse of(Coupon coupon){
			return new OrderCouponResponse(
				coupon.getId(),
				coupon.getType(),
				coupon.getDescription(),
				coupon.getDiscountAmount(),
				coupon.getExpiredAt()
			);
		}
	}
	public record IssuedResponse(
		String couponId,
		CouponType couponType,
		String description,
//		Integer remainingStock,
		CouponStatus couponStatus,
		LocalDateTime issuedAt,
		LocalDateTime expiredAt
	){
		public static IssuedResponse from(Coupon coupon,CouponStatus status) {
			return new IssuedResponse(
				coupon.getId(),
				coupon.getType(),
				coupon.getDescription(),
				status,
				coupon.getCreatedAt(),
				coupon.getExpiredAt()
			);
		}
	}
}
