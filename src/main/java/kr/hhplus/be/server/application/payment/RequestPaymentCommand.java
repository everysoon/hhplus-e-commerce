package kr.hhplus.be.server.application.payment;

import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.payment.PaymentMethod;

import java.math.BigDecimal;

public record RequestPaymentCommand(
	String MID,
	BigDecimal price,
	String merchantKey,
	Order order,
	PaymentMethod paymentMethod
) {
	public static RequestPaymentCommand of(BigDecimal price,Order order,PaymentMethod paymentMethod) {
		return new RequestPaymentCommand(
			"MID_SOON_STORE",
			price,
			"SECRET_MERCHANT_KEY",
			order,
			paymentMethod
		);
	}
	public String combineAll(String token){
		return token+MID+price+merchantKey;
	}
	public String combineInfo(){
		return MID+merchantKey;
	}
}
