package kr.hhplus.be.server.application.coupon;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class CouponIssueDLQPublisher {

	private final RedisTemplate<String, String> redisTemplate;
	private final String DLQ_STREAM_KEY = "coupon:issue:dlq";

	@Async
	public void publishEvent(CouponIssueDLQEvent event) {
		Map<String, String> message = new HashMap<>();
		message.put("userId", event.userId().toString());
		message.put("couponId", event.couponId());
		message.put("error", event.exception().getMessage());

		redisTemplate.opsForStream().add(DLQ_STREAM_KEY, message);
		log.warn("DLQ에 메시지 저장됨: userId={}, couponId={}", event.userId(), event.couponId());
	}
}
