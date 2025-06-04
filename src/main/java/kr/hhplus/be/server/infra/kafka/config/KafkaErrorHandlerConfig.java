package kr.hhplus.be.server.infra.kafka.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
@RequiredArgsConstructor
public class KafkaErrorHandlerConfig {

	// DLT로 메시지를 보내는 recoverer 빈 등록
	@Bean
	public DeadLetterPublishingRecoverer deadLetterPublishingRecoverer(KafkaTemplate<Object, Object> kafkaTemplate) {
		return new DeadLetterPublishingRecoverer(kafkaTemplate);
	}

	// DefaultErrorHandler 빈 등록 - 재시도 3회 후 DLT로 발행
	@Bean
	public DefaultErrorHandler defaultErrorHandler(DeadLetterPublishingRecoverer recoverer) {
		// FixedBackOff(delay, maxAttempts): delay(ms) 후 재시도, maxAttempts 재시도 횟수
		FixedBackOff fixedBackOff = new FixedBackOff(1000L, 3L);

		// recoverer와 backoff 설정
		DefaultErrorHandler errorHandler = new DefaultErrorHandler(recoverer, fixedBackOff);

		// 필요 시 특정 예외 무시 또는 추가 처리 가능 (예: 특정 예외는 재시도하지 않도록 설정)
		// errorHandler.addNotRetryableExceptions(SpecificException.class);

		return errorHandler;
	}
}
