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
                        // For 429 error codes it is a standard practice to include a response header "Retry-After"
                        // instructing
                        //   the caller that they can try their call again after X seconds.  However, I know the server
                        // app in this
                        //   exercise did not include that header but I wanted to leave the code in to show how I would
                        // handle that
                        //   in the case where it was present
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
                        } else {
                            // In retrospect I should probably have added a sleep here to make the retryTemplate wait
                            //   for 90 seconds, to handle the longest random BackOff time enforced at the server,
                            // before
                            //   it started retrying and then I would have set the BackoffPolicy on the RetryTemplate to
                            //   be something much more reasonable for common retry scenarios.
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
