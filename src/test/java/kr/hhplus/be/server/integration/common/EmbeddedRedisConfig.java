package kr.hhplus.be.server.integration.common;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.io.IOException;
import java.net.Inet4Address;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Profile;
import redis.embedded.RedisCluster;
import redis.embedded.RedisSentinel;
import redis.embedded.core.RedisSentinelBuilder;

@TestConfiguration
@Slf4j
@Profile({"local", "test"})
public class EmbeddedRedisConfig {

	private RedisCluster cluster;

	@PostConstruct
	public void startRedis() throws IOException, IllegalAccessException {
		String bindAddress = Inet4Address.getLocalHost().getHostAddress();
		RedisSentinelBuilder sentinelBuilder = RedisSentinel.newRedisSentinel();
		sentinelBuilder.bind(bindAddress);

		cluster = RedisCluster.newRedisCluster()
			.withSentinelBuilder(sentinelBuilder)
			.ephemeralServers()
			.sentinelStartingPort(26400)
			.sentinelCount(3)
			.quorumSize(2)
			.replicationGroup("master1", 1)
			.replicationGroup("master2", 1)
			.replicationGroup("master3", 1)
			.build();
		cluster.start();
	}

	@PreDestroy
	public void stopRedis() throws IOException {
		cluster.stop();
	}
}
