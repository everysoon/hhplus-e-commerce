package kr.hhplus.be.server.domain.order.event;

import kr.hhplus.be.server.application.payment.PaymentCommand;
import kr.hhplus.be.server.domain.payment.PaymentMethod;

import java.math.BigDecimal;

public record OrderPaidEvent(
	Long userId,
	Long orderId,
	String MID,
	BigDecimal price,
	String merchantKey,
	PaymentMethod paymentMethod,
	String requestId
) {
	public static OrderPaidEvent of(Long userId, Long orderId, BigDecimal totalPrice, PaymentMethod paymentMethod,String requestId) {
		return new OrderPaidEvent(
			userId,
			orderId,
			"MID_SOON_STORE",
			totalPrice,
			"SECRET_MERCHANT_KEY",
			paymentMethod,
			requestId
		);
	}

	public PaymentCommand.Request toRequest() {
		return new PaymentCommand.Request(
			MID,
			price,
			merchantKey,
			orderId,
			paymentMethod
		);
	}
}
