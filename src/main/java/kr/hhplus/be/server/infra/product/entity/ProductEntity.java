package kr.hhplus.be.server.infra.product.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "products")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	private Long id;

	private String productName;

	@Column(nullable = false)
	private Integer stock;

	@Column(nullable = false)
	private Category category;

	private String description;

	@Column(nullable = false)
	private BigDecimal price = BigDecimal.ZERO;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ProductStatus status;

	@CreatedDate
	@Column(nullable = false)
	private LocalDateTime createdAt;
	public ProductEntity(String productName, Integer stock, Category category, String description, BigDecimal price,ProductStatus status) {
		this.productName = productName;
		this.stock = stock;
		this.category = category;
		this.description = description;
		this.price = price;
		this.status = status;
		this.createdAt = LocalDateTime.now();
	}
	public static ProductEntity from (Product product){
		return new ProductEntity(
			product.getProductName(),
			product.getStock(),
			product.getCategory(),
			product.getDescription(),
			product.getPrice(),
			product.getStatus()
		);
	}
	public Product toDomain(){
		return new Product(
			this.id,
			this.productName,
			this.stock,
			this.category,
			this.description,
			this.price,
			this.status,
			this.createdAt
		);
	}
}
