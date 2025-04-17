package kr.hhplus.be.server.domain.payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import kr.hhplus.be.server.application.payment.RequestPaymentCommand;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Payment {

	private final Long id;
	private PaymentMethod paymentMethod;
	private final LocalDateTime createdAt;
	private final Long orderId;
	private BigDecimal price;
	private PaymentStatus status;

	public static Payment of(RequestPaymentCommand command) {
		return new Payment(
			null,
			command.paymentMethod(),
			LocalDateTime.now(),
			command.orderId(),
			command.price(),
			PaymentStatus.COMPLETED
		);
	}
}
