package kr.hhplus.be.server.interfaces.dto;

import kr.hhplus.be.server.application.product.ProductCommand;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductStatus;
import kr.hhplus.be.server.infra.product.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

public class ProductDTO {
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class SearchRequest{
		private String name;
		private String category;
		private String sortBy;
		private String sorted;
		private boolean soldOut;
		public ProductCommand.FilterSearch toCommand(){
			return ProductCommand.FilterSearch.of(name, category, sortBy, sorted, soldOut);
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
		Integer price,
		ProductStatus status
	) {
		public static ProductResponse from(Product product){
			return new ProductResponse(
				product.getId(),
				product.getProductName(),
				product.getCategory(),
				product.getStock(),
				product.getDescription(),
				product.getPrice().intValue(),
				product.getStatus()
			);
		}
	}
}
