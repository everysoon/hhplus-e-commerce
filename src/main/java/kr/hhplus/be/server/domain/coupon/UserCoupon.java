package kr.hhplus.be.server.domain.coupon;

import jakarta.persistence.*;
import kr.hhplus.be.server.support.common.exception.CustomException;
import kr.hhplus.be.server.support.config.swagger.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "user_coupons")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserCoupon {

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

	public UserCoupon(Long userId, String couponId) {
		this.userId = userId;
		this.couponId = couponId;
		this.status = CouponStatus.ISSUED;
		this.issuedAt = LocalDateTime.now();
	}

	public UserCoupon isValidRestore() {
		if (this.status.equals(CouponStatus.REVOKED)) {
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

	public void expire() {
		this.status = CouponStatus.EXPIRED;
	}

	public void restore() {
		this.status = CouponStatus.ISSUED;
	}
}
