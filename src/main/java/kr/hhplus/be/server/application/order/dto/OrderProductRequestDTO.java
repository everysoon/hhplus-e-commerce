package kr.hhplus.be.server.application.order.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderProductRequestDTO {
    @NotNull
    private Long productId;
    @NotNull
    private Integer quantity;
}
