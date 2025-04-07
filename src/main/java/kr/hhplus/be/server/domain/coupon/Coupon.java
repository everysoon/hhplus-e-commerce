package kr.hhplus.be.server.domain.coupon;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Coupon {
	private final Long id;
	private final CouponType type;
	private final String description;
	private final BigDecimal discount;
	private final Integer stock;
	private final LocalDateTime expiredAt;
	private final LocalDateTime createdAt;

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
