package com.ask.sample.logger;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.util.Arrays;

@Component
@Aspect
@Slf4j
public class LoggerAspect {

	@Around("execution(* com.ask.sample.service.*.*(..))")
	public Object timeLog(ProceedingJoinPoint pjp) throws Throwable {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();

		log.info("1. args: " + Arrays.toString(pjp.getArgs()));
		log.info("2. name: " + pjp.getSignature().getName());

		Object result = pjp.proceed();

		stopWatch.stop();

		log.info(pjp.getSignature().getName() + " - method time : " + stopWatch.getTotalTimeMillis() + " ms");
		return result;
	}
}