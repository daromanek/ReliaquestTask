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
    @Value("${app.rest.timeout.connect:30}") // Default to 30 seconds if not set
    private long connectTimeout;

    @Value("${app.rest.timeout.read:30}") // Default to 30 seconds if not set
    private long readTimeout;

    /**
     * Bean definition for RestTemplate.
     *
     * @param builder RestTemplateBuilder used to create the RestTemplate instance.
     * @return Configured RestTemplate instance.
     */
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        // Log the configuration process
        logger.debug("Configuring RestTemplate with timeouts");

        // Build and return a RestTemplate with custom settings
        return builder.setConnectTimeout(Duration.ofSeconds(connectTimeout)) // Set connection timeout
                .setReadTimeout(Duration.ofSeconds(readTimeout)) // Set read timeout
                .additionalInterceptors(new LoggingInterceptor()) // Add logging interceptor
                .errorHandler(new CustomResponseErrorHandler()) // Add custom error handler
                .build(); // Build the RestTemplate instance
    }
}
