package kr.hhplus.be.server.domain.product.repository;

import kr.hhplus.be.server.application.product.ProductSearchCommand;
import kr.hhplus.be.server.domain.product.Product;

import java.util.List;

public interface ProductRepository {

	Product findById(Long productId);

	List<Product> findAll(ProductSearchCommand command);

	List<Product> findPopularAll();
}
