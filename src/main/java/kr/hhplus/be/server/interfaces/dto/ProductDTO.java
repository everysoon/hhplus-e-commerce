package kr.hhplus.be.server.interfaces.dto;

import java.math.BigDecimal;
import kr.hhplus.be.server.application.product.ProductSearchCommand;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductStatus;
import kr.hhplus.be.server.infra.product.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class ProductDTO {
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public class SearchRequest{
		private String name;
		private String category;
		private String sortBy;
		private String sorted;
		private boolean soldOut;
		public ProductSearchCommand toCommand(){
			return ProductSearchCommand.of(name, category, sortBy, sorted, soldOut);
		}
	}
	public record OrderItemDetailResponse(
		Long productId,
		String productName,
		Category category,
		String prodDescription,
		BigDecimal unitPrice
	){
		public static OrderItemDetailResponse of(Product product){
			return new OrderItemDetailResponse(
				product.getId(),
				product.getProductName(),
				product.getCategory(),
				product.getDescription(),
				product.getPrice()
			);
		}
	}
	public record ProductResponse(
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
