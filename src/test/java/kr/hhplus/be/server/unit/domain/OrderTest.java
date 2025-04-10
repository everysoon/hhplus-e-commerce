package kr.hhplus.be.server.unit.domain;

import static kr.hhplus.be.server.utils.TestFixture.toBigDecimal;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderItem;
import kr.hhplus.be.server.domain.product.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

@DisplayName("Order Domain Test")
public class OrderTest {

	@Test
	public void 쿠폰_적용_시_할인_금액이_계산되어야_한다() {
		// given
		Order order = new Order(
			null,
			1L,
			null,
			null,
			toBigDecimal(10000),
			BigDecimal.ZERO,
			LocalDateTime.now()
		);
		Coupon coupon = Mockito.mock(Coupon.class);
		when(coupon.getDiscountAmount(any()))
			.thenReturn(toBigDecimal(3000));
		// when
		order.applyCoupon(coupon);
		// then
		assertEquals(toBigDecimal(3000), order.getTotalDiscount());
		assertEquals(toBigDecimal(7000), order.getTotalPrice());
	}

	@Test
	public void 주문_상품에_대해_가격_총_합이_맞아야한다() {
		// given
		Order order = new Order(
			null,
			1L,
			null,
			null,
			toBigDecimal(10000),
			BigDecimal.ZERO,
			LocalDateTime.now()
		);
		OrderItem orderItem = Mockito.mock(OrderItem.class);
		Product product = Mockito.mock(Product.class);
		when(product.getPrice()).thenReturn(toBigDecimal(1000));
		when(orderItem.getProduct()).thenReturn(product);
		List<OrderItem> orderItems = new ArrayList<>();
		orderItems.add(orderItem);
		// when
		order.calculateTotalPrice(orderItems);
		// then
		assertEquals(toBigDecimal(1000), order.getTotalPrice());

	}

}
