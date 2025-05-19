package kr.hhplus.be.server.application.coupon;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CouponIssueDLQPublisher {

	private final RedisTemplate<String, String> redisTemplate;

	private static final String DLQ_STREAM = "coupon:issue:dlq";

	public void publish(MapRecord<String, String, String> message,Exception exception) {
		log.info("DlqMessagePublisher publishing to redis : {}",message.getValue());
		log.info("exception message: {}",exception.getMessage());
		redisTemplate.opsForStream().add(DLQ_STREAM,message.getValue());
	}

}
