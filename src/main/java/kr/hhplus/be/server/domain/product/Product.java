package kr.hhplus.be.server.domain.product;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import kr.hhplus.be.server.application.product.dto.ProductResponseDTO;
import kr.hhplus.be.server.infra.product.entity.Category;
import kr.hhplus.be.server.infra.product.entity.ProductEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Product {
	private final Long id;
	private String productName;
	private Integer stock;
	private final Category category;
	private String description;
	private BigDecimal price;
	private ProductStatus status;
	private LocalDateTime createdAt;
	public static Product from(ProductEntity entity) {
		return new Product(
			entity.getId(),
			entity.getProductName(),
			entity.getStock(),
			entity.getCategory(),
			entity.getDescription(),
			entity.getPrice(),
			entity.getStatus(),
			entity.getCreatedAt()
		);
	}
}
