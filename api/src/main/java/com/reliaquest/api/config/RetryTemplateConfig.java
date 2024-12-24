package com.reliaquest.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

/**
 * Configuration class for setting up a RetryTemplate.
 */
@Configuration
public class RetryTemplateConfig {

    // Injecting values from application.yml
    @Value("${app.rest.retry.maxattempts:3}") // Default to 3 if not set
    private int maxAttempts;

    @Value("${app.rest.retry.backoff.initialinterval:1000}") // Default to 1000 ms if not set
    private long initialInterval;

    @Value("${app.rest.retry.backoff.multiplier:2}") // Default to 2 if not set
    private double multiplier;

    @Value("${app.rest.retry.backoff.maxinterval:10000}") // Default to 10000 ms if not set
    private long maxInterval;

    /**
     * Bean definition for RetryTemplate to handle retries for failed requests.
     *
     * @return Configured RetryTemplate instance.
     */
    @Bean
    public RetryTemplate retryTemplate() {
        // Create a new RetryTemplate instance
        RetryTemplate retryTemplate = new RetryTemplate();

        // Define a simple retry policy with a maximum number of attempts
        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(maxAttempts); // Set maximum attempts from yml
        retryTemplate.setRetryPolicy(retryPolicy); // Set the retry policy

        // Define an exponential backoff policy for retries
        ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
        backOffPolicy.setInitialInterval(initialInterval); // Set initial wait time from yml
        backOffPolicy.setMultiplier(multiplier); // Set exponential multiplier from yml
        backOffPolicy.setMaxInterval(maxInterval); // Set maximum wait time from yml
        retryTemplate.setBackOffPolicy(backOffPolicy); // Set the backoff policy

        return retryTemplate; // Return the configured RetryTemplate
    }
}
