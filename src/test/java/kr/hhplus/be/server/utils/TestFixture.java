package kr.hhplus.be.server.utils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderItem;
import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductStatus;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.infra.product.entity.Category;

public class TestFixture {
	public static Order createOrder(Payment payment) {
		return new Order(
			null,
			1L,
			payment,
			null,
			toBigDecimal(1000),
			null,
			LocalDateTime.now()
		);
	}
	public static List<Product> createProducts() {
		List<Product> products = new ArrayList<Product>();
		products.add(createProduct(1L));
		products.add(createProduct(2L));
		products.add(createProduct(3L));
		products.add(createProduct(4L));
		return products;
	}

	public static List<OrderItem> productToOrderItems(List<Product> products, Order order) {
		return products.stream()
			.map(p -> OrderItem.create(p, 1))
			.toList();
	}

	public static User createUser(Long userId,BigDecimal point) {
		return new User(
			userId,
			point,
			"soonforjoy@gmail.com",
			"Guro, Seoul",
			"minsun",
			"alstjsl1!",
			LocalDateTime.now()
		);
	}
	public static Product createProduct(Long productId,Category category) {
		return new Product(
			productId,
			"테스트 상품 " + productId,
			10,
			category,
			"유아용 자동차 장난감" + productId,
			toBigDecimal(1000),
			ProductStatus.AVAILABLE,
			LocalDateTime.now()
		);
	}
	public static Product createProduct(Long productId) {
		return new Product(
			productId,
			"테스트 상품 " + productId,
			10,
			Category.BABY,
			"유아용 자동차 장난감" + productId,
			toBigDecimal(1000),
			ProductStatus.AVAILABLE,
			LocalDateTime.now()
		);
	}

	public static BigDecimal toBigDecimal(Integer value) {
		return new BigDecimal(value);
	}
}
