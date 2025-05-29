package kr.hhplus.be.server.domain.order.event;

import kr.hhplus.be.server.application.payment.PaymentCommand;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.payment.PaymentMethod;

import java.math.BigDecimal;

public record OrderPlacedEvent(
	Long userId,
	Long orderId,
	BigDecimal totalPrice,
	PaymentMethod paymentMethod,
	String requestId
) {
	public static OrderPlacedEvent of(Long userId, Order order, PaymentMethod paymentMethod, String requestId) {
		return new OrderPlacedEvent(
			userId,
			order.getId(),
			order.getTotalPrice(),
			paymentMethod,
			requestId
		);
	}

	public PaymentCommand.Request toPayRequest() {
		return new PaymentCommand.Request(
			"MID_SOON_STORE",
			totalPrice,
			"SECRET_MERCHANT_KEY",
			orderId,
			paymentMethod
		);
	}

	public String getIdempotencyKey() {
		return "order:placed".concat(requestId);
	}
}
