package kr.hhplus.be.server.application.product;

import kr.hhplus.be.server.domain.order.OrderItem;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.transaction.annotation.Propagation.MANDATORY;

@Service
@RequiredArgsConstructor
public class ProductService {
	private final Logger logger = LoggerFactory.getLogger(ProductService.class);
	private final ProductRepository productRepository;

	@Transactional(readOnly = true)
	public Product findById(Long productId) {
		return productRepository.findById(productId);
	}

	@Transactional(readOnly = true)
	public List<Product> searchFilter(ProductCommand.FilterSearch command) {
		return productRepository.searchFilter(command);
	}

	@Transactional(readOnly = true)
	public List<Product> findPopularAll(ProductCommand.TopSelling command) {
		return productRepository.findPopularAll(command);
	}

	@Transactional(propagation = MANDATORY)
	public List<Product> increaseStock(List<OrderItem> orderItems) {
		return orderItems.stream()
			.map(o -> productRepository.increaseStock(o.getProduct(), o.getQuantity()))
			.toList();
	}

	@Transactional(propagation = MANDATORY)
	public Product decreaseStock(Long productId, Integer quantity) {
		logger.info("### decreaseStock : {}, {}", productId, quantity);
		Product product = productRepository.decreaseStock(productId, quantity);
		return product;
	}
}
