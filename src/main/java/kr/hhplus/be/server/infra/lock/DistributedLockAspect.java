package kr.hhplus.be.server.infra.lock;

import java.sql.SQLException;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.hibernate.exception.LockAcquisitionException;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class DistributedLockAspect {

	private final RedissonClient redissonClient;

	@Around("@annotation(redisLock)")
	public Object lock(ProceedingJoinPoint joinPoint, RedisLock redisLock) throws Throwable {
		log.info("### DistributedLockAspect lockKey : {} ",redisLock.lockKey());
		log.info("### DistributedLockAspect params : {} ",redisLock.params());
		log.info("### DistributedLockAspect waitTime : {} ",redisLock.waitTime());
		log.info("### DistributedLockAspect lockTimeout : {} ",redisLock.lockTimeout());
		String lockKey = redisLock.lockKey();
		long waitTime = redisLock.waitTime();
		long lockTimeout = redisLock.lockTimeout();

		RLock lock = redissonClient.getLock(lockKey);
		boolean lockAcquired = lock.tryLock(waitTime, lockTimeout, TimeUnit.MILLISECONDS);

		if (!lockAcquired) {
			throw new LockAcquisitionException("Unable to acquire lock within the wait time",
				new SQLException());
		}

		try {
			return joinPoint.proceed();  // 락을 획득한 후 실제 메서드 실행
		} finally {
			lock.unlock();  // 메서드 실행 후 락 해제
		}
	}
	@AfterThrowing(value = "@annotation(redisLock)", throwing = "ex")
	public void handleLockOnException(RedisLock redisLock, Throwable ex) {
		// 예외가 발생하면 락 해제
		log.info("### DistributedLockAspect handleLockOnException : {} ",ex.getMessage());
		String lockKey = redisLock.lockKey();
		RLock lock = redissonClient.getLock(lockKey);
		if (lock.isHeldByCurrentThread()) {
			lock.unlock();
		}
	}

	@After(value = "@annotation(redisLock)")
	public void releaseLock(RedisLock redisLock) {
		// 트랜잭션 성공/롤백 여부와 관계없이 락 해제
		log.info("### DistributedLockAspect releaseLock : {} ",redisLock.lockKey());
		String lockKey = redisLock.lockKey();
		RLock lock = redissonClient.getLock(lockKey);
		if (lock.isHeldByCurrentThread()) {
			lock.unlock();
		}
	}
}
