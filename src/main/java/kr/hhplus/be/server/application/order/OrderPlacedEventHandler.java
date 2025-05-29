package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.application.point.PointCommand;
import kr.hhplus.be.server.application.point.PointService;
import kr.hhplus.be.server.domain.order.event.OrderPaidEvent;
import kr.hhplus.be.server.domain.order.event.OrderPlacedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderPlacedEventHandler {
	private final ApplicationEventPublisher applicationEventPublisher;
	private final PointService pointService;

	@EventListener
	public void handle(OrderPlacedEvent event) {
		pointService.use(PointCommand.Use.of(event.userId(), event.totalPrice()));
		applicationEventPublisher.publishEvent(OrderPaidEvent.of(event.userId(), event.orderId(), event.totalPrice(), event.paymentMethod(), UUID.randomUUID().toString()));
	}
}
