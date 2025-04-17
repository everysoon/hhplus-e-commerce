package kr.hhplus.be.server.infra.coupon.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.coupon.CouponStatus;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Entity
@Table(name = "user_coupon")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserCouponEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	private Long id;

	private Long userId;

	private UUID couponId;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private CouponStatus status;

	private LocalDateTime issuedAt;

	public UserCouponEntity(Long userId,UUID couponId, CouponStatus status) {
		this.userId = userId;
		this.couponId = couponId;
		this.status = status;
		this.issuedAt = LocalDateTime.now();
	}
	public static UserCouponEntity from (UserCoupon uc){
		return new UserCouponEntity(
			uc.getUserId(),
			uc.getCouponId(),
			uc.getStatus()
		);
	}
	public UserCoupon toDomain(){
		return new UserCoupon(
			this.id,
			this.userId,
			this.couponId,
			this.status,
			this.issuedAt
		);
	}
}
