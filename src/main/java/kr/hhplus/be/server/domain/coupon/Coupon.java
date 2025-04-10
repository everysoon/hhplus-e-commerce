package kr.hhplus.be.server.domain.coupon;

import kr.hhplus.be.server.infra.coupon.entity.CouponEntity;
import kr.hhplus.be.server.support.common.exception.CustomException;
import kr.hhplus.be.server.support.config.swagger.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class Coupon {
	private final UUID id;
	private final CouponType type;
	private final String description;
	private final BigDecimal discount;
	private final Integer stock;
	private final LocalDateTime expiredAt;
	private final LocalDateTime createdAt;

	public static Coupon from(CouponEntity entity){
		return new Coupon(
			entity.getId(),
			entity.getType(),
			entity.getDescription(),
			entity.getDiscount(),
			entity.getStock(),
			entity.getExpiredAt(),
			entity.getCreatedAt()
		);
	}
	public BigDecimal getDiscountAmount(BigDecimal price) {
		if (this.isExpired() || isOlderThan7Days()) {
			throw new CustomException(ErrorCode.INVALID_COUPON);
		}
		return switch (this.type) {
			case FIXED -> this.discount;
			case PERCENT -> price.multiply(this.discount)
				.divide(BigDecimal.valueOf(100));
		};
	}
	public void use(){

	}
	public boolean isOlderThan7Days(){
		return createdAt.plusDays(7).isBefore(LocalDateTime.now());
	}
	public boolean isExpired(){
		return expiredAt.isBefore(LocalDateTime.now());
	}
}
