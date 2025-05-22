package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.application.payment.PaymentService;
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
public class OrderPaidEventHandler {
	private final PaymentService paymentService;
	private final RetryHandler retryHandler;
	private final ApplicationEventPublisher applicationEventPublisher;

	@TransactionalEventListener(phase = AFTER_COMMIT)
	public void handle(OrderPaidEvent event) {
		retryHandler.runWithRetry(() -> {
			paymentService.pay(event.toRequest());
			log.info("주문 결제 처리 : orderId={}", event.orderId());
			return null;
		}, e -> applicationEventPublisher.publishEvent(new OrderPaidFailEvent(event.userId(), event.orderId(), e)), 3);
	}
}
