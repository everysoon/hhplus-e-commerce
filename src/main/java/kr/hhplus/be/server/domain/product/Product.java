package kr.hhplus.be.server.domain.product;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import kr.hhplus.be.server.application.product.dto.ProductResponseDTO;
import kr.hhplus.be.server.infra.product.entity.Category;
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
	public ProductResponseDTO toResponseDTO(){
		return ProductResponseDTO.builder()
			.id(id)
			.productName(productName)
			.stock(stock)
			.description(description)
			.price(price)
			.status(status)
			.category(category)
			.build();
	}
}
