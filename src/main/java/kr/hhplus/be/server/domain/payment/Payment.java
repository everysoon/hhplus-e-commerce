package kr.hhplus.be.server.domain.payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import kr.hhplus.be.server.application.payment.PaymentCommand;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.infra.payment.entity.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Payment {

	private final Long id;
	private PaymentMethod paymentMethod;
	private final LocalDateTime paidAt;
	private Long orderId;
	private BigDecimal price;
	private PaymentStatus status;
	private String transactionId;

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

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

	public static Payment create(Order order) {
		return new Payment(
			null,
			PaymentMethod.POINTS,
			LocalDateTime.now(),
			order.getId(),
			order.getTotalPrice(),
			PaymentStatus.COMPLETED,
			UUID.randomUUID().toString()
		);
	}

	public void cancel(BigDecimal price, String transactionId) {
		this.price = price;
		this.transactionId = transactionId;
		this.status = PaymentStatus.CANCELED;
	}

}
