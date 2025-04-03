package kr.hhplus.be.server.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.enums.CouponType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Entity
@Table(name = "coupons")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Coupon {
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
    private Order order;
    @Column(nullable = false)
	@Builder.Default
    private BigDecimal discount = BigDecimal.ZERO; // Fixed 일땐 할인 금액 (원), Percent 일땐 할인 비율 (1 ~ 100)

    @Column(nullable = false)
    private Integer stock;

    @Column(nullable = false)
    private LocalDateTime expiredAt;
    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;

    public void calculateDiscount(BigDecimal originalPrice) {
        if (this.type == CouponType.FIXED) {
            this.order.addDiscount(discount);
        } else if (this.type == CouponType.PERCENT) {
            this.order.addDiscount(originalPrice.multiply(discount.divide(BigDecimal.valueOf(100))));
        }
    }
}
