package kr.hhplus.be.server.domain.order;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "order_history")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderHistory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	private Long id;

	private Long orderId;

	private String description;

	@CreatedDate
	@Column(nullable = false)
	private LocalDateTime createdAt;

	public OrderHistory(Long orderId) {
		this.orderId = orderId;
		this.createdAt = LocalDateTime.now();
	}

	public OrderHistory(Long orderId, String description) {
		this.orderId = orderId;
		this.description = description;
		this.createdAt = LocalDateTime.now();
	}
}

