package kr.hhplus.be.server.domain.payment;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "payments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment {

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
	@Builder
    public Payment(Long orderId, BigDecimal price, PaymentMethod paymentMethod,String transactionId) {
        this.orderId = orderId;
        this.paymentMethod = paymentMethod;
		this.transactionId = transactionId;
        this.price = price;
        this.status = PaymentStatus.COMPLETED;
        this.paidAt = LocalDateTime.now();
    }

    public void cancel(BigDecimal price, String transactionId) {
        this.price = price;
        this.transactionId = transactionId;
        this.status = PaymentStatus.CANCELED;
    }
}
