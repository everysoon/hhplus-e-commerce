package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.infra.order.entity.OrderItemEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderItem {

	private final Long id;
	private final Product product;
	private final Long orderId;
	private final Integer quantity;

	public static OrderItem from(OrderItemEntity entity) {
		return new OrderItem(
			entity.getId(),
			Product.from(entity.getProductEntity()),
			entity.getOrderEntity().getId(),
			entity.getQuantity()
		);
	}
	public static OrderItem create(Product product,Integer quantity){
		return new OrderItem(
			null,
			product,
			null,
			quantity
		);
	}
}
