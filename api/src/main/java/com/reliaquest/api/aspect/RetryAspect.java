package com.reliaquest.api.aspect;

import com.reliaquest.api.config.RetryTemplateConfig;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class RetryAspect {
    private final RetryTemplate retryTemplate;
    private final RetryTemplateConfig retryTemplateConfig;

    public RetryAspect(RetryTemplate retryTemplate, RetryTemplateConfig retryTemplateConfig) {
        this.retryTemplate = retryTemplate;
        this.retryTemplateConfig = retryTemplateConfig;
    }

    // Pointcut to target all public methods in the com.reliaquest.api.service package
    @Around("execution(public * com.reliaquest.api.model..*(..))")
    public Object retry(ProceedingJoinPoint joinPoint) throws Throwable {
        return retryTemplate.execute(context -> {
            try {
                return joinPoint.proceed(); // Proceed with the original method call
            } catch (Throwable throwable) {
                // If the maximum number of attempts has been reached, throw the original exception
                if (context.getRetryCount() >= retryTemplateConfig.getMaxAttempts() - 1) {
                    throw throwable; // Propagate the original exception after max attempts
                }
                throw new RuntimeException(throwable); // Rethrow the exception for retry logic
            }
        });
    }
}
