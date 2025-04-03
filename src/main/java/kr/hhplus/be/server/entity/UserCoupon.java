package kr.hhplus.be.server.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import kr.hhplus.be.server.dto.user.UserCouponResponseDTO;
import kr.hhplus.be.server.enums.CouponStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

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
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CouponStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    @Column(nullable = false)
    private Integer remainingStock; // 쿠폰이 사용되거나 만료되면 갱신

    @Column(nullable = false)
    @CreatedDate
    private LocalDateTime issuedAt;

    public UserCouponResponseDTO toResponse(LocalDateTime expired){
        return UserCouponResponseDTO.builder()
                .couponId(coupon.getId())
                .userId(user.getId())
                .couponType(coupon.getType())
				.expiredAt(expired)
                .description(coupon.getDescription())
                .remainingStock(remainingStock)
                .couponStatus(status)
                .issuedAt(issuedAt)
                .build();
    }
	public boolean isValid(){
		return EnumSet.of(CouponStatus.PENDING, CouponStatus.ISSUED).contains(this.status);
	}
}
