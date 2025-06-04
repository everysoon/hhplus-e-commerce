package kr.hhplus.be.server.infra.kafka.resolver;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
import org.springframework.stereotype.Component;

@Component
public class DefaultDeadLetterTopicResolver implements DeadLetterTopicResolver {
	@Override
	public TopicPartition resolveTopicPartition(ConsumerRecord<?, ?> record, Exception exception) {
		return new TopicPartition(record.topic().concat(".DLT"), record.partition());
	}
}
