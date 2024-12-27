package com.reliaquest.api.config;

import com.reliaquest.api.error.CustomResponseErrorHandler;
import com.reliaquest.api.logger.AppLogger;
import com.reliaquest.api.logger.LoggingInterceptor;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.web.client.RestTemplate;

/**
 * Configuration class for setting up a RestTemplate with custom error handling,
 * logging, and retry capabilities.
 */
@Configuration
@EnableRetry // Enables the retry functionality for methods annotated with @Retryable
public class RestTemplateConfig {

    // Logger instance for logging events in this configuration class
    private final AppLogger logger = new AppLogger(RestTemplateConfig.class);

    // Define connection and read timeouts
    @Value("${app.rest.timeout.connect:10}") // Default to 10 seconds if not set as we want connects to fail quickly
    private long connectTimeout;

    @Value("${app.rest.timeout.read:120}") // Default to 120 seconds if not set as the max rate limit backoff period is
    // 90 seconds so this will cover that successfully
    private long readTimeout;

    /**
     * Bean definition for RestTemplate.
     *
     * @param builder RestTemplateBuilder used to create the RestTemplate instance.
     * @param errorHandler CustomResponseErrorHandler bean for handling errors.
     * @param loggingInterceptor LoggingInterceptor bean for logging requests and responses.
     * @return Configured RestTemplate instance.
     */
    @Bean
    public RestTemplate restTemplate(
            RestTemplateBuilder builder,
            CustomResponseErrorHandler errorHandler,
            LoggingInterceptor loggingInterceptor) {
        // Log the configuration process
        logger.debug("Configuring RestTemplate with timeouts");

        // Build and return a RestTemplate with custom settings
        return builder.setConnectTimeout(Duration.ofSeconds(connectTimeout)) // Set connection timeout
                .setReadTimeout(Duration.ofSeconds(readTimeout)) // Set read timeout
                .additionalInterceptors(loggingInterceptor) // Add logging interceptor
                .errorHandler(errorHandler) // Use the injected custom error handler
                .build(); // Build the RestTemplate instance
    }
}
