package kr.hhplus.be.server.application.coupon;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

import static kr.hhplus.be.server.support.utils.CacheKeys.COUPON_STREAM;

@Component
@RequiredArgsConstructor
public class CouponIssueEventPublisher {
	private final RedisTemplate<String, String> redisTemplate;

	public void publish(CouponIssuedEvent event) {
		try {
			Map<String, String> data = Map.of(
				"couponId", event.couponId(),
				"userId", event.userId().toString()
			);
			redisTemplate.opsForStream().add(COUPON_STREAM.getKey(), data);
		} catch (Exception e) {
			throw new IllegalStateException("Redis Stream 발행 실패", e);
		}
	}
}
