package kr.hhplus.be.server.domain.order;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Entity
@Table(name = "order_items")
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
// 주문이 들어간 products
public class OrderItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	private Long id;

	@Column(nullable = false)
	private Long productId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id", nullable = false)
	private Order order;

	private BigDecimal unitPrice; // 주문 당시 개당 가격

	@Column(nullable = false)
	private Integer quantity;

	public OrderItem(Long productId, BigDecimal price, Integer quantity) {
		this.productId = productId;
		this.unitPrice = price;
		this.quantity = quantity;
	}

	public void setOrder(Order order) {
		this.order = order;
	}
}
