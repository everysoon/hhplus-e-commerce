package kr.hhplus.be.server.utils;

import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductStatus;
import kr.hhplus.be.server.infra.product.entity.Category;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ProductTestFixture {
	public static Product create(Long productId, Integer stock) {
		return new Product(
			productId,
			"TEST PRODUCT" + productId,
			stock,
			Category.BABY,
			"TEST desc" + productId,
			BigDecimal.valueOf(1000 * stock),
			ProductStatus.AVAILABLE,
			LocalDateTime.now()
		);
	}

	public static Product create(Long productId, Category category) {
		return new Product(
			productId,
			"TEST PRODUCT" + productId,
			10,
			category,
			"TEST desc" + productId,
			BigDecimal.valueOf(1000),
			ProductStatus.AVAILABLE,
			LocalDateTime.now()
		);
	}

	public static Product create(Long productId) {
		return new Product(
			productId,
			"TEST PRODUCT" + productId,
			10,
			Category.BABY,
			"TEST desc" + productId,
			BigDecimal.valueOf(1000),
			ProductStatus.AVAILABLE,
			LocalDateTime.now()
		);
	}
}
