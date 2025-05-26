package kr.hhplus.be.server.application.coupon;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class CouponIssueDLQHandler {

	private final CouponIssueProcessor processor;
	private final RedisTemplate<String, String> redisTemplate;

	private final String DLQ_STREAM_KEY = "coupon:issue:dlq";
	private static final String GROUP_NAME = "coupon-dlq-group";
	private static final String CONSUMER_NAME = "dlq-processor";

	@Scheduled(fixedDelay = 10_000) // 10초
	public void processDLQMessages() {
		createGroupIfNotExists();
		List<MapRecord<String, Object, Object>> messages = redisTemplate.opsForStream()
			.read(
				Consumer.from(GROUP_NAME, CONSUMER_NAME),
				StreamReadOptions.empty().count(10).block(Duration.ofSeconds(2)),
				StreamOffset.create(DLQ_STREAM_KEY, ReadOffset.from("0"))
			);
		if (messages == null || messages.isEmpty()) {
			log.debug("DLQ에 처리할 메시지 없음");
			return;
		}
		for (MapRecord<String, Object, Object> message : Objects.requireNonNull(messages)) {
			try {
				Long userId = Long.parseLong((String) message.getValue().get("userId"));
				String couponId = (String) message.getValue().get("couponId");

				processor.process(userId, couponId);
				log.info("DLQ 재처리 성공: messageId={}, userId={}, couponId={}",
					message.getId(), userId, couponId);
				// 메시지 ack 처리(컨슈머 그룹에서 처리 완료 표시)
				redisTemplate.opsForStream()
					.acknowledge(DLQ_STREAM_KEY, GROUP_NAME, message.getId());
			} catch (Exception e) {
				log.warn("DLQ 재처리 실패: {}", message.getId(), e);
			}
		}
	}

	private void createGroupIfNotExists() {
		try {
			log.info("createGroupIfNotExists groupName : {}, streamKey : {}", DLQ_STREAM_KEY,
				GROUP_NAME);
			RedisConnection conn = redisTemplate.getConnectionFactory().getConnection();
			conn.streamCommands().xGroupCreate(
				DLQ_STREAM_KEY.getBytes(StandardCharsets.UTF_8),
				GROUP_NAME,
				ReadOffset.from("0"),
				true // MKSTREAM
			);
		} catch (Exception e) {
			log.info("### RedisSystemException?!", e.getMessage());
			if (e.getMessage() != null && e.getMessage().contains("BUSYGROUP")) {
				// 그룹 이미 존재 — 무시
				log.info("### Consumer group '{}' already exists for stream '{}'", e.getMessage());
			}
		}
	}
}
