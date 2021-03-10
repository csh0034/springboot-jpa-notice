package com.ask.sample.logger;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Aspect
@Slf4j
public class LoggerAspect {

	@Around("execution(* com.ask.sample.service.*.*(..))")
	public Object timeLog(ProceedingJoinPoint pjp) throws Throwable {
		long startTime = System.currentTimeMillis();

		log.info("1: " + Arrays.toString(pjp.getArgs()));
		log.info("2: " + pjp.getSignature().getName());

		Object result = pjp.proceed();

		long endTime = System.currentTimeMillis();
		log.info(pjp.getSignature().getName() + " - 메서드 실행시간 : " + (endTime - startTime) + " ms");

		return result;
	}
}