package kr.hhplus.be.server.infra.product.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductStatus;
import kr.hhplus.be.server.support.common.exception.CustomException;
import kr.hhplus.be.server.support.config.swagger.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;

@Getter
@Entity
@ToString
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

	@Column(name = "category", nullable = false)
	@Enumerated(EnumType.STRING)
	private Category category;

	private String description;

	@Column(nullable = false)
	private BigDecimal price;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ProductStatus status;

	@CreatedDate
	@Column(nullable = false)
	private LocalDateTime createdAt;

	public ProductEntity(String productName, Integer stock, Category category, String description, BigDecimal price, ProductStatus status) {
		this.productName = productName;
		this.stock = stock;
		this.category = category;
		this.description = description;
		this.price = price;
		this.status = status;
		this.createdAt = LocalDateTime.now();
	}

	public static ProductEntity from(Product product) {
		return new ProductEntity(
			product.getProductName(),
			product.getStock(),
			product.getCategory(),
			product.getDescription(),
			product.getPrice(),
			product.getStatus()
		);
	}

	public Product toDomain() {
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

	public void decreaseStock(Integer amount) {
		if (this.stock < amount) {
			throw new CustomException(ErrorCode.OUT_OF_STOCK);
		}
		this.stock -= amount;
		if (this.stock == 0) {
			this.status = ProductStatus.OUT_OF_STOCK;
		}
	}

	public void increaseStock(Integer amount) {
		this.stock += amount;
	}
}
