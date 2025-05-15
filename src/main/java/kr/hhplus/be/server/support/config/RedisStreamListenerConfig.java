package kr.hhplus.be.server.support.config;

import kr.hhplus.be.server.application.coupon.CouponIssueStreamListener;
import kr.hhplus.be.server.support.utils.CacheKeys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;

import java.time.Duration;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class RedisStreamListenerConfig {
	private final RedisTemplate<String, String> redisTemplate;

	/**
	 * StreamMessageListenerContainer 생성 팩토리 메서드.
	 *
	 * @param connectionFactory Redis 연결 팩토리
	 * @param streamKey         Redis Stream 키
	 * @param groupName         컨슈머 그룹 이름
	 * @param consumerName      컨슈머 이름
	 * @param listener          StreamListener
	 * @return StreamMessageListenerContainer 인스턴스
	 */
	public StreamMessageListenerContainer<String, MapRecord<String, String, String>> createListenerContainer(RedisConnectionFactory connectionFactory,
																													String streamKey,
																													String groupName,
																													String consumerName,
																													StreamListener<String, MapRecord<String, String, String>> listener) {
		createConsumerGroupIfNotExists(streamKey, groupName);
		StreamMessageListenerContainer.StreamMessageListenerContainerOptions<String, MapRecord<String, String, String>> options =
			StreamMessageListenerContainer.StreamMessageListenerContainerOptions.builder()
				.pollTimeout(Duration.ofSeconds(2))
				.build();

		StreamMessageListenerContainer<String, MapRecord<String, String, String>> container =
			StreamMessageListenerContainer.create(connectionFactory, options);

		container.receive(
			Consumer.from(groupName, consumerName),
			StreamOffset.create(streamKey, ReadOffset.lastConsumed()),
			listener
		);

		container.start();
		return container;
	}

	private void createConsumerGroupIfNotExists(String streamKey, String groupName) {
		try {
			redisTemplate.opsForStream().createGroup(streamKey, ReadOffset.lastConsumed(), groupName);
		} catch (RedisSystemException e) {
			if (e.getMessage() != null && e.getMessage().contains("BUSYGROUP")) {
				// 그룹 이미 존재 — 무시
				return;
			}
			throw e;
		}
	}
	@Bean
	public StreamMessageListenerContainer<String, MapRecord<String, String, String>> couponIssueListenerContainer(
		RedisConnectionFactory connectionFactory,
		CouponIssueStreamListener couponIssueStreamListener) {

		return createListenerContainer(
			connectionFactory,
			CacheKeys.COUPON_STREAM.getKey(),
			"coupon-group",
			"coupon-consumer-1",
			couponIssueStreamListener);
	}
}
