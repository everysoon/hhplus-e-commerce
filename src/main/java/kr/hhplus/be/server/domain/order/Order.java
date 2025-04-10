package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.product.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class Order {

	private final Long id;
	private final Long userId;
	@Setter
	private Payment payment;
	@Setter
	private List<OrderCoupon> orderCoupons;
	private BigDecimal totalPrice;
	private BigDecimal totalDiscount;
	private final LocalDateTime orderedAt;

	public BigDecimal getTotalPrice() {
		return this.totalPrice.subtract(this.totalDiscount);
	}

	public void applyCoupon(Coupon coupon) {
		this.orderCoupons.add(new OrderCoupon(this, coupon));
		calculateTotalDiscount();
	}
	public static Order create(Long userId) {
		return new Order(
			null,
			userId,
			null,
			null,
			BigDecimal.ZERO,
			BigDecimal.ZERO,
			LocalDateTime.now()
		);
	}
	public void calculateTotalPrice(List<OrderItem> orderItems) {
		this.totalPrice =  orderItems.stream()
			.map(OrderItem::getProduct)
			.map(Product::getPrice)
			.reduce(BigDecimal.ZERO, BigDecimal::add);
	}
	public void calculateTotalDiscount() {
		this.totalDiscount =orderCoupons.stream()
			.map(OrderCoupon::getDiscountAmount)
			.reduce(BigDecimal.ZERO, BigDecimal::add);
	}
}
