package kr.hhplus.be.server.support.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableCaching
@Profile("!test")
public class RedisConfig {

	@Bean(destroyMethod = "shutdown")
	public RedissonClient redissonClient() {
		Config config = new Config();
		config.useSingleServer()
			.setAddress("redis://localhost:6379")
			.setConnectionMinimumIdleSize(10)  // 커넥션 풀 설정
			.setConnectionPoolSize(64);
		config.setCodec(new JsonJacksonCodec()); // Jackson 직렬화 설정
		return Redisson.create(config);
	}

	@Bean
	public RedisTemplate<String, Long> redisTemplate(RedisConnectionFactory factory) {
		RedisTemplate<String, Long> template = new RedisTemplate<>();
		template.setConnectionFactory(factory);
		template.setKeySerializer(new StringRedisSerializer());
		template.setValueSerializer(new GenericToStringSerializer<>(Long.class));
		template.setEnableTransactionSupport(true);
		return template;
	}
}
