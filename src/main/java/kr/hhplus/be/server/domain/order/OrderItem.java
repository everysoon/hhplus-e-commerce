package kr.hhplus.be.server.domain.order;

import java.math.BigDecimal;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.infra.order.entity.OrderItemEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderItem {

	private final Long id;
	private final Long productId;
	private final Long orderId;
	private final Integer quantity;
	private BigDecimal unitPrice;

	public static OrderItem from(OrderItemEntity entity) {
		return new OrderItem(
			entity.getId(),
			entity.getProductId(),
			entity.getOrder().getId(),
			entity.getQuantity(),
			entity.getUnitPrice()
		);
	}
	public static OrderItem create(Product product,Integer quantity){
		return new OrderItem(
			null,
			product.getId(),
			null,
			quantity,
			product.getPrice()
		);
	}

}
