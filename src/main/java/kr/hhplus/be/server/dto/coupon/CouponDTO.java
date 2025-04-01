package kr.hhplus.be.server.dto.coupon;

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
public class CouponDTO {
    private UUID id;
    private CouponType type;
    private String description;
    private Integer stock;
    private LocalDateTime expiredAt;
}
