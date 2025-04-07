package kr.hhplus.be.server.infra.order.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import kr.hhplus.be.server.infra.coupon.entity.CouponEntity;
import kr.hhplus.be.server.infra.payment.entity.PaymentEntity;
import kr.hhplus.be.server.infra.user.entity.UserEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

@Getter
@Entity
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	private Long id;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private UserEntity userEntity;

	@OneToOne(mappedBy = "orderEntity", cascade = CascadeType.ALL)
	private PaymentEntity paymentEntity;

	@OneToMany(mappedBy = "orderEntity", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<OrderCouponEntity> orderCouponEntities = new ArrayList<>();

	@Setter
	@Column(nullable = false)
	@Builder.Default
	private BigDecimal totalPrice = BigDecimal.ZERO;

	@NotNull
	@Column(nullable = false)
	@Builder.Default
	private BigDecimal totalDiscount = BigDecimal.ZERO;

	@CreatedDate
	@Column(nullable = false)
	private LocalDateTime createdAt;


}
