package kr.hhplus.be.server.domain.order;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import kr.hhplus.be.server.domain.coupon.Coupon;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	private Long id;

	@Column(nullable = false)
	private Long userId;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(
		name = "user_coupon_ids",
		joinColumns = @JoinColumn(
			name = "order_id",
			foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)
		)
	)
	@Column(name = "user_coupon_ids")
	private List<String> usedUserCouponIds = new ArrayList<>();

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "order_item_id")
	@Setter
	private List<OrderItem> orderItems = new ArrayList<>();

	@Column(nullable = false)
	private BigDecimal totalPrice = BigDecimal.ZERO;

	@NotNull
	@Column(nullable = false)
	private BigDecimal totalDiscount = BigDecimal.ZERO;

	@CreatedDate
	@Column(nullable = false)
	private LocalDateTime orderedAt;

	public BigDecimal getTotalPrice() {
		if (this.totalDiscount == null) {
			this.totalDiscount = BigDecimal.ZERO;
			return this.totalPrice;
		}
		return this.totalPrice.subtract(this.totalDiscount);
	}
	public Order(Long userId, List<Coupon> coupons, List<OrderItem> orderItems){
		this.usedUserCouponIds  = coupons.stream().map(Coupon::getId).toList();
		this.totalDiscount = coupons.stream()
			.map(Coupon::getDiscountAmount)
			.reduce(BigDecimal.ZERO, BigDecimal::add);
		this.userId = userId;
		this.orderItems = orderItems;
		this.orderItems.forEach((oi)->oi.setOrder(this));
		this.totalPrice = getTotalPrice();
		this.orderedAt = LocalDateTime.now();
	}

//	public Order(Long userId, List<Coupon> coupons, List<OrderItem> orderItems) {
//		this.userId = userId;
//		this.coupons = coupons;
//		this.orderItems = orderItems;
//		this.orderedAt = LocalDateTime.now();
//		calculateItemTotalPrice();
//		calculateTotalDiscount();
//	}
//	public void calculateItemTotalPrice() {
//		if (orderItems == null || orderItems.isEmpty()) {
//			throw new CustomException(ErrorCode.NOT_EXIST_ORDER_ITEM);
//		}
//		this.totalPrice = orderItems.stream()
//			.map(OrderItem::getProduct)
//			.map(Product::getPrice)
//			.reduce(BigDecimal.ZERO, BigDecimal::add);
//	}

//	public void calculateTotalDiscount() {
//		if (coupons == null || coupons.isEmpty()) {
//			this.totalDiscount = BigDecimal.ZERO;
//			return;
//		}
//		this.totalDiscount = coupons.stream()
//			.map(Coupon::getDiscountAmount)
//			.reduce(BigDecimal.ZERO, BigDecimal::add);
//	}
}
