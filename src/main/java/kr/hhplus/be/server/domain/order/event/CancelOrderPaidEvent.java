package kr.hhplus.be.server.domain.order.event;

import java.math.BigDecimal;

public record CancelOrderPaidEvent(
	Long orderId,
	BigDecimal totalPrice
) {
	public String getIdempotencyKey(){
		return "order:canceled:paid".concat(orderId.toString());
	}
}
