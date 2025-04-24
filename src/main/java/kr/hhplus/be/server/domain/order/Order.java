package kr.hhplus.be.server.domain.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.infra.order.entity.OrderStatus;
import kr.hhplus.be.server.support.common.exception.CustomException;
import kr.hhplus.be.server.support.config.swagger.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

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
			this.totalDiscount = BigDecimal.ZERO;
			return this.totalPrice;
		}
		return this.totalPrice.subtract(this.totalDiscount);
	}
	public Order(Long userId, List<Coupon> coupons, List<OrderItem> orderItems) {
		this.userId = userId;
		this.coupons = coupons;
		this.orderItems = orderItems;
		this.orderedAt = LocalDateTime.now();
		calculateItemTotalPrice();
		calculateTotalDiscount();
	}

	public void calculateItemTotalPrice() {
		if (orderItems == null || orderItems.isEmpty()) {
			throw new CustomException(ErrorCode.NOT_EXIST_ORDER_ITEM);
		}
		this.totalPrice = orderItems.stream()
			.map(OrderItem::getProduct)
			.map(Product::getPrice)
			.reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	public void calculateTotalDiscount() {
		if (coupons == null || coupons.isEmpty()) {
			this.totalDiscount = BigDecimal.ZERO;
			return;
		}
		this.totalDiscount = coupons.stream()
			.map(Coupon::getDiscountAmount)
			.reduce(BigDecimal.ZERO, BigDecimal::add);
	}
}
