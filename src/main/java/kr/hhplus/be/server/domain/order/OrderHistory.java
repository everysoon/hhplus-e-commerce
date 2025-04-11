package kr.hhplus.be.server.domain.order;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class OrderHistory {
	private final Long id;
	private final Long orderId;
	private final LocalDateTime createdAt;
	private String description;

	public static OrderHistory of(Order order, String description) {
		return new OrderHistory(null, order.getId(), LocalDateTime.now(), description);
	}
}
