package kr.hhplus.be.server.infra.idempotent;

import kr.hhplus.be.server.support.aop.event.idempotent.IdempotentRepository;
import kr.hhplus.be.server.support.aop.event.idempotent.IdempotentStatus;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
@RequiredArgsConstructor
public class IdempotentRepositoryImpl implements IdempotentRepository {

	private final RedissonClient redissonClient;

	@Override
	public boolean isAlreadyProcessed(String key) {
		RBucket<String> bucket = redissonClient.getBucket(key);
		if (!bucket.isExists()) {
			return false;
		}
		return IdempotentStatus.PROCESSED.getValue().equals(bucket.get());
	}

	@Override
	public void markProcessed(String key, long ttlSeconds) {
		RBucket<String> bucket = redissonClient.getBucket(key);
		bucket.set(IdempotentStatus.PROCESSED.getValue(),Duration.ofSeconds(ttlSeconds));
	}

	@Override
	public void remove(String key) {
		RBucket<String> bucket = redissonClient.getBucket(key);
		bucket.delete();
	}
}
