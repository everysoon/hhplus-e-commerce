package kr.hhplus.be.server.domain.order;

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
	private List<String> couponIds;
	private List<OrderItem> orderItems;
	private BigDecimal totalPrice;
	@Setter
	private BigDecimal totalDiscount;
	private final LocalDateTime orderedAt;

	public BigDecimal getTotalPrice() {
		if (this.totalDiscount == null) {
			return this.totalPrice;
		}
		return this.totalPrice.subtract(this.totalDiscount);
	}
//	public void applyCoupon(List<Coupon> coupons) {
//		if(coupons == null || coupons.isEmpty()) {
//			return;
//		}
//		calculateTotalDiscount(coupons);
//	}
//
//	public void calculateTotalPrice(List<Product> products) {
//		if(products == null || products.isEmpty()) {return;}
//		this.totalPrice = products.stream()
//			.map(Product::getPrice)
//			.reduce(BigDecimal.ZERO, BigDecimal::add);
//	}
//
//	public void calculateTotalDiscount(List<Coupon> usedCoupons) {
//		if(usedCoupons == null || usedCoupons.isEmpty()) {return;}
//		this.totalDiscount = usedCoupons.stream()
//			.map(Coupon::getDiscountAmount)
//			.reduce(BigDecimal.ZERO, BigDecimal::add);
//	}
}
