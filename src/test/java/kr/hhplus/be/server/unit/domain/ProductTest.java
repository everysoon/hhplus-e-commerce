package kr.hhplus.be.server.unit.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.utils.TestFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Product Domain Test")
public class ProductTest {

	@Test
	public void 상품_재고_감소() {
		// given
		Product product = TestFixture.createProduct(1L);
		// when
		product.decreaseStock(9);
		// then
		assertEquals(1, product.getStock());
	}
}
