package kr.hhplus.be.server.domain.coupon;

import kr.hhplus.be.server.support.common.exception.CustomException;
import kr.hhplus.be.server.support.config.swagger.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class UserCoupon {

	private final Long id;
	private final Long userId;
	private final Coupon coupon;
	private CouponStatus status;
	private LocalDateTime issuedAt;

	public static UserCoupon of(Long userId, Coupon coupon) {
		return new UserCoupon(null, userId, coupon, CouponStatus.ISSUED, LocalDateTime.now());
	}
	public UserCoupon isValidRestore(){
		if(this.status.equals(CouponStatus.REVOKED)){
			throw new CustomException(ErrorCode.REVOKED_COUPON);
		}
		return this;
	}
	public UserCoupon use() {
		if (this.status == CouponStatus.USED) {
			throw new CustomException(ErrorCode.USED_COUPON);
		}
		if (this.status == CouponStatus.EXPIRED) {
			throw new CustomException(ErrorCode.EXPIRED_COUPON);
		}
		if (this.status == CouponStatus.REVOKED) {
			throw new CustomException(ErrorCode.REVOKED_COUPON);
		}
		this.status = CouponStatus.USED;
		return this;
	}

	public boolean isValid() {
		return this.status == CouponStatus.ISSUED;
	}

	public void restore() {
		this.status = CouponStatus.ISSUED;
	}
}
