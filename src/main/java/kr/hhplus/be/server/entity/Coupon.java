package kr.hhplus.be.server.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.enums.CouponType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private UUID id;
    private String code;
    @Enumerated(EnumType.STRING)
    private CouponType type;
    private String description;
    private Integer stock;
    private LocalDateTime expiredAt;
    @CreatedDate
    private LocalDateTime createdAt;
}
