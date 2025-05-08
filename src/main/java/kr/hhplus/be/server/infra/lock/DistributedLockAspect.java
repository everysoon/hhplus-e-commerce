package kr.hhplus.be.server.infra.lock;

import java.util.concurrent.TimeUnit;
import kr.hhplus.be.server.support.common.exception.CustomException;
import kr.hhplus.be.server.support.config.swagger.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class DistributedLockAspect {

	private final RedissonClient redissonClient;
	private final AopForTransaction aopForTransaction;

	@Around("@annotation(redisLock)")
	public Object lock(ProceedingJoinPoint joinPoint, RedisLock redisLock) throws Throwable {
		String lockKey = redisLock.lockKey();
		long waitTime = redisLock.waitTime();
		long lockTimeout = redisLock.lockTimeout();

		RLock lock = redissonClient.getLock(lockKey);
		boolean lockAcquired = lock.tryLock(waitTime, lockTimeout, TimeUnit.MILLISECONDS);

		if (!lockAcquired) {
			throw new CustomException(ErrorCode.LOCK_ACQUISITION_FAIL);
		}
		try {
			return aopForTransaction.proceed(joinPoint);
		} finally {
			// release lock
			if (lock.isHeldByCurrentThread()) {
				log.info("### [{}] releaseLock : {} ", Thread.currentThread().getName(),
					lock.getName());
				lock.unlock();
			}
		}
	}

	@AfterThrowing(value = "@annotation(redisLock)", throwing = "ex")
	public void handleLockOnException(RedisLock redisLock, Throwable ex) {
		log.info("### [{}] handleLockOnException ", Thread.currentThread().getName(),
			ex.getMessage());
	}
}
