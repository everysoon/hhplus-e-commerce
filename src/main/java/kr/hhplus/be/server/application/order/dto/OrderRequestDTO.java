package kr.hhplus.be.server.application.order.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequestDTO {

	@NotNull
	Long userId;
	@NotNull
	List<OrderProductRequestDTO> products;
	List<UUID> couponId;
}
