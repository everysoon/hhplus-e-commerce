package kr.hhplus.be.server.integration.common;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import redis.embedded.RedisServer;

import java.io.IOException;

@TestConfiguration
@Slf4j
@Profile({"test"})
public class EmbeddedRedisConfig {
	private static RedisServer redisServer;

	private static final int REDIS_PORT = 6380;

	@PostConstruct
	public void startRedis() throws IOException {
		redisServer = new RedisServer(REDIS_PORT);
		redisServer.start();
	}

	@PreDestroy
	public void stopRedis() throws IOException {
		if (redisServer != null) {
			redisServer.stop();
		}
	}

	@Bean
	public RedissonClient redissonClient() {
		Config config = new Config();
		config.useSingleServer()
			.setAddress("redis://127.0.0.1:" + REDIS_PORT)
			.setConnectionMinimumIdleSize(1)
			.setConnectionPoolSize(5);
		return Redisson.create(config);
	}
}
