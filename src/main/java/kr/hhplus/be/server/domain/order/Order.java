package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.product.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class Order {
	private Long id;
	private final Long userId;
	private List<Coupon> coupons;
	private List<OrderItem> orderItems;
	private BigDecimal totalPrice;
	private BigDecimal totalDiscount;
	private final LocalDateTime orderedAt;

	public BigDecimal getTotalPrice() {
		if (this.totalDiscount == null) {
			return this.totalPrice;
		}
		return this.totalPrice.subtract(this.totalDiscount);
	}

	public Order(Long userId, List<Coupon> coupons, List<OrderItem> orderItems) {
		this.userId = userId;
		this.coupons = coupons;
		this.orderItems = orderItems;
		this.orderedAt = LocalDateTime.now();
	}

	public void calculateItemTotalPrice() {
		if (orderItems == null || orderItems.isEmpty()) {
			return;
		}
		this.totalPrice = orderItems.stream()
			.map(OrderItem::getProduct)
			.map(Product::getPrice)
			.reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	public void calculateTotalDiscount() {
		if (coupons == null || coupons.isEmpty()) {
			return;
		}
		this.totalDiscount = coupons.stream()
			.map(Coupon::getDiscountAmount)
			.reduce(BigDecimal.ZERO, BigDecimal::add);
	}
}
