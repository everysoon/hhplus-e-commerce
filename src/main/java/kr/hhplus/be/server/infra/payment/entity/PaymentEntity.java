package kr.hhplus.be.server.infra.payment.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.payment.PaymentMethod;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "payments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	private Long id;

	private Long orderId;

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

	public PaymentEntity(Long orderId, PaymentMethod method, BigDecimal price, String transactionId, PaymentStatus status) {
		this.orderId = orderId;
		this.paymentMethod = method;
		this.price = price;
		this.transactionId = transactionId;
		this.status = status;
		this.paidAt = LocalDateTime.now();
	}

	public Payment toDomain() {
		return new Payment(
			this.id,
			this.paymentMethod,
			this.paidAt,
			this.orderId,
			this.price,
			this.status,
			this.transactionId
		);
	}

	public static PaymentEntity from(Payment payment) {
		return new PaymentEntity(
			payment.getOrderId(),
			payment.getPaymentMethod(),
			payment.getPrice(),
			payment.getTransactionId(),
			payment.getStatus()
		);
	}
}
