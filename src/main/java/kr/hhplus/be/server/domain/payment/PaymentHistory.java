package kr.hhplus.be.server.domain.payment;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class PaymentHistory {
	private final Long id;
	private final Payment payment;
	private final PaymentStatus status;
	private final BigDecimal amount;
	private final String description;
	private final LocalDateTime createdAt;
	private final String transactionId;
	public static PaymentHistory of(Payment payment,String transactionId) {
		return new PaymentHistory(
			null,
			payment,
			payment.getStatus(),
			payment.getOrder().getTotalDiscount(),
			null,
			LocalDateTime.now(),
			transactionId
		);
	}
}
