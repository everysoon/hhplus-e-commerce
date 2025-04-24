package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.domain.product.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class OrderItem {

	private final Long id;
	private final Product product;
	private Long orderId;
	private final Integer quantity;
	private BigDecimal unitPrice;

	public OrderItem setOrderId(Long orderId) {
		this.orderId = orderId;
		return this;
	}
	public static OrderItem create(Product product,Integer quantity){
		return new OrderItem(
			null,
			product,
			null,
			quantity,
			product.getPrice()
		);
	}

}
