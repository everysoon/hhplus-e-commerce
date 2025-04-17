package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.domain.product.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class OrderItem {

	private final Long id;
	private final Long productId;
	private final Order order;
	private final Integer quantity;
	private BigDecimal unitPrice;

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
