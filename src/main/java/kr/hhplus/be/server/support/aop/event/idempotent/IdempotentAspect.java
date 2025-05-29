package kr.hhplus.be.server.support.aop.event.idempotent;

import kr.hhplus.be.server.support.common.exception.CustomException;
import kr.hhplus.be.server.support.utils.SpELParserHelper;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

import static kr.hhplus.be.server.support.config.swagger.ErrorCode.ALREADY_PROCESSED_EVENT;

@Aspect
@Component
@RequiredArgsConstructor
public class IdempotentAspect {
	private final IdempotentRepository idempotentRepository;

	@Around("@annotation(idempotent)")
	public Object around(ProceedingJoinPoint joinPoint, Idempotent idempotent) throws Throwable {
		Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
		Object[] args = joinPoint.getArgs();

		String parsedLockKey = SpELParserHelper.parseExpression(idempotent.key(), method, args);
		if(idempotentRepository.isAlreadyProcessed(parsedLockKey)){
			throw new CustomException(ALREADY_PROCESSED_EVENT);
		}
		try {
			Object proceed = joinPoint.proceed();
			idempotentRepository.markProcessed(parsedLockKey, idempotent.ttlSeconds());
			return proceed;
		} catch (Exception e) {
			throw e;
		}
	}
}
