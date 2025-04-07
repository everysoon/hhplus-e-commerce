package kr.hhplus.be.server.application.order.dto;

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
    private Long productId;
    private String productName;
    private BigDecimal price;
    private Integer quantity;
}
