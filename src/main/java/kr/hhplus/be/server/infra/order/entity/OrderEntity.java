package kr.hhplus.be.server.infra.order.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import kr.hhplus.be.server.infra.payment.entity.PaymentEntity;
import kr.hhplus.be.server.infra.user.entity.UserEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
	private BigDecimal totalPrice = BigDecimal.ZERO;

	@NotNull
	@Column(nullable = false)
	private BigDecimal totalDiscount = BigDecimal.ZERO;

	@CreatedDate
	@Column(nullable = false)
	private LocalDateTime createdAt;


}
