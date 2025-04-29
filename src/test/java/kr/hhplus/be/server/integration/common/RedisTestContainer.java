package kr.hhplus.be.server.integration.common;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@TestConfiguration
public class RedisContainer {
	@Container
	private static final GenericContainer<?> REDIS_CONTAINER =
		new GenericContainer<>("redis:7.2")
			.withExposedPorts(6379);

	@BeforeAll
	public static void startContainer() {
		REDIS_CONTAINER.start();
	}

	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
		String address = REDIS_CONTAINER.getHost();
		Integer port = REDIS_CONTAINER.getMappedPort(6379);
		return new LettuceConnectionFactory(address, port);
	}
}
