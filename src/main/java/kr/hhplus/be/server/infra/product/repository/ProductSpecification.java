package kr.hhplus.be.server.infra.product.repository;

import kr.hhplus.be.server.infra.product.entity.ProductEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecification {
	public static Specification<ProductEntity> nameContains(String name) {
		return ((root, query, criteriaBuilder) -> {
			if (!isValidString(name)) return null;
			return criteriaBuilder.like(root.get("name").as(String.class), "%" + name + "%");
		});
	}
	public static Specification<ProductEntity> filterCategory(String category) {
		return ((root, query, criteriaBuilder) -> {
			if(!isValidString(category)) return null;
			return criteriaBuilder.equal(root.get("category").as(String.class), category);
		});
	}
	public static Specification<ProductEntity> filterSoldOut(boolean soldOut) {
		return ((root, query, criteriaBuilder) -> {
			if(soldOut) return criteriaBuilder.equal(root.get("stock"), 0);
			else return criteriaBuilder.greaterThan(root.get("stock"), 0);
		});
	}

	public static Sort getSort(String sortBy, String sorted){
		if(!isValidString(sortBy) || !isValidString(sorted)){
			return null;
		}
		Sort.Direction direction = "DESC".equalsIgnoreCase(sorted) ? Sort.Direction.DESC : Sort.Direction.ASC;
		return switch (sortBy){
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
