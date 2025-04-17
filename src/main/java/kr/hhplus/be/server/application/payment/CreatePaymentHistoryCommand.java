package kr.hhplus.be.server.application.payment;

import java.math.BigDecimal;
import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.payment.PaymentMethod;

public record CreatePaymentHistoryCommand(
	String MID,
	BigDecimal price,
	String merchantKey,
	Long orderId,
	PaymentMethod paymentMethod,
	Payment payment,
	String transactionId
) {
}
