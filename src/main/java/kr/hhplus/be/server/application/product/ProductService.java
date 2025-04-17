package kr.hhplus.be.server.application.product;

import java.util.List;
import kr.hhplus.be.server.domain.order.OrderItem;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

	public List<Product> decreaseStock(List<OrderItem> orderItems) {
		return orderItems.stream().map(o -> {
			Product product = findById(o.getProductId());
			product.decreaseStock(o.getQuantity());
			return productRepository.save(product);
		}).toList();

	}
}
