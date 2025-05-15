package kr.hhplus.be.server.infra.order.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id", nullable = false)
	private OrderEntity order;

	private BigDecimal unitPrice; // 주문 당시 개당 가격

	@Column(nullable = false)
	private Integer quantity;

	public OrderItemEntity(Long id, Long productId, OrderEntity order, BigDecimal unitPrice, Integer quantity) {
		this.id = id;
		this.order = order;
		this.productId = productId;
		this.unitPrice = unitPrice;
		this.quantity = quantity;
	}
}
