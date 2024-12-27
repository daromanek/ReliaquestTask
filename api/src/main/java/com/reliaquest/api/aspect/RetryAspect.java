package com.reliaquest.api.aspect;

import com.reliaquest.api.config.RetryTemplateConfig;
import java.util.concurrent.TimeUnit;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

@Aspect
@Component
public class RetryAspect {
    private final RetryTemplate retryTemplate;
    private final RetryTemplateConfig retryTemplateConfig;

    public RetryAspect(RetryTemplate retryTemplate, RetryTemplateConfig retryTemplateConfig) {
        this.retryTemplate = retryTemplate;
        this.retryTemplateConfig = retryTemplateConfig;
    }

    @Around("execution(public * com.reliaquest.api.service..*(..))")
    public Object retry(ProceedingJoinPoint joinPoint) throws Throwable {
        return retryTemplate.execute(context -> {
            try {
                return joinPoint.proceed(); // Proceed with the original method call
            } catch (Throwable throwable) {
                if (throwable instanceof HttpClientErrorException) {
                    HttpClientErrorException clientError = (HttpClientErrorException) throwable;
                    if (clientError.getStatusCode().value() == 429) {
                        // Handle rate limiting
                        String retryAfterHeader =
                                clientError.getResponseHeaders().getFirst("Retry-After");
                        if (retryAfterHeader != null) {
                            // Parse the Retry-After header
                            try {
                                long waitTime = Long.parseLong(retryAfterHeader) * 1000; // Convert to milliseconds
                                // Wait for the specified time before throwing the exception
                                TimeUnit.MILLISECONDS.sleep(waitTime);
                            } catch (NumberFormatException ex) {
                                // If parsing fails, we won't wait because we don't know how long to wait for
                            }
                        }

                        // If this is the last retry attempt, throw the original exception
                        if (context.getRetryCount() >= retryTemplateConfig.getMaxAttempts() - 1) {
                            throw throwable; // Propagate the original exception after max attempts
                        }
                        throw new RuntimeException(throwable); // Rethrow to trigger retry logic
                    }
                }

                // If the maximum number of attempts has been reached, throw the original exception
                if (context.getRetryCount() >= retryTemplateConfig.getMaxAttempts() - 1) {
                    throw throwable; // Propagate the original exception after max attempts
                }
                throw new RuntimeException(throwable); // Rethrow the exception for retry logic
            }
        });
    }
}
