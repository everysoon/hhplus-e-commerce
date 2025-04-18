package kr.hhplus.be.server.application.order;

import java.util.List;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.product.Product;

public record PlaceOrderResult(
	Long userId,
	List<Product> products,
	Payment payment,
	Order order
) {
	public static PlaceOrderResult of(Long userId, List<Product> products, Payment payment, Order order) {
		return new PlaceOrderResult(
			userId,
			products,
			payment,
			order
		);
	}
}
