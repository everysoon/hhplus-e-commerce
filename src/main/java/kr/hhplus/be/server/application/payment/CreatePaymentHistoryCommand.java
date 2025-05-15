package kr.hhplus.be.server.application.payment;

import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.payment.PaymentMethod;

import java.math.BigDecimal;

public record CreatePaymentHistoryCommand(
	String MID,
	BigDecimal price,
	String merchantKey,
	Long orderId,
	PaymentMethod paymentMethod,
	Payment payment
) {
}
