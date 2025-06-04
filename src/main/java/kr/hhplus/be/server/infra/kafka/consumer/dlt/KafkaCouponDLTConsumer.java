package kr.hhplus.be.server.infra.kafka.consumer.dlt;

import kr.hhplus.be.server.application.dltRecord.DLTRecordService;
import kr.hhplus.be.server.domain.dltRecord.DLTRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaCouponDLTConsumer {
	private final DLTRecordService dlqRecordService;

	@KafkaListener(topics = "coupon.issued.DLT", groupId = "coupon-dlt-group")
	public void handlePaidDLT(ConsumerRecord<String, String> record) {
		log.error("Received DLT message for CouponIssuedEvent: {}", record.value());

		DLTRecord dltRecord = createRecord(DLTRecord.DLTEventType.COUPON_ISSUE, record);
		dlqRecordService.save(dltRecord);
	}

	private DLTRecord createRecord(DLTRecord.DLTEventType eventType, ConsumerRecord<String, String> record) {
		return DLTRecord.builder()
			.eventType(eventType)
			.messageKey(record.key())
			.value(record.value())
			.timestamp(record.timestamp())
			.partitionNum(record.partition())
			.offset(record.offset())
			.build();
	}
}
