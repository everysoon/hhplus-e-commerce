package kr.hhplus.be.server.infra.product.entity;

import jakarta.persistence.*;
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
	/**
	 * stock이 0인 경우 status를 out_of_stock로 자동으로 설정되도록 로직을 추가하거나, 상태를 관리할 때 재고를 기반으로 상태를 관리하는 로직을
	 * 추가하자
	 */
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ProductStatus status;
	@CreatedDate
	@Column(nullable = false)
	private LocalDateTime createdAt;
}
