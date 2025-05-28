package kr.hhplus.be.server.application.order.handler;

import kr.hhplus.be.server.infra.kafka.publisher.OrderEventPublisher;
import kr.hhplus.be.server.domain.order.event.OrderPlacedEvent;
import kr.hhplus.be.server.application.point.PointCommand;
import kr.hhplus.be.server.application.point.PointService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderPlacedEventHandler {
	private final PointService pointService;
	private final OrderEventPublisher orderEventPublisher;
	@EventListener
	public void handle(OrderPlacedEvent event) {
		pointService.use(PointCommand.Use.of(event.userId(), event.totalPrice()));
		orderEventPublisher.sendOrderPlacedEvent(event);
	}
}
