package kr.hhplus.be.server.dto.order;

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
