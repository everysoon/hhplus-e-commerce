package kr.hhplus.be.server.domain.product;

import kr.hhplus.be.server.application.product.ProductCommand;

import java.util.List;

public interface ProductRepository {

	Product findById(Long productId);

	List<Product> findByIdIn(List<Long> productIds);

	Product decreaseStock(Long productId, Integer quantity);

	Product increaseStock(Long productId, Integer quantity);

	List<Product> searchFilter(ProductCommand.FilterSearch command);

	List<Product> findPopularAll(ProductCommand.TopSelling command);

	Product save(Product product);

	List<Product> findAll();
}
