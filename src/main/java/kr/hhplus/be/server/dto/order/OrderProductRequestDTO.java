package kr.hhplus.be.server.dto.order;

import lombok.Data;

@Data
public class OrderProductRequestDTO {
    private Long productId;
    private Integer quantity;
}
