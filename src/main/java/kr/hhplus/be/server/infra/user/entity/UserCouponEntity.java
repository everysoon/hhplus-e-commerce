package kr.hhplus.be.server.infra.user.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.coupon.CouponStatus;
import kr.hhplus.be.server.infra.coupon.entity.CouponEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "user_coupon")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserCouponEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CouponStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    private CouponEntity couponEntity;

    @Column(nullable = false)
    @CreatedDate
    private LocalDateTime issuedAt;


}
