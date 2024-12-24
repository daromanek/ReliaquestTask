package com.reliaquest.api.config;

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

    /**
     * Bean definition for RetryTemplate to handle retries for failed requests.
     *
     * @return Configured RetryTemplate instance.
     */
    @Bean
    public RetryTemplate retryTemplate() {
        // Create a new RetryTemplate instance
        RetryTemplate retryTemplate = new RetryTemplate();

        // Define a simple retry policy with a maximum of 3 attempts
        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(3); // Set maximum attempts
        retryTemplate.setRetryPolicy(retryPolicy); // Set the retry policy

        // Define an exponential backoff policy for retries
        ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
        backOffPolicy.setInitialInterval(1000); // Initial wait time of 1 second
        backOffPolicy.setMultiplier(2.0); // Exponential multiplier
        backOffPolicy.setMaxInterval(10000); // Maximum wait time of 10 seconds
        retryTemplate.setBackOffPolicy(backOffPolicy); // Set the backoff policy

        return retryTemplate; // Return the configured RetryTemplate
    }
}
