package com.reliaquest.api.config;

import lombok.Data;
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
@Data
public class RetryTemplateConfig {

    @Value("${app.rest.retry.maxattempts:3}") // Default to 3 if not set
    private int maxAttempts;

    @Value("${app.rest.retry.backoff.initialinterval:1000}") // Default to 1000 ms if not set
    private long initialInterval;

    @Value("${app.rest.retry.backoff.multiplier:2}") // Default to 2 if not set
    private double multiplier;

    @Value("${app.rest.retry.backoff.maxinterval:10000}") // Default to 10000 ms if not set
    private long maxInterval;

    private SimpleRetryPolicy retryPolicy;
    private ExponentialBackOffPolicy backOffPolicy;

    /**
     * Bean definition for RetryTemplate to handle retries for failed requests.
     *
     * @return Configured RetryTemplate instance.
     */
    @Bean
    public RetryTemplate retryTemplate() {
        RetryTemplate retryTemplate = new RetryTemplate();

        // Initialize and configure the retry policy
        this.retryPolicy = new SimpleRetryPolicy();
        this.retryPolicy.setMaxAttempts(maxAttempts);
        retryTemplate.setRetryPolicy(this.retryPolicy);

        // Initialize and configure the backoff policy
        this.backOffPolicy = new ExponentialBackOffPolicy();
        this.backOffPolicy.setInitialInterval(initialInterval);
        this.backOffPolicy.setMultiplier(multiplier);
        this.backOffPolicy.setMaxInterval(maxInterval);
        retryTemplate.setBackOffPolicy(this.backOffPolicy);

        return retryTemplate;
    }
}
