package kr.hhplus.be.server.application.user.dto;

import java.util.List;
import kr.hhplus.be.server.application.coupon.dto.CouponResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserCouponResponseDTO {

	private Long userId;
	private List<CouponResponseDTO> conpons;
}
