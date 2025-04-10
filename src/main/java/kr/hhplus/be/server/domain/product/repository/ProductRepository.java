package kr.hhplus.be.server.domain.product.repository;

import java.util.List;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.infra.product.entity.ProductEntity;

public interface ProductRepository {

	Product findById(Long productId);

	List<Product> findAll();

	List<Product> findPopularAll();
}
