package kr.hhplus.be.server.domain.payment;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Payment {
	private final Long id;
	private PaymentMethod paymentMethod;
	private final LocalDateTime createdAt;
	private final Long orderId;
}
