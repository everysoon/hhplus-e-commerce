package kr.hhplus.be.server.infra.lock;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RedisLock {
	String lockKey();  // 락을 걸 키
	long leaseTime() default 3000;  // 락 만료 시간 (밀리초 단위)
	long waitTime() default 100;    // 락을 획득하기 위한 최대 대기 시간 (밀리초 단위)
}
