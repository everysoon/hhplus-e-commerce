package kr.hhplus.be.server.infra.order.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderItem;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

@Getter
@Entity
@Table(name = "orders")
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

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
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

	public OrderEntity(Long userId, List<String> usedUserCouponIds, List<OrderItem> orderItems, BigDecimal totalPrice, BigDecimal totalDiscount, LocalDateTime orderedAt) {
		this.userId = userId;
		this.usedUserCouponIds = usedUserCouponIds;
		this.orderItems = orderItems.stream().map(OrderItemEntity::from).toList();
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
			order.getOrderItems(),
			order.getTotalPrice(),
			order.getTotalDiscount(),
			order.getOrderedAt()
		);
	}
}
