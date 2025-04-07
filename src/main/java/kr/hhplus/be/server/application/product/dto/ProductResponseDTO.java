package kr.hhplus.be.server.application.product.dto;

import java.math.BigDecimal;
import kr.hhplus.be.server.domain.product.ProductStatus;
import kr.hhplus.be.server.infra.product.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
	private ProductStatus status;
}
