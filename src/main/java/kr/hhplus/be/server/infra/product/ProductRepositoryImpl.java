package kr.hhplus.be.server.infra.product;

import kr.hhplus.be.server.application.product.ProductCommand;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductRepository;
import kr.hhplus.be.server.support.common.exception.CustomException;
import kr.hhplus.be.server.support.config.swagger.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static kr.hhplus.be.server.infra.product.ProductSpecification.*;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {
	private final ProductJpaRepository productJpaRepository;

	@Override
	public Product findById(Long productId) {
		return productJpaRepository.findById(productId)
			.orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST_PRODUCT));
	}

	@Override
	public List<Product> findByIdIn(List<Long> productIds) {
		return productJpaRepository.findByIdIn(productIds).stream().toList();
	}

	@Override
	public Product decreaseStock(Long productId, Integer quantity) {
		Product product = productJpaRepository.findById(productId)
			.orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST_PRODUCT));
		product.decreaseStock(quantity);
		return product;
	}

	@Override
	public Product increaseStock(Long productId, Integer quantity) {
		Product product = productJpaRepository.findById(productId).orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST_PRODUCT));
		product.increaseStock(quantity);
		return product;
	}

	@Override
	public List<Product> searchFilter(ProductCommand.FilterSearch command) {
		Specification<Product> spec = Specification
			.where(nameContains(command.name()))
			.and(filterCategory(command.category()))
			.and(filterSoldOut(command.soldOut()));
		Sort sort = ProductSpecification.getSort(command.sortBy(), command.sorted());
		return productJpaRepository.findAll(spec, sort).stream().toList();
	}

	@Override
	public List<Product> findPopularAll(ProductCommand.TopSelling command) {
		LocalDateTime endDate = command.getEndDateOrDefault();
		LocalDateTime startDate = command.getStartDateOrDefault();
		return productJpaRepository.findPopularAll(startDate, endDate, command.pageable()).stream()
			.toList();
	}

	@Override
	public Product save(Product product) {
		return productJpaRepository.save(product);
	}

	@Override
	public List<Product> findAll() {
		return productJpaRepository.findAll().stream().toList();
	}
}
