package kr.hhplus.be.server.infra.payment.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import kr.hhplus.be.server.domain.payment.PaymentMethod;
import kr.hhplus.be.server.infra.order.entity.OrderEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

@Getter
@Entity
@Table(name = "payments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	private Long id;

	@OneToOne
	@JoinColumn(name = "order_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
	private OrderEntity order;

	@Column(nullable = false)
	private PaymentMethod paymentMethod;



	private BigDecimal price;

	@Column(nullable = false)
	private String transactionId;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private PaymentStatus status;

	@Column(nullable = false)
	@CreatedDate
	private LocalDateTime paidAt;
}
