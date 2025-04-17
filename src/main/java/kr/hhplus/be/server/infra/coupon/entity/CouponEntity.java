package kr.hhplus.be.server.infra.coupon.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import kr.hhplus.be.server.domain.coupon.CouponType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

@Getter
@Entity
@Table(name = "coupons")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CouponEntity {

	@Id
	@GeneratedValue(generator = "UUID")
	@Column(updatable = false, nullable = false)
	private UUID id;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private CouponType type;

	private String description;

//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "order_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
//	private OrderEntity order;

	@Column(nullable = false)
	private BigDecimal discountAmount = BigDecimal.ZERO; // Fixed 일땐 할인 금액 (원), Percent 일땐 할인 비율 (1 ~ 100)

	private int initialQuantity;

	private int remainingQuantity;

	@Column(nullable = false)
	private LocalDateTime expiredAt;

	@CreatedDate
	@Column(nullable = false)
	private LocalDateTime issuedAt;
}
