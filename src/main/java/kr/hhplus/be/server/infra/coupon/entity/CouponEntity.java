package kr.hhplus.be.server.infra.coupon.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Entity
@Table(name = "coupons")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CouponEntity {

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

	public CouponEntity(CouponType type, String description, BigDecimal discountAmount,
		int initialQuantity, int remainingQuantity) {
		this.id = UUID.randomUUID().toString();
		this.type = type;
		this.description = description;
		this.discountAmount = discountAmount;
		this.initialQuantity = initialQuantity;
		this.remainingQuantity = remainingQuantity;
		this.expiredAt = LocalDateTime.now().plusDays(7);
		this.issuedAt = LocalDateTime.now();
	}

	public CouponEntity(String id, CouponType type, String description, BigDecimal discountAmount,
		int initialQuantity, int remainingQuantity,LocalDateTime expiredAt, LocalDateTime issuedAt) {
		this(type, description, discountAmount, initialQuantity, remainingQuantity);
		this.id = id;
		this.expiredAt = expiredAt;
		this.issuedAt = issuedAt;
	}

	public static CouponEntity update(Coupon coupon) {
		return new CouponEntity(
			coupon.getId(),
			coupon.getType(),
			coupon.getDescription(),
			coupon.getDiscountAmount(),
			coupon.getInitialQuantity(),
			coupon.getRemainingQuantity(),
			coupon.getExpiredAt(),
			coupon.getCreatedAt()
		);
	}

	public static CouponEntity from(Coupon coupon) {
		return new CouponEntity(
			coupon.getType(),
			coupon.getDescription(),
			coupon.getDiscountAmount(),
			coupon.getInitialQuantity(),
			coupon.getRemainingQuantity()
		);
	}

	public Coupon toDomain() {
		return new Coupon(
			this.id,
			this.type,
			this.description,
			this.discountAmount,
			this.initialQuantity,
			this.remainingQuantity,
			this.expiredAt,
			this.issuedAt
		);
	}
}
