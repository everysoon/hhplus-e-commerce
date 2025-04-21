package kr.hhplus.be.server.domain.product.repository;

import kr.hhplus.be.server.application.product.ProductCommand;
import kr.hhplus.be.server.domain.product.Product;

import java.util.List;

public interface ProductRepository {

	Product findById(Long productId);

	List<Product> searchFilter(ProductCommand.FilterSearch command);

	List<Product> findPopularAll(ProductCommand.TopSelling command);

	Product save(Product product);
	List<Product> findAll();
}
