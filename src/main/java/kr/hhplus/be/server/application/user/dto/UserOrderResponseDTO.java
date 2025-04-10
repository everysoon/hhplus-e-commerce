package kr.hhplus.be.server.application.user.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserOrderResponseDTO {
	private Long userId;
	private List<OrderItemDTO> orderItems;
}
