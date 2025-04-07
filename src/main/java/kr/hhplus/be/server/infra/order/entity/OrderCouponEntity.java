package kr.hhplus.be.server.infra.order.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import kr.hhplus.be.server.infra.coupon.entity.CouponEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class OrderCouponEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	private Long id;
	@ManyToOne
	private OrderEntity orderEntity;

	@ManyToOne
	private CouponEntity couponEntity;

	@CreatedDate
	private LocalDateTime appliedAt;

	public OrderCouponEntity(OrderEntity orderEntity, CouponEntity couponEntity) {
		this.orderEntity = orderEntity;
		this.couponEntity = couponEntity;
		this.appliedAt = LocalDateTime.now();
	}

}
