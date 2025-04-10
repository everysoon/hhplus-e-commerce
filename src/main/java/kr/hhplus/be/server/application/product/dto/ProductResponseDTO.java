package kr.hhplus.be.server.application.product.dto;

import java.math.BigDecimal;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductStatus;
import kr.hhplus.be.server.infra.product.entity.Category;


public record ProductResponseDTO(
	Long id,
	String productName,
	Category category,
	Integer stock,
	String description,
	BigDecimal price,
	ProductStatus status
) {
	public static ProductResponseDTO from(Product product){
		return new ProductResponseDTO(
			product.getId(),
			product.getProductName(),
			product.getCategory(),
			product.getStock(),
			product.getDescription(),
			product.getPrice(),
			product.getStatus()
		);
	}
}
