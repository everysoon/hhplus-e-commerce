package kr.hhplus.be.server.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.dto.coupon.CouponDTO;
import kr.hhplus.be.server.dto.user.UserCouponResponseDTO;
import kr.hhplus.be.server.enums.CouponType;
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
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Coupon {
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private UUID id;
    @Enumerated(EnumType.STRING)
    private CouponType type;
    private String description;

    private BigDecimal discount; // Fixed 일땐 할인 금액 (원), Percent 일땐 할인 비율 (1 ~ 100)

    private Integer stock;
    private LocalDateTime expiredAt;
    @CreatedDate
    private LocalDateTime createdAt;

    public BigDecimal calculateDiscount(BigDecimal originalPrice) {
        if (this.type == CouponType.FIXED) {
            return discount; // Fixed 금액을 그대로 반환
        } else if (this.type == CouponType.PERCENT) {
            return originalPrice.multiply(discount.divide(BigDecimal.valueOf(100))); // 백분율 계산
        }
        return BigDecimal.ZERO;
    }
    public CouponDTO toDTO(){
        return CouponDTO.builder()
                .id(id)
                .type(type)
                .stock(stock)
                .description(description)
                .expiredAt(expiredAt)
                .build();
    }
}
