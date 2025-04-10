package kr.hhplus.be.server.application.order.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderProductRequestDTO {
	@NotNull
	Long productId;
	@NotNull
	Integer quantity;
}
