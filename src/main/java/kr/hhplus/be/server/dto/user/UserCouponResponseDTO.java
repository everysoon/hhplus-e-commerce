package kr.hhplus.be.server.dto.user;

import com.fasterxml.jackson.annotation.JsonFormat;
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
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime issuedAt;
}
