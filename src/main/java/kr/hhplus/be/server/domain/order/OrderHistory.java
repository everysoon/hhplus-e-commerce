package kr.hhplus.be.server.domain.order;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class OrderHistory {
	private final Long id;
	private final Long orderId;
	private String description;
	private final LocalDateTime createdAt;

	public static OrderHistory of(Long orderId, String description) {
		return new OrderHistory(null, orderId, description, LocalDateTime.now());
	}
	public static OrderHistory of(Long orderId) {
		return new OrderHistory(null, orderId, "", LocalDateTime.now());
	}
}
