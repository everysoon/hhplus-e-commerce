package kr.hhplus.be.server.infra.coupon.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.coupon.CouponType;
import kr.hhplus.be.server.infra.order.entity.OrderEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

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
    @ManyToOne
    @JoinColumn(name = "order_id")
    private OrderEntity orderEntity;
    @Column(nullable = false)
    private BigDecimal discount = BigDecimal.ZERO; // Fixed 일땐 할인 금액 (원), Percent 일땐 할인 비율 (1 ~ 100)

    @Column(nullable = false)
    private Integer stock;

    @Column(nullable = false)
    private LocalDateTime expiredAt;
    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;
}
