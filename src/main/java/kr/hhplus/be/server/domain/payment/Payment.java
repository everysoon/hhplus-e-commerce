package kr.hhplus.be.server.domain.payment;

import kr.hhplus.be.server.application.payment.RequestPaymentCommand;
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
	public static Payment of(RequestPaymentCommand command,String transactionId) {
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
	//	public void cancel(BigDecimal price,String description) {
//		if(this.price.compareTo(price) > 0) {
//			throw new CustomException();
//		}
//		this.price = price;
//		this.description = description;
//	}
}
