package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.application.payment.PaymentService;
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
	private final ApplicationEventPublisher applicationEventPublisher;

	@TransactionalEventListener(phase = AFTER_COMMIT)
	public void handle(OrderPaidEvent event) {
		try{
		 	paymentService.pay(event.toRequest());
		}catch(Exception e){
			log.error("### OrderPaidEventHandler.handle Error", e);
			// 결제 실패 이벤트 발행
			applicationEventPublisher.publishEvent(new OrderPaidFailEvent(event.userId(), event.orderId()));
		}
	}
}
