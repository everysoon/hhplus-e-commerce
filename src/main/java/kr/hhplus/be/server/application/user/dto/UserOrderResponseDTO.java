package kr.hhplus.be.server.application.user.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserOrderResponseDTO {
    private Long productId;
    private String productName;
    private BigDecimal price;
    private Integer quantity;
}
