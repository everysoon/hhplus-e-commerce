package kr.hhplus.be.server.infra.kafka.consumer;

import kr.hhplus.be.server.application.order.consumer.OrderEventConsumer;
import kr.hhplus.be.server.application.payment.PaymentService;
import kr.hhplus.be.server.domain.order.event.CancelOrderPaidEvent;
import kr.hhplus.be.server.domain.order.event.OrderPlacedEvent;
import kr.hhplus.be.server.support.aop.event.idempotent.Idempotent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaOrderEventConsumer implements OrderEventConsumer {

	private final PaymentService paymentService;

	@KafkaListener(topics = "order.placed.paid", groupId = "order-group")
	@Idempotent(key = "#event.getIdempotencyKey()")
	public void consumeOrderPlaced(OrderPlacedEvent event) {
		handleOrderPlacedEvent(event);
	}

	@KafkaListener(topics = "order.canceled.paid", groupId = "order-group")
	@Idempotent(key = "#event.getIdempotencyKey()")
	public void consumeOrderPlaced(CancelOrderPaidEvent event) {
		handleOrderCancelPaidEvent(event);
	}

	@Override
	public void handleOrderPlacedEvent(OrderPlacedEvent event) {
		log.info("Received OrderPlacedEvent: {}", event);
		paymentService.pay(event.toPayRequest());
	}

	@Override
	public void handleOrderCancelPaidEvent(CancelOrderPaidEvent event) {
		log.info("Received CancelOrderPaidEvent: {}", event);
		paymentService.cancel(event.orderId(), event.totalPrice());
	}
}
