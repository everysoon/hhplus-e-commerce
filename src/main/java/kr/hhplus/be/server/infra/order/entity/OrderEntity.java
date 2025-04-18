package kr.hhplus.be.server.infra.order.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import kr.hhplus.be.server.domain.order.Order;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "orders")
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	private Long id;

	@Column(nullable = false)
	private Long userId;

	@ElementCollection
	@CollectionTable(
		name = "order_user_coupon",
		joinColumns = @JoinColumn(
			name = "order_id",
			foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT) // 💥 여기!
		)
	)
	@Column(name = "user_coupon_id")
	private List<String> usedUserCouponIds = new ArrayList<>();

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "orderId")
	private List<OrderItemEntity> orderItems = new ArrayList<>();

	@Setter
	@Column(nullable = false)
	private BigDecimal totalPrice = BigDecimal.ZERO;

	@NotNull
	@Column(nullable = false)
	private BigDecimal totalDiscount = BigDecimal.ZERO;

	@CreatedDate
	@Column(nullable = false)
	private LocalDateTime orderedAt;

	public OrderEntity(Long userId, List<String> usedUserCouponIds, BigDecimal totalPrice, BigDecimal totalDiscount, LocalDateTime orderedAt) {
		this.userId = userId;
		this.usedUserCouponIds = usedUserCouponIds;
		this.totalPrice = totalPrice;
		this.totalDiscount = totalDiscount;
		this.orderedAt = orderedAt;
	}

	public Order toDomain() {
		return new Order(
			this.id,
			this.userId,
			this.usedUserCouponIds,
			this.orderItems.stream().map(OrderItemEntity::toDomain).toList(),
			this.totalPrice,
			this.totalDiscount,
			this.orderedAt
		);
	}

	public static OrderEntity from(Order order) {
		return new OrderEntity(
			order.getUserId(),
			order.getCouponIds(),
			order.getTotalPrice(),
			order.getTotalDiscount(),
			order.getOrderedAt()
		);
	}
}
