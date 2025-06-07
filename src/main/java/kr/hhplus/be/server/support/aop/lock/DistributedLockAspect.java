package kr.hhplus.be.server.support.aop.lock;

import kr.hhplus.be.server.support.common.exception.CustomException;
import kr.hhplus.be.server.support.config.swagger.ErrorCode;
import kr.hhplus.be.server.support.utils.SpELParserHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class DistributedLockAspect {

	//	private final RedissonClient redissonClient;
	private final LockRepository lockRepository;
	private final AopForTransaction aopForTransaction;

	@Around("@annotation(redisLock)")
	public Object lock(ProceedingJoinPoint joinPoint, RedisLock redisLock) throws Throwable {

		long waitTime = redisLock.waitTime();
		long leaseTime = redisLock.leaseTime();
		Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
		Object[] args = joinPoint.getArgs();

		String parsedLockKey = SpELParserHelper.parseExpression(redisLock.lockKey(), method, args);

		RLock lock = lockRepository.getLock(parsedLockKey);
		log.debug("[분산락 시작] {} 획득 시도", lock.getName());
		boolean lockAcquired = lock.tryLock(waitTime, leaseTime, TimeUnit.MILLISECONDS);

		if (!lockAcquired) {
			log.warn("[분산락 획득 실패] {} {}초 대기 후 락 획득 실패", lock.getName(), waitTime);
			throw new CustomException(ErrorCode.LOCK_ACQUISITION_FAIL);
		}
		try {
			log.debug("[분산락 획득 성공] {} (유효시간: {}초)", lock.getName(), leaseTime);
			return aopForTransaction.proceed(joinPoint);
		} finally {
			// release lock
			if (lock.isHeldByCurrentThread()) {
				log.debug("[분산락 해제] {}", lock.getName());
				lock.unlock();
			}
		}
	}

	@AfterThrowing(value = "@annotation(redisLock)", throwing = "ex")
	public void handleLockOnException(RedisLock redisLock, Throwable ex) {
		log.error("분산락 {} 획득 중 오류 발생", Thread.currentThread().getName(), ex);
	}
}
