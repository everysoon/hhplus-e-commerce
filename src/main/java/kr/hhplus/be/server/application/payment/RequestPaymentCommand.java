package kr.hhplus.be.server.application.payment;

import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.payment.PaymentMethod;

import java.math.BigDecimal;

public record RequestPaymentCommand(
	String MID,
	BigDecimal price,
	String merchantKey,
	Long orderId,
	PaymentMethod paymentMethod
) {
	public static RequestPaymentCommand of(Order order,PaymentMethod paymentMethod) {
		return new RequestPaymentCommand(
			"MID_SOON_STORE",
			order.getTotalPrice(),
			"SECRET_MERCHANT_KEY",
			order.getId(),
			paymentMethod
		);
	}
	public CreatePaymentHistoryCommand toCreatePaymentCommand(Payment payment) {
		return new CreatePaymentHistoryCommand(
			"MID_SOON_STORE",
			price,
			"SECRET_MERCHANT_KEY",
			orderId,
			paymentMethod,
			payment
		);
	}
	public String combineAll(String token){
		return token+MID+price+merchantKey;
	}
	public String combineInfo(){
		return MID+merchantKey;
	}
}
