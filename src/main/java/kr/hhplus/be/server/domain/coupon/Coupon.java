package kr.hhplus.be.server.domain.coupon;

import kr.hhplus.be.server.support.common.exception.CustomException;
import kr.hhplus.be.server.support.config.swagger.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class Coupon {
	private final String id;
	private final CouponType type;
	private final String description;
	private final BigDecimal discountAmount;
	private int initialQuantity;
	private int remainingQuantity;
	private final LocalDateTime expiredAt;
	private final LocalDateTime createdAt;


	public Coupon issue(){
		validateStock();
		validExpired();
		decreaseStock();
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
	public void decreaseStock(){
		if(this.remainingQuantity <= 0){
			throw new CustomException(ErrorCode.COUPON_SOLD_OUT);
		}
		this.remainingQuantity-=1;
	}
	public void increaseStock(){
		this.remainingQuantity+=1;
	}
	public void validateStock() {
		if (this.initialQuantity < this.remainingQuantity) {
			throw new CustomException(ErrorCode.INVALID_COUPON_QUANTITY);
		}
	}
	public void validExpired(){
		if(expiredAt.isBefore(LocalDateTime.now())){
			throw new CustomException(ErrorCode.EXPIRED_COUPON);
		}
	}
}
