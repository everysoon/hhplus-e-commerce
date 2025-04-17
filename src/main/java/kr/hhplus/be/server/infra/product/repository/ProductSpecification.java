package kr.hhplus.be.server.infra.product.repository;

import static kr.hhplus.be.server.support.config.swagger.ErrorCode.INVALID_CATEGORY;
import static kr.hhplus.be.server.support.config.swagger.ErrorCode.INVALID_SORTED;
import static kr.hhplus.be.server.support.config.swagger.ErrorCode.INVALID_SORTED_BY;

import java.util.List;
import kr.hhplus.be.server.infra.product.entity.Category;
import kr.hhplus.be.server.infra.product.entity.ProductEntity;
import kr.hhplus.be.server.support.common.exception.CustomException;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecification {

	public static Specification<ProductEntity> nameContains(String name) {
		return ((root, query, criteriaBuilder) -> {
			if (!isValidString(name)) {
				return null;
			}
			return criteriaBuilder.like(root.get("productName").as(String.class), "%" + name + "%");
		});
	}

	public static Specification<ProductEntity> filterCategory(String category) {
		return ((root, query, criteriaBuilder) -> {
			if (!isValidString(category)) {
				return null;
			}
			if(Category.valueOf(category.toUpperCase()) == null){
				throw new CustomException(INVALID_CATEGORY);
			}
			return criteriaBuilder.equal(root.get("category").as(String.class), category);
		});
	}

	public static Specification<ProductEntity> filterSoldOut(boolean soldOut) {
		return ((root, query, criteriaBuilder) -> {
			if (soldOut) {
				return criteriaBuilder.equal(root.get("stock"), 0);
			} else {
				return criteriaBuilder.greaterThan(root.get("stock"), 0);
			}
		});
	}

	public static Sort getSort(String sortBy, String sorted) {
		if (isValidString(sorted) && !List.of("DESC", "ASC").contains(sorted)) {
			throw new CustomException(INVALID_SORTED);
		}
		Sort.Direction direction =
			"ASC".equalsIgnoreCase(sorted) ? Sort.Direction.ASC : Sort.Direction.DESC;
		if (isValidString(sortBy) && !List.of("CATEGORY", "PRICE", "LATEST").contains(sortBy)) {
			throw new CustomException(INVALID_SORTED_BY);
		}
		if(sortBy == null) {
			return Sort.by(Sort.Direction.DESC, "createdAt");
		}
		return switch (sortBy) {
			case "CATEGORY" -> Sort.by(direction, "category");
			case "PRICE" -> Sort.by(direction, "price");
			case "LATEST" -> Sort.by(direction, "createdAt");
			default -> Sort.by(Sort.Direction.DESC, "createdAt"); // 기본값: 최신순
		};
	}

	public static boolean isValidString(String str) {
		return str != null && !str.isEmpty();
	}
}
