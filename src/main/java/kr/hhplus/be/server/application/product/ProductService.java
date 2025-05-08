package kr.hhplus.be.server.application.product;

import static org.springframework.transaction.annotation.Propagation.MANDATORY;

import java.util.List;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.repository.ProductRepository;
import kr.hhplus.be.server.infra.lock.RedisLock;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	@RedisLock(lockKey = "#command.getLockKey()")
	public List<Product> increaseStock(ProductCommand.Refund command) {
		return command.orderItems().stream()
			.map(o -> productRepository.increaseStock(o.getProduct(), o.getQuantity()))
			.toList();
	}

	@Transactional(propagation = MANDATORY)
	@RedisLock(lockKey = "'lock:product:' + #productId")
	public Product decreaseStock(Long productId, Integer quantity) {
		logger.info("### decreaseStock : {}, {}", productId, quantity);
		Product product = productRepository.decreaseStock(productId, quantity);
		return product;
	}
}
