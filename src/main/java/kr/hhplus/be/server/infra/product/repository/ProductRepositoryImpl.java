package kr.hhplus.be.server.infra.product.repository;

import kr.hhplus.be.server.application.product.ProductSearchCommand;
import kr.hhplus.be.server.application.product.ProductTopSellingCommand;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.repository.ProductRepository;
import kr.hhplus.be.server.infra.product.entity.ProductEntity;
import kr.hhplus.be.server.support.common.exception.CustomException;
import kr.hhplus.be.server.support.config.swagger.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static kr.hhplus.be.server.infra.product.repository.ProductSpecification.*;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {
	private final ProductJpaRepository productJpaRepository;

	@Override
	public Product findById(Long productId) {
		return productJpaRepository.findById(productId)
			.orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST_PRODUCT))
			.toDomain();
	}

	@Override
	public List<Product> searchFilter(ProductSearchCommand command) {
		Specification<ProductEntity> spec = Specification
			.where(nameContains(command.name()))
			.and(filterCategory(command.category()))
			.and(filterSoldOut(command.soldOut()));
		Sort sort = ProductSpecification.getSort(command.sortBy(), command.sorted());
		return productJpaRepository.findAll(spec, sort).stream().map(ProductEntity::toDomain).toList();
	}

	@Override
	public List<Product> findPopularAll(ProductTopSellingCommand command) {
		LocalDateTime endDate = command.getEndDateOrDefault();
		LocalDateTime startDate = command.getStartDateOrDefault();
		return productJpaRepository.findPopularAll(startDate, endDate, command.pageable()).stream()
			.map(ProductEntity::toDomain).toList();
	}

	@Override
	public Product save(Product product) {
		return productJpaRepository.save(ProductEntity.from(product)).toDomain();
	}
}
