package kr.hhplus.be.server.infra.payment.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import kr.hhplus.be.server.domain.payment.PaymentStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
    @JoinColumn(name = "payment_id")
    private PaymentEntity paymentEntity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;
    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;
    @NotNull
    private BigDecimal amount = BigDecimal.ZERO; // 환불액, 또는 결제액
    private String description; // 실패,환불 사유 명

}
