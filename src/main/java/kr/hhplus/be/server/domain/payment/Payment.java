package kr.hhplus.be.server.domain.payment;

import kr.hhplus.be.server.application.payment.PaymentCommand;
import kr.hhplus.be.server.infra.payment.entity.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class Payment {

	private final Long id;
	private PaymentMethod paymentMethod;
	private final LocalDateTime paidAt;
	private final Long orderId;
	private BigDecimal price;
	private PaymentStatus status;
	private String transactionId;
	public static Payment of(PaymentCommand.Request command, String transactionId) {
		return new Payment(
			null,
			command.paymentMethod(),
			LocalDateTime.now(),
			command.orderId(),
			command.price(),
			PaymentStatus.COMPLETED,
			transactionId
		);
	}
	public void cancel(BigDecimal price, String transactionId) {
		this.price = price;
		this.transactionId = transactionId;
		this.status = PaymentStatus.CANCELED;
	}

}
