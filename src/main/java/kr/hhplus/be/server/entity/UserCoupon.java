package kr.hhplus.be.server.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.enums.CouponStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "user_coupon")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCoupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    private User user;

    @Enumerated(EnumType.STRING)
    private CouponStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    private Coupon coupon;

    private Integer remainingStock; // 쿠폰이 사용되거나 만료되면 갱신

    private LocalDateTime issuedAt;

}
