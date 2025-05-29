package kr.hhplus.be.server.infra.kafka.resolver;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;

public interface DeadLetterTopicResolver {
	TopicPartition resolveTopicPartition(ConsumerRecord<?, ?> record, Exception exception);
}
