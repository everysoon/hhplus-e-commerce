package kr.hhplus.be.server.unit.service;

import static kr.hhplus.be.server.utils.TestFixture.createProduct;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import kr.hhplus.be.server.application.product.ProductService;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

	@Mock
	private ProductRepository productRepository;

	@InjectMocks
	private ProductService productService;

	@Test
	void 상품_ID로_조회_정상작동() {
		// given
		Long productId = 1L;
		Product product = createProduct(productId);
		when(productRepository.findById(productId)).thenReturn(product);

		// when
		Product result = productService.findById(productId);

		// then
		assertNotNull(result);
		assertEquals(product, result);
		verify(productRepository).findById(productId);
	}
	@Test
	void 인기상품_조회_정상작동() {
		// given
		List<Product> popularProducts = List.of(
			createProduct(1L),
			createProduct(2L)
		);

		when(productRepository.findPopularAll()).thenReturn(popularProducts);

		// when
		List<Product> result = productService.findPopularAll();

		// then
		assertNotNull(result);
		assertEquals(2, result.size());
		verify(productRepository).findPopularAll();
	}

	@Test
	void 상품_재고_차감_정상작동() {
		// given
		Long productId = 1L;
		Integer amount = 2;
		Product product = spy(createProduct(productId));
		when(productRepository.findById(productId)).thenReturn(product);
		when(productRepository.save(product)).thenReturn(product);

		// when
		Product result = productService.decrease(productId, amount);

		// then
		verify(product).decreaseStock(amount);
		verify(productRepository).save(product);
		assertEquals(product, result);
		assertEquals(8, result.getStock());
	}
}
