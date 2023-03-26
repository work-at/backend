package com.workat.common.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Aspect
public class TimeCheckAop {

	// 모든 ServiceClass의 public한 Function들의 성능을 측정한다.
    @Around("execution(public * com..*Service..*(..))")
    public Object logger(ProceedingJoinPoint joinPoint) {
        long startTime = System.currentTimeMillis();
        String methodName = joinPoint.getSignature().getDeclaringTypeName();
        Class clazz = joinPoint.getSignature().getDeclaringType();
        try {
            log.info("start {} {}", clazz, methodName);
            return joinPoint.proceed();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        } finally {
            long endTime = System.currentTimeMillis();
            long time = (endTime - startTime);
            log.info("end {} {} - {}(ms)", clazz, methodName, time);
        }
    }
}
