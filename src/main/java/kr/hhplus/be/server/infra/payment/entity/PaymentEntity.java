package kr.hhplus.be.server.infra.payment.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.payment.PaymentMethod;
import kr.hhplus.be.server.infra.order.entity.OrderEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

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
    @Column(nullable = false)
    private PaymentMethod  paymentMethod;
    @Column(nullable = false)
    @CreatedDate
    private LocalDateTime createdAt;
    @OneToOne
    @JoinColumn(name = "order_id")
    private OrderEntity orderEntity;
}
