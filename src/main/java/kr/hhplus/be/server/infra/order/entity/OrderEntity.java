package kr.hhplus.be.server.infra.order.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(
		name = "order_user_coupon",
		joinColumns = @JoinColumn(
			name = "order_id",
			foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)
		)
	)
	@Column(name = "user_coupon_ids")
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

	public OrderEntity(Long id, Long userId, List<String> usedUserCouponIds, List<OrderItemEntity> orderItems, BigDecimal totalPrice, BigDecimal totalDiscount, LocalDateTime orderedAt) {
		this.id = id;
		this.userId = userId;
		this.usedUserCouponIds = usedUserCouponIds;
		this.orderItems = orderItems;
		this.totalPrice = totalPrice;
		this.totalDiscount = totalDiscount == null ? BigDecimal.ZERO : totalDiscount;
		this.orderedAt = orderedAt;
	}
}
