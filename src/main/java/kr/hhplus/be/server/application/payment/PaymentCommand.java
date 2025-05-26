package kr.hhplus.be.server.application.payment;

import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.payment.PaymentMethod;

import java.math.BigDecimal;

public class PaymentCommand {
	public record Request(
		String MID,
		BigDecimal price,
		String merchantKey,
		Long orderId,
		PaymentMethod paymentMethod
	) {
		public static Request of(Long orderId, BigDecimal totalPrice, PaymentMethod paymentMethod) {
			return new Request(
				"MID_SOON_STORE",
				totalPrice,
				"SECRET_MERCHANT_KEY",
				orderId,
				paymentMethod
			);
		}

		public static Request of(Order order, PaymentMethod paymentMethod) {
			return new Request(
				"MID_SOON_STORE",
				order.getTotalPrice(),
				"SECRET_MERCHANT_KEY",
				order.getId(),
				paymentMethod
			);
		}

		public CreateHistory toCreatePaymentCommand(Payment payment) {
			return new CreateHistory(
				"MID_SOON_STORE",
				price,
				"SECRET_MERCHANT_KEY",
				orderId,
				paymentMethod,
				payment
			);
		}

		public String combineAll(String token) {
			return token + MID + price + merchantKey;
		}

		public String combineInfo() {
			return MID + merchantKey;
		}
	}

	public record CreateHistory(
		String MID,
		BigDecimal price,
		String merchantKey,
		Long orderId,
		PaymentMethod paymentMethod,
		Payment payment
	) {

	}
}
