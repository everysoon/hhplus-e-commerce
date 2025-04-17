package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.payment.Payment;

public record CancelOrderResult(
	Order order,
	Payment payment
) {
	public static CancelOrderResult from(Order order, Payment payment) {
		return new CancelOrderResult(order,payment);
	}
}
