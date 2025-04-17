package kr.hhplus.be.server.domain.coupon;

import java.util.UUID;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.infra.coupon.entity.UserCouponEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserCoupon {
	private final Long id;

	private final Long userId;
	private final UUID couponId;
	private CouponStatus status;

	public static UserCoupon of(User user,Coupon coupon) {
		return new UserCoupon(null,user.getId(),coupon.getId(),CouponStatus.ISSUED);
	}
	public UserCoupon use(){
		this.status =  CouponStatus.USED;
		return this;
	}
	public boolean isValid(){
		return  this.status == CouponStatus.ISSUED;
	}
	public static UserCoupon from(UserCouponEntity entity) {
		return new UserCoupon(
			entity.getId(),
			entity.getUser().getId(),
			entity.getCoupon().getId(),
			entity.getStatus()
		);
	}

}
