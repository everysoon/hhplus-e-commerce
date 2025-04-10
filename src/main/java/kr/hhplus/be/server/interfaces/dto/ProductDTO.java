package kr.hhplus.be.server.interfaces.dto;

import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductStatus;
import kr.hhplus.be.server.infra.product.entity.Category;

import java.math.BigDecimal;

public class ProductDTO {
	public static record ProductResponse(
		Long id,
		String productName,
		Category category,
		Integer stock,
		String description,
		BigDecimal price,
		ProductStatus status
	) {
		public static ProductResponse from(Product product){
			return new ProductResponse(
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
}
