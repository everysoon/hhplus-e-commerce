package kr.hhplus.be.server.infra.coupon.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import kr.hhplus.be.server.domain.coupon.CouponStatus;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

	private String couponId;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private CouponStatus status;

	private LocalDateTime issuedAt;

	public UserCouponEntity(Long userId, String couponId, CouponStatus status) {
		this.userId = userId;
		this.couponId = couponId;
		this.status = status;
		this.issuedAt = LocalDateTime.now();
	}

	public UserCouponEntity(Long id, Long userId, String couponId, CouponStatus status,
		LocalDateTime issuedAt) {
		this(userId, couponId, status);
		this.id = id;
		this.issuedAt = issuedAt;
	}

	public static UserCouponEntity update(UserCoupon uc) {
		return new UserCouponEntity(
			uc.getId(),
			uc.getUserId(),
			uc.getCouponId(),
			uc.getStatus(),
			uc.getIssuedAt()
		);
	}

	public static UserCouponEntity from(UserCoupon uc) {
		return new UserCouponEntity(
			uc.getUserId(),
			uc.getCouponId(),
			uc.getStatus()
		);
	}

	public UserCoupon toDomain() {
		return new UserCoupon(
			this.id,
			this.userId,
			this.couponId,
			this.status,
			this.issuedAt
		);
	}
}
