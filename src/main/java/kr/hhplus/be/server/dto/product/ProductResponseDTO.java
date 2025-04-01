package kr.hhplus.be.server.dto.product;

import kr.hhplus.be.server.enums.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponseDTO {
    private Long id;
    private String productName;
    private Integer stock;
    private Category category;
    private String description;
    private BigDecimal price;
}
