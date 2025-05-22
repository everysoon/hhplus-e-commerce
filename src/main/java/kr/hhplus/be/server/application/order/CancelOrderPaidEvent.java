package kr.hhplus.be.server.application.order;

import java.math.BigDecimal;

public record CancelOrderPaidEvent(
	Long orderId,
	BigDecimal totalPrice
) {
}
