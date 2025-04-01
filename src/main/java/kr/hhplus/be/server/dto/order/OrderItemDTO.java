package kr.hhplus.be.server.dto.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItemDTO{
    private Long orderId;
    private Long productId;
    private String productName;
    private BigDecimal price;
    private Integer quantity;

}