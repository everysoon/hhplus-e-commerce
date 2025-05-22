package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.payment.PaymentMethod;

import java.math.BigDecimal;

public record OrderPlacedEvent(
	Long userId,
	Long orderId,
	BigDecimal totalPrice,
	PaymentMethod paymentMethod
) {
	public static OrderPlacedEvent of(Long userId, Order order, PaymentMethod paymentMethod) {
		return new OrderPlacedEvent(
			userId,
			order.getId(),
			order.getTotalPrice(),
			paymentMethod
		);
	}
}
