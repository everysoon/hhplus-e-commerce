package kr.hhplus.be.server.domain.order;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class OrderHistory {
	private final Long id;
	private final Order order;
	private String description;
	private final LocalDateTime createdAt;

	public static OrderHistory of(Order order, String description) {
		return new OrderHistory(null, order, description, LocalDateTime.now());
	}
	public static OrderHistory of(Order order) {
		return new OrderHistory(null, order, "", LocalDateTime.now());
	}
}
