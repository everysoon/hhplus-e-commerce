package kr.hhplus.be.server.domain.coupon;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserCoupon;
import kr.hhplus.be.server.infra.coupon.entity.CouponEntity;
import kr.hhplus.be.server.infra.user.entity.UserCouponEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

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
		if (this.isValid()) {
			return BigDecimal.ZERO; // 만료된 쿠폰은 할인 없음
		}

		return switch (this.type) {
			case FIXED -> this.discount;
			case PERCENT -> price.multiply(this.discount)
				.divide(BigDecimal.valueOf(100));
		};
	}
	public boolean isValid(){
		return expiredAt.isBefore(LocalDateTime.now());
	}
}
