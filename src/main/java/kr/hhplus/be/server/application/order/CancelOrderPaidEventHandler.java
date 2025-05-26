package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.application.dataplatform.PaymentClient;
import kr.hhplus.be.server.application.payment.PaymentCommand;
import kr.hhplus.be.server.application.payment.PaymentService;
import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.payment.PaymentHistory;
import kr.hhplus.be.server.interfaces.dto.PaymentDTO;
import kr.hhplus.be.server.support.utils.RetryHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import static org.springframework.transaction.event.TransactionPhase.AFTER_COMMIT;

@Slf4j
@Component
@RequiredArgsConstructor
public class CancelOrderPaidEventHandler {
	private final PaymentService paymentService;
	private final PaymentClient paymentClient;
	private final RetryHandler retryHandler;
	private final ApplicationEventPublisher applicationEventPublisher;

	@TransactionalEventListener(phase = AFTER_COMMIT)
	public void handle(OrderPaidEvent event) {
		retryHandler.runWithRetry(() -> {
			Payment payment = paymentService.findByOrderId(event.orderId());
			PaymentCommand.Request command = PaymentCommand.Request.of(event.orderId(), event.price(), payment.getPaymentMethod());
			String token = paymentClient.getToken(PaymentDTO.TokenRequest.from(command));
			String transactionId = paymentClient.cancel(PaymentDTO.PaymentRequest.from(command, token));
			payment.cancel(event.price(), transactionId);
			PaymentCommand.CreateHistory payloadCommand = command.toCreatePaymentCommand(payment);
			paymentService.saveWithHistory(payment, new PaymentHistory(payloadCommand));
			log.info("주문 결제 처리 : orderId={}", event.orderId());
			return null;
		}, e -> applicationEventPublisher.publishEvent(new CancelOrderPaidFailEvent(event.userId(), event.orderId(), event.price(), e)), 3);
	}
}
