package kr.hhplus.be.server.application.product;

import kr.hhplus.be.server.domain.order.OrderItem;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
	private final Logger logger = LoggerFactory.getLogger(ProductService.class);
	private final ProductRepository productRepository;

	public Product findById(Long productId) {
		return productRepository.findById(productId);
	}

	public List<Product> orderItemToProduct(List<OrderItem> orderItems) {
		return orderItems.stream().map(OrderItem::getProductId).map(productRepository::findById)
			.toList();
	}

	public List<Product> searchFilter(ProductSearchCommand command) {
		return productRepository.searchFilter(command);
	}

	public List<Product> findPopularAll(ProductTopSellingCommand command) {
		return productRepository.findPopularAll(command);
	}

	public List<Product> increaseStock(List<OrderItem> orderItems) {
		return orderItems.stream().map(o -> {
			Product product = findById(o.getProductId());
			product.increaseStock(o.getQuantity());
			return productRepository.save(product);
		}).toList();
	}

	public List<Product> decreaseStock(List<OrderItem> orderItems) {
		logger.info("### decreaseStock :{}", orderItems);
		return orderItems.stream().map(o -> {
			Product product = findById(o.getProductId());
			product.decreaseStock(o.getQuantity());
			return productRepository.save(product);
		}).toList();
	}
}
