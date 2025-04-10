package kr.hhplus.be.server.infra.product.repository;

import kr.hhplus.be.server.application.product.ProductSearchCommand;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.repository.ProductRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductRepositoryImpl implements ProductRepository {

	@Override
	public Product findById(Long productId) {
		return null;
	}

	@Override
	public List<Product> findAll(ProductSearchCommand command) {
		return List.of();
	}


	@Override
	public List<Product> findPopularAll() {
		return List.of();
	}
}
