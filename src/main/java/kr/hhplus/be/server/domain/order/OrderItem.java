package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.application.order.dto.OrderItemDTO;
import kr.hhplus.be.server.domain.product.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderItem {
	private final Long id;
	private final Product product;
	private final Long orderId;
	private final Integer quantity;

	public OrderItemDTO toDTO() {
		return OrderItemDTO.builder()
			.productId(product.getId())
			.price(product.getPrice())
			.productName(product.getProductName())
			.quantity(quantity)
			.build();
	}
}
