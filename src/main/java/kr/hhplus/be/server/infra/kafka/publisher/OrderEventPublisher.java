package kr.hhplus.be.server.infra.kafka.publisher;

import kr.hhplus.be.server.domain.order.event.CancelOrderPaidEvent;
import kr.hhplus.be.server.domain.order.event.OrderPlacedEvent;
import kr.hhplus.be.server.support.event.EventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderEventPublisher {
	private final EventPublisher eventPublisher;
	public void sendOrderPlacedEvent(OrderPlacedEvent orderPlacedEvent) {
		eventPublisher.publish("order.placed", orderPlacedEvent);
	}
	public void sendOrderCancelPaidEvent(CancelOrderPaidEvent cancelOrderPaidEvent) {
		eventPublisher.publish("order.cancel.paid", cancelOrderPaidEvent);
	}
}
