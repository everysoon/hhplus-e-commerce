package kr.hhplus.be.server.domain.coupon;

import java.time.LocalDateTime;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.support.common.exception.CustomException;
import kr.hhplus.be.server.support.config.swagger.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserCoupon {
	private final Long id;
	private final Long userId;
	private final String couponId;
	private CouponStatus status;
	private LocalDateTime issuedAt;

	public static UserCoupon of(User user,Coupon coupon) {
		return new UserCoupon(null,user.getId(),coupon.getId(),CouponStatus.ISSUED,LocalDateTime.now());
	}
	public UserCoupon use(){
		if(this.status == CouponStatus.USED){
			throw new CustomException(ErrorCode.USED_COUPON);
		}
		if(this.status == CouponStatus.EXPIRED){
			throw new CustomException(ErrorCode.EXPIRED_COUPON);
		}
		if(this.status == CouponStatus.REVOKED){
			throw new  CustomException(ErrorCode.REVOKED_COUPON);
		}
		this.status =  CouponStatus.USED;
		return this;
	}
	public boolean isValid(){
		return this.status == CouponStatus.ISSUED;
	}
	public void restore(){
		this.status = CouponStatus.ISSUED;
	}
}
