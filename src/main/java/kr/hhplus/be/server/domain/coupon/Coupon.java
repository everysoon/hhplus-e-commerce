package kr.hhplus.be.server.domain.coupon;

import jakarta.persistence.*;
import kr.hhplus.be.server.support.common.exception.CustomException;
import kr.hhplus.be.server.support.config.swagger.ErrorCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "coupons")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Coupon {

	@Id
	@Column(updatable = false, nullable = false, length = 36)
	private String id;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private CouponType type;

	private String description;

	@Column(nullable = false)
	private BigDecimal discountAmount = BigDecimal.ZERO; // Fixed 일땐 할인 금액 (원), Percent 일땐 할인 비율 (1 ~ 100)

	private int initialQuantity;

	private int remainingQuantity;

	@Column(nullable = false)
	private LocalDateTime expiredAt;

	@CreatedDate
	@Column(nullable = false)
	private LocalDateTime issuedAt;

	public Coupon issue(Integer quantity) {
		validateStock();
		validExpired();
		decreaseStock(quantity);
		return this;
	}

	public BigDecimal calculateDiscountAmount(BigDecimal price) {
		validExpired();
		return switch (this.type) {
			case FIXED -> this.discountAmount;
			case PERCENT -> price.multiply(this.discountAmount)
				.divide(BigDecimal.valueOf(100));
		};
	}

	public void decreaseStock(Integer quantity) {
		if (this.remainingQuantity - quantity < 0) {
			throw new CustomException(ErrorCode.COUPON_SOLD_OUT);
		}
		this.remainingQuantity -= quantity;
	}

	public void increaseStock() {
		this.remainingQuantity += 1;
	}

	public void validateStock() {
		if (this.initialQuantity < this.remainingQuantity) {
			throw new CustomException(ErrorCode.INVALID_COUPON_QUANTITY);
		}
	}

	public void validExpired() {
		if (expiredAt.isBefore(LocalDateTime.now())) {
			throw new CustomException(ErrorCode.EXPIRED_COUPON);
		}
	}
}
