package kr.hhplus.be.server.dto.user;

import kr.hhplus.be.server.dto.coupon.CouponDTO;
import kr.hhplus.be.server.entity.Coupon;
import kr.hhplus.be.server.entity.User;
import kr.hhplus.be.server.entity.UserCoupon;
import kr.hhplus.be.server.enums.CouponStatus;
import kr.hhplus.be.server.enums.CouponType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserCouponResponseDTO {
    private Long userId;
    private UUID couponId;
    private CouponType couponType;
    private String description;
    private Integer remainingStock;
    private CouponStatus couponStatus;
    private LocalDateTime issuedAt;

    public UserCouponResponseDTO(CouponDTO coupon, UserCoupon uc){
        this.userId = uc.getUser().getId();
        this.couponId = coupon.getId();
        this.couponType = coupon.getType();
        this.description = coupon.getDescription();
        this.remainingStock = coupon.getStock();
        this.couponStatus = uc.getStatus();
        this.issuedAt = uc.getIssuedAt();
    }
}
