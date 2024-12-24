package com.reliaquest.api.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class RetryAspect {
    private final RetryTemplate retryTemplate;

    public RetryAspect(RetryTemplate retryTemplate) {
        this.retryTemplate = retryTemplate;
    }

    // Pointcut to target all public methods in the com.reliaquest.api.service package
    @Around("execution(public * com.reliaquest.api.service..*(..))")
    public Object retry(ProceedingJoinPoint joinPoint) throws Throwable {
        return retryTemplate.execute(context -> {
            try {
                return joinPoint.proceed(); // Proceed with the original method call
            } catch (Throwable throwable) {
                throw new RuntimeException(throwable); // Rethrow the exception for retry logic
            }
        });
    }
}
