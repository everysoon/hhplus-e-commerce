package kr.hhplus.be.server.infra.order.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.order.OrderItem;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Entity
@Table(name = "order_items")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
// 주문이 들어간 products
public class OrderItemEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	private Long id;
	@Column(nullable = false)
	private Long productId;

	private Long orderId;

	private BigDecimal unitPrice; // 주문 당시 개당 가격

	@Column(nullable = false)
	private Integer quantity;
	public OrderItemEntity(Long productId, Long orderId,BigDecimal unitPrice, Integer quantity) {
		this.productId = productId;
		this.orderId = orderId;
		this.unitPrice = unitPrice;
		this.quantity = quantity;
	}
	public OrderItemEntity(Long productId, BigDecimal unitPrice, Integer quantity) {
		this.productId = productId;
		this.unitPrice = unitPrice;
		this.quantity = quantity;
	}
	public static OrderItemEntity from(OrderItem orderItem) {
		return new OrderItemEntity(
			orderItem.getOrderId(),
			orderItem.getProductId(),
			orderItem.getUnitPrice(),
			orderItem.getQuantity()
		);
	}
	public OrderItem toDomain() {
		return new OrderItem(
			this.id,
			this.productId,
			this.orderId,
			this.quantity,
			this.unitPrice
		);
	}
}
