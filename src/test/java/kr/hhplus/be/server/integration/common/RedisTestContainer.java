package kr.hhplus.be.server.integration.common;

import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration
@Profile("test")
@Slf4j
public class RedisTestContainer {
	private static final int REDIS_PORT = 6379;


	@Bean(initMethod = "start", destroyMethod = "stop")
	public GenericContainer redisContainer() {
		return new GenericContainer<>(DockerImageName.parse("redis:7.0"))
			.withExposedPorts(REDIS_PORT);
	}

	@Bean
	public RedisConnectionFactory redisConnectionFactory(GenericContainer redisContainer) {
		String host = redisContainer.getHost();
		Integer port = redisContainer.getMappedPort(REDIS_PORT);

		log.info("Redis TestContainer is running at {}:{}", host, port);

		RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration();
		redisConfig.setHostName(host);
		redisConfig.setPort(port);

		return new LettuceConnectionFactory(redisConfig);
	}

	@Bean(destroyMethod = "shutdown")
	public RedissonClient redissonClient(GenericContainer redisContainer) {
		Config config = new Config();
		String redisAddress = String.format("redis://%s:%d",
			redisContainer.getHost(),
			redisContainer.getMappedPort(REDIS_PORT));

		log.info("Creating RedissonClient for TestContainer at {}:{}", redisContainer.getHost(), redisContainer.getMappedPort(REDIS_PORT));

		config.useSingleServer()
			.setAddress(redisAddress)
			.setConnectionMinimumIdleSize(10)
			.setConnectionPoolSize(64);
		config.setCodec(new JsonJacksonCodec());

		return Redisson.create(config);
	}
}
