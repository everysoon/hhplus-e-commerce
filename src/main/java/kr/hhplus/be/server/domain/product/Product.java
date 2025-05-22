package kr.hhplus.be.server.domain.product;

import jakarta.persistence.*;
import kr.hhplus.be.server.support.common.exception.CustomException;
import kr.hhplus.be.server.support.config.swagger.ErrorCode;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Entity
@ToString
@Table(name = "products")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Product {

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

	public Product decreaseStock(Integer amount) {
		if (this.stock < amount) {
			throw new CustomException(ErrorCode.OUT_OF_STOCK);
		}
		this.stock -= amount;
		if (this.stock == 0) {
			this.status = ProductStatus.OUT_OF_STOCK;
		}
		return this;
	}

	public Product increaseStock(Integer amount) {
		this.stock += amount;
		return this;
	}
}
