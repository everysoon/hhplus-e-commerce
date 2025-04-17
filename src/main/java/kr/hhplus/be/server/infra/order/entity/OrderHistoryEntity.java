package kr.hhplus.be.server.infra.order.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderHistory;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "order_history")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderHistoryEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
	private OrderEntity order;

	private String description;

	@CreatedDate
	@Column(nullable = false)
	private LocalDateTime createdAt;

	public OrderHistoryEntity(Order order, String description, LocalDateTime createdAt) {
		this.order = OrderEntity.from(order);
		this.description = description;
		this.createdAt = createdAt;
	}

	public static OrderHistoryEntity from(OrderHistory orderHistory) {
		return new OrderHistoryEntity(
			orderHistory.getOrder(),
			orderHistory.getDescription(),
			orderHistory.getCreatedAt()
		);
	}

	public OrderHistory toDomain() {
		return new OrderHistory(
			this.id,
			this.order.toDomain(),
			this.description,
			this.createdAt
		);
	}
}
