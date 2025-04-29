package kr.hhplus.be.server.infra.lock;

import kr.hhplus.be.server.support.common.exception.CustomException;
import kr.hhplus.be.server.support.config.swagger.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class DistributedLockAspect {

	private final RedissonClient redissonClient;

	@Around("@annotation(redisLock)")
	public Object lock(ProceedingJoinPoint joinPoint, RedisLock redisLock) throws Throwable {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		String lockKey = getDynamicValue(signature.getParameterNames(), joinPoint.getArgs(), redisLock.lockKey());
		long waitTime = redisLock.waitTime();
		long lockTimeout = redisLock.lockTimeout();
		log.info("### DistributedLockAspect lockKey : {} ", lockKey);
		log.info("### DistributedLockAspect waitTime : {} ", redisLock.waitTime());
		log.info("### DistributedLockAspect lockTimeout : {} ", redisLock.lockTimeout());
		RLock lock = redissonClient.getLock(lockKey);
		boolean lockAcquired = lock.tryLock(waitTime, lockTimeout, TimeUnit.MILLISECONDS);

		if (!lockAcquired) {
			throw new CustomException(ErrorCode.LOCK_ACQUISITION_FAIL);
		}
		try {
			return joinPoint.proceed();  // 락을 획득한 후 실제 메서드 실행
		} finally {
			// release lock
			if (lock.isHeldByCurrentThread()) {
				log.info("### [{}] releaseLock : {} ", Thread.currentThread().getName(), lock.getName());
				lock.unlock();
			}
		}
	}

	@AfterThrowing(value = "@annotation(redisLock)", throwing = "ex")
	public void handleLockOnException(RedisLock redisLock, Throwable ex) {
		log.info("### [{}] handleLockOnException ", Thread.currentThread().getName(), ex.getMessage());
	}

	public static String getDynamicValue(String[] parameterNames, Object[] args, String key) {
		ExpressionParser parser = new SpelExpressionParser();
		StandardEvaluationContext context = new StandardEvaluationContext();

		for (int i = 0; i < parameterNames.length; i++) {
			context.setVariable(parameterNames[i], args[i]);
		}

		return parser.parseExpression(key).getValue(context, String.class);
	}
}
