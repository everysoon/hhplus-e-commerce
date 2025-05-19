package kr.hhplus.be.server.application.order;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderPaidFailEventHandler {
	private final OrderFacade orderFacade;

	@EventListener
	public void handle(OrderPaidFailEvent event) {
		try{
			orderFacade.cancel(OrderCriteria.Cancel.from(event.userId(),event.orderId()));
		}catch(Exception e){
			log.error("### OrderPaidFailEventHandler.handle Error", e);
			// 결제 실패 이벤트 발행
		}
	}
}
