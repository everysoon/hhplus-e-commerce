package kr.hhplus.be.server.domain.product.repository;

import kr.hhplus.be.server.application.product.ProductSearchCommand;
import kr.hhplus.be.server.application.product.ProductTopSellingCommand;
import kr.hhplus.be.server.domain.product.Product;

import java.util.List;

public interface ProductRepository {

	Product findById(Long productId);

	List<Product> searchFilter(ProductSearchCommand command);

	List<Product> findPopularAll(ProductTopSellingCommand command);

	Product save(Product product);
	List<Product> findAll();
}
