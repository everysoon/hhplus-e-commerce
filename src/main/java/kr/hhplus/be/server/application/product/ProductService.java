package kr.hhplus.be.server.application.product;

import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
	private final ProductRepository productRepository;

	public Product findById(Long productId) {
		return productRepository.findById(productId);
	}

	public List<Product> findAll(ProductSearchCommand command) {
		return productRepository.findAll(command);
	}

	public List<Product> findPopularAll() {
		return productRepository.findPopularAll();
	}

	public Product decrease(Long productId,Integer amount) {
		Product product = findById(productId);
		product.decreaseStock(amount);
		return productRepository.save(product);
	}
}
