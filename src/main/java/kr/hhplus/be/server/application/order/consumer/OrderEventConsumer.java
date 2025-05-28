package kr.hhplus.be.server.application.order.consumer;

import kr.hhplus.be.server.domain.order.event.CancelOrderPaidEvent;
import kr.hhplus.be.server.domain.order.event.OrderPlacedEvent;

public interface OrderEventConsumer {
	void handleOrderPlacedEvent(OrderPlacedEvent event);
	void handleOrderCancelPaidEvent(CancelOrderPaidEvent event);
}
