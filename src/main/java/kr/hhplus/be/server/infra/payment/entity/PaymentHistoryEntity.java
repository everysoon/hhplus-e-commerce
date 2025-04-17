package kr.hhplus.be.server.infra.payment.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.payment.PaymentHistory;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "payment_history")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentHistoryEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "payment_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
	private PaymentEntity payment;

	private Long orderId;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private PaymentStatus status;
	@CreatedDate
	@Column(nullable = false)
	private LocalDateTime createdAt;
	@NotNull
	private BigDecimal price = BigDecimal.ZERO; // 환불액, 또는 결제액
	private String description; // 실패,환불 사유 명
	private String transactionId;

	public PaymentHistoryEntity(Payment payment, Long orderId, PaymentStatus status, String description, String transactionId) {
		this.payment = PaymentEntity.from(payment);
		this.orderId = orderId;
		this.status = status;
		this.createdAt = LocalDateTime.now();
		this.description = description;
		this.transactionId = transactionId;
	}

	public static PaymentHistoryEntity from(PaymentHistory history) {
		return new PaymentHistoryEntity(
			history.getPayment(),
			history.getOrderId(),
			history.getStatus(),
			history.getDescription(),
			history.getTransactionId()
		);
	}

	public PaymentHistory toDomain() {
		return new PaymentHistory(
			this.id,
			this.orderId,
			this.payment.toDomain(),
			this.status,
			this.price,
			this.description,
			this.createdAt,
			this.payment.getTransactionId()
		);
	}
}
