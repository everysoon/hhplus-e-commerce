package kr.hhplus.be.server.infra.lock;

import kr.hhplus.be.server.support.aop.lock.LockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class LockRepositoryImpl implements LockRepository {
	private final RedissonClient redissonClient;

	@Override
	public RLock getLock(String lockKey) {
		log.info("### lock lockKey : {}", lockKey);
		return redissonClient.getLock(lockKey);
	}
}
