package kr.hhplus.be.server.application.product;

import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductRepository;
import kr.hhplus.be.server.infra.cache.PopularProductRedisService;
import kr.hhplus.be.server.support.aop.lock.RedisLock;
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

	private final PopularProductRedisService popularProductRedisService;

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
		List<Long> topPopularProductIds = popularProductRedisService.getTopPopularProductIds(
			command.getStartDateOrDefault()
				.toLocalDate(), command.getEndDateOrDefault().toLocalDate(), 5);
		return productRepository.findByIdIn(topPopularProductIds);
	}

	@Transactional(readOnly = true)
	public List<Product> findPopularAllWithOutZSet(ProductCommand.TopSelling command) {
		List<Long> topPopularProductIds = popularProductRedisService.getTopNPopularProductIdsWithoutZSet(
			command.getStartDateOrDefault()
				.toLocalDate(), command.getEndDateOrDefault().toLocalDate(), 5);
		return productRepository.findByIdIn(topPopularProductIds);
	}

	@Transactional(propagation = MANDATORY)
	@RedisLock(lockKey = "#command.getLockKey()")
	public List<Product> increaseStock(ProductCommand.Refund command) {

		return command.orderItems().stream()
			.map(o -> {
				popularProductRedisService.decreaseScore(o.getProductId(),
					-o.getQuantity());
				return productRepository.increaseStock(o.getProductId(), o.getQuantity());
			})
			.toList();
	}

	@Transactional(propagation = MANDATORY)
	@RedisLock(lockKey = "'lock:product:' + #productId")
	public Product decreaseStock(Long productId, Integer quantity) {
		logger.info("### decreaseStock : {}, {}", productId, quantity);
		Product product = productRepository.decreaseStock(productId, quantity);
		popularProductRedisService.increaseScore(productId, quantity);
		return product;
	}
}
