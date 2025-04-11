package kr.hhplus.be.server.domain.payment;

import kr.hhplus.be.server.domain.order.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class Payment {
	private final Long id;
	private PaymentMethod paymentMethod;
	private final LocalDateTime createdAt;
	private final Order order;
	private PaymentStatus status;
	public static Payment of(Order order, PaymentMethod paymentMethod) {
		return new Payment(
			null,
			paymentMethod,
			LocalDateTime.now(),
			order,
			PaymentStatus.COMPLETED
			);
	}
}
