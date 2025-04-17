package kr.hhplus.be.server.domain.payment;

import kr.hhplus.be.server.application.payment.CreatePaymentHistoryCommand;
import kr.hhplus.be.server.infra.payment.entity.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class PaymentHistory {

	private final Long id;
	private final Long orderId;
	private final Payment payment;
	private final PaymentStatus status;
	private final BigDecimal price;
	private String description;
	private final LocalDateTime createdAt;
	private final String transactionId;

	public static PaymentHistory of(CreatePaymentHistoryCommand command){
		return new PaymentHistory(
			null,
			command.orderId(),
			command.payment(),
			command.payment().getStatus(),
			command.price(),
			null,
			LocalDateTime.now(),
			command.payment().getTransactionId()
		);
	}
}
