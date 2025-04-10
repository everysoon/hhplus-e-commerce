package kr.hhplus.be.server.domain.user;

import java.time.LocalDateTime;
import java.util.EnumSet;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponStatus;
import kr.hhplus.be.server.infra.user.entity.UserCouponEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserCoupon {
	private final Long id;
	private final User user;
	private final Coupon coupon;
	private final CouponStatus status;
	private Integer remainingStock;
	private LocalDateTime issuedAt;
	public boolean isValid(){
		return EnumSet.of(CouponStatus.PENDING, CouponStatus.ISSUED).contains(this.status);
	}
	public static UserCoupon from(UserCouponEntity entity){
		return new UserCoupon(
			entity.getId(),
			User.from(entity.getUserEntity()),
			Coupon.from(entity.getCouponEntity()),
			entity.getStatus(),
			entity.getRemainingStock(),
			entity.getIssuedAt()
		);
	}
}
