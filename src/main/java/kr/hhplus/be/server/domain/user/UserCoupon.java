package kr.hhplus.be.server.domain.user;

import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponStatus;
import kr.hhplus.be.server.infra.user.entity.UserCouponEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.EnumSet;

@Getter
@AllArgsConstructor
public class UserCoupon {
	private final Long id;
	private final User user;
	private final Coupon coupon;
	private CouponStatus status;
	private LocalDateTime issuedAt;

	public boolean isValid() {
		return EnumSet.of(CouponStatus.PENDING, CouponStatus.ISSUED).contains(this.status)
			&& !coupon.isExpired();
	}
	public static UserCoupon of(User user,Coupon coupon) {
		return new UserCoupon(null,user,coupon,CouponStatus.ISSUED,LocalDateTime.now());
	}
	public UserCoupon use(){
		this.status =  CouponStatus.USED;
		return this;
	}
	public static UserCoupon from(UserCouponEntity entity) {
		return new UserCoupon(
			entity.getId(),
			User.from(entity.getUserEntity()),
			Coupon.from(entity.getCouponEntity()),
			entity.getStatus(),
			entity.getIssuedAt()
		);
	}
}
