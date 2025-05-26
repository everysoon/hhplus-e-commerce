package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.application.dlqRecord.DLQRecordService;
import kr.hhplus.be.server.domain.dlqRecord.DLQRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderPaidFailEventHandler {
	private final OrderFacade orderFacade;
	private final DLQRecordService dlqRecordService;
	@Async
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void handle(OrderPaidFailEvent event) {
		try{
			orderFacade.cancel(OrderCriteria.Cancel.from(event.userId(), event.orderId()));
		}catch(Exception e){
			DLQRecord dlqRecord = DLQRecord.builder()
				.dlqEventType(DLQRecord.DLQEventType.ORDER_PAID)
				.eventKey(event.getEventKey())
				.userId(event.userId())
				.errorMessage(e.getMessage())
				.build();
			dlqRecordService.save(dlqRecord);
			log.error("### OrderPaidFailEventHandler.handle Error", e);
		}
	}
}
