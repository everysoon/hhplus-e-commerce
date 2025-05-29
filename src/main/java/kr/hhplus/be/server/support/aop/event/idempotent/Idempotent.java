package kr.hhplus.be.server.support.aop.event.idempotent;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Idempotent {
	String key();  // ex. "#event.id"
	long ttlSeconds() default 86400;  // 기본 하루
}
