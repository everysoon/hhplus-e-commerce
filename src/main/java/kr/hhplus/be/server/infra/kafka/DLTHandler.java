package kr.hhplus.be.server.infra.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface DLTHandler {
	void handle(ConsumerRecord<String, String> record);
}
