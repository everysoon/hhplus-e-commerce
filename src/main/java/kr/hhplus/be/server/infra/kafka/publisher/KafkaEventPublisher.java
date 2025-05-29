package kr.hhplus.be.server.infra.kafka.publisher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.support.event.EventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaEventPublisher implements EventPublisher{
	private final KafkaTemplate<String, String> kafkaTemplate;
	private final ObjectMapper objectMapper;
	@Override
	public <T> void publish(String topic, T event) {
		try {
			kafkaTemplate.send(topic,objectMapper.writeValueAsString(event));
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
}
