package com.reliaquest.api.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

/**
 * Configuration class for setting up a RetryTemplate.
 */
@Configuration
@Data
public class RetryTemplateConfig {

    // I went with the FixedBackOffPolicy as I didn't have time to create my own BackOffPolicy that would have allowed
    // for an initial one or two quicker retries to handle non-rate limiting
    //  errors while providing for intervals that were around 10ish seconds to avoid burdening the server when rate
    // limiting is in effecct but still occuring often enough to get done asap
    //  based on how long the rate limiting random interval was set to
    @Value("${app.rest.retry.maxattempts:20}") // Default to 20 if not set
    private int maxAttempts;

    @Value("${app.rest.retry.backoff.fixedinterval:5000}") // Fixed backoff interval of 5 seconds
    private long fixedInterval;

    private SimpleRetryPolicy retryPolicy;
    private FixedBackOffPolicy backOffPolicy;

    @Bean
    public RetryTemplate retryTemplate() {
        RetryTemplate retryTemplate = new RetryTemplate();

        // Initialize and configure the retry policy
        retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(maxAttempts);
        retryTemplate.setRetryPolicy(retryPolicy);

        // Initialize and configure the fixed backoff policy
        backOffPolicy = new FixedBackOffPolicy();
        backOffPolicy.setBackOffPeriod(fixedInterval); // Set the fixed interval
        retryTemplate.setBackOffPolicy(backOffPolicy);

        return retryTemplate;
    }

    public SimpleRetryPolicy getRetryPolicy() {
        return retryPolicy;
    }

    public FixedBackOffPolicy getBackOffPolicy() {
        return backOffPolicy;
    }
}
