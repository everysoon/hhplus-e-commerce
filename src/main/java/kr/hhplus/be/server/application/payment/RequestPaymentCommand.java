package kr.hhplus.be.server.application.payment;

import java.math.BigDecimal;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.payment.PaymentMethod;

public record RequestPaymentCommand(
	String MID,
	BigDecimal price,
	String merchantKey,
	Long orderId,
	PaymentMethod paymentMethod
) {
	public static RequestPaymentCommand of(BigDecimal price,Order order,PaymentMethod paymentMethod) {
		return new RequestPaymentCommand(
			"MID_SOON_STORE",
			price,
			"SECRET_MERCHANT_KEY",
			order.getId(),
			paymentMethod
		);
	}
	public CreatePaymentHistoryCommand toCreatePaymentCommand(Payment payment, String transactionId) {
		return new CreatePaymentHistoryCommand(
			"MID_SOON_STORE",
			price,
			"SECRET_MERCHANT_KEY",
			orderId,
			paymentMethod,
			payment,
			transactionId
		);
	}
	public String combineAll(String token){
		return token+MID+price+merchantKey;
	}
	public String combineInfo(){
		return MID+merchantKey;
	}
}
