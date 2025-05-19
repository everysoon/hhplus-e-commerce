package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.application.payment.PaymentCommand;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.payment.PaymentMethod;

import java.math.BigDecimal;

public record OrderPaidEvent(
	Long userId,
	Long orderId,
	String MID,
	BigDecimal price,
	String merchantKey,
	PaymentMethod paymentMethod
) {
	public static OrderPaidEvent of(Long userId,Order order, PaymentMethod paymentMethod){
		return new OrderPaidEvent(
			userId,
			order.getId(),
			"MID_SOON_STORE",
			order.getTotalPrice(),
			"SECRET_MERCHANT_KEY",
			paymentMethod
		);
	}
	public PaymentCommand.Request toRequest(){
		return new  PaymentCommand.Request(
			MID,
			price,
			merchantKey,
			orderId,
			paymentMethod
		);
	}
}
