package com.reliaquest.api.aspect;

import com.reliaquest.api.config.RetryTemplateConfig;
import java.util.concurrent.TimeUnit;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

// This annotation indicates that the class is an Aspect in Aspect-Oriented Programming (AOP) in Spring.
@Aspect
// This annotation indicates that this class is a Spring component, making it eligible for component scanning and
// dependency injection.
@Component
public class RetryAspect {
    // This instance will be used to manage retry logic.
    private final RetryTemplate retryTemplate;
    // This configuration object contains settings related to the retry logic.
    private final RetryTemplateConfig retryTemplateConfig;

    // Constructor that initializes the RetryTemplate and RetryTemplateConfig.
    public RetryAspect(RetryTemplate retryTemplate, RetryTemplateConfig retryTemplateConfig) {
        this.retryTemplate = retryTemplate;
        this.retryTemplateConfig = retryTemplateConfig;
    }

    // The @Around annotation indicates that this method will run "around" the method matched by the pointcut.
    // The pointcut expression below specifies which methods to intercept.
    // Specifically, it targets any public method within any class located in the com.reliaquest.api.service package or
    // any of its sub-packages.
    @Around("execution(public * com.reliaquest.api.service..*(..))")
    public Object retry(ProceedingJoinPoint joinPoint) throws Throwable {
        // Use the retryTemplate to execute the code with retry logic.
        return retryTemplate.execute(context -> {
            try {
                // Proceed with the original method call.
                return joinPoint.proceed();
            } catch (Throwable throwable) {
                // Handle exceptions that occur during the method execution.
                if (throwable instanceof HttpClientErrorException) {
                    // Cast the throwable to HttpClientErrorException for specific handling.
                    HttpClientErrorException clientError = (HttpClientErrorException) throwable;
                    // Check if the error status code is 429 (Too Many Requests), indicating rate limiting.
                    if (clientError.getStatusCode().value() == 429) {
                        // Attempt to retrieve the "Retry-After" header value from the response.
                        String retryAfterHeader =
                                clientError.getResponseHeaders().getFirst("Retry-After");
                        if (retryAfterHeader != null) {
                            // Parse the "Retry-After" header value to determine how long to wait.
                            try {
                                long waitTime =
                                        Long.parseLong(retryAfterHeader) * 1000; // Convert seconds to milliseconds
                                // Wait for the specified time before retrying.
                                TimeUnit.MILLISECONDS.sleep(waitTime);
                            } catch (NumberFormatException ex) {
                                // If parsing fails, we will not wait since the duration is unknown.
                            }
                        } else {
                            // Commenting here as a note for improvement: We could have added a default wait time here.
                        }

                        // If this is the last retry attempt, throw the original exception.
                        if (context.getRetryCount() >= retryTemplateConfig.getMaxAttempts() - 1) {
                            throw throwable;
                        }
                        // If not the last attempt, rethrow the exception wrapped in a RuntimeException to trigger retry
                        // logic.
                        throw new RuntimeException(throwable);
                    }
                }

                // For other exceptions, check if maximum retry attempts have been reached.
                if (context.getRetryCount() >= retryTemplateConfig.getMaxAttempts() - 1) {
                    throw throwable; // Propagate the original exception after reaching max attempts.
                }
                // Rethrow the exception for retry logic in case of non-HTTP errors.
                throw new RuntimeException(throwable);
            }
        });
    }
}
