package kr.hhplus.be.server.domain.payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PaymentHistory {
	private final Long id;
	private final Payment payment;
	private final PaymentStatus status;
	private final BigDecimal amount;
	private final String description;
	private final LocalDateTime createdAt;
}
