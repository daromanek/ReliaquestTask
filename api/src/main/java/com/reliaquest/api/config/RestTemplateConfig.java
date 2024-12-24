package com.reliaquest.api.config;

import com.reliaquest.api.logger.AppLogger;
import com.reliaquest.api.logger.LoggingInterceptor;
import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

/**
 * Configuration class for setting up a RestTemplate with custom error handling,
 * logging, and retry capabilities.
 */
@Configuration
@EnableRetry // Enables the retry functionality for methods annotated with @Retryable
public class RestTemplateConfig {

    // Define connection and read timeouts
    private static final Duration CONNECT_TIMEOUT = Duration.ofSeconds(1200);
    private static final Duration READ_TIMEOUT = Duration.ofSeconds(1200);

    // Logger instance for logging events in this configuration class
    private final AppLogger logger = new AppLogger(RestTemplateConfig.class);

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
        return builder.setConnectTimeout(CONNECT_TIMEOUT) // Set connection timeout
                .setReadTimeout(READ_TIMEOUT) // Set read timeout
                .additionalInterceptors(new LoggingInterceptor()) // Add logging interceptor
                .errorHandler(new CustomResponseErrorHandler()) // Add custom error handler
                .build(); // Build the RestTemplate instance
    }

    /**
     * Custom error handler for handling HTTP response errors.
     */
    public static class CustomResponseErrorHandler implements ResponseErrorHandler {

        // Logger instance for logging errors in this handler
        private final AppLogger logger = new AppLogger(CustomResponseErrorHandler.class);

        /**
         * Check if the response has an error based on its status code.
         *
         * @param response The ClientHttpResponse to check.
         * @return true if the response indicates an error, false otherwise.
         * @throws IOException if an I/O error occurs.
         */
        @Override
        public boolean hasError(ClientHttpResponse response) throws IOException {
            // Return true for 4xx and 5xx status codes
            return response.getStatusCode().is4xxClientError()
                    || response.getStatusCode().is5xxServerError();
        }

        /**
         * Handle the error response by logging the error details.
         *
         * @param response The ClientHttpResponse that contains the error.
         * @throws IOException if an I/O error occurs.
         */
        @Override
        public void handleError(ClientHttpResponse response) throws IOException {
            // Create a context map to hold error details
            Map<String, Object> context = new HashMap<>();
            context.put("statusCode", response.getStatusCode());
            context.put("statusText", response.getStatusText());

            // Get the status code as HttpStatusCode
            HttpStatusCode statusCode = response.getStatusCode(); // Get the status code

            // Use a standard switch statement
            switch (statusCode.value()) { // Use the value() method to get the int value
                case 400: // BAD_REQUEST
                    logger.error("Bad Request", context);
                    break;
                case 401: // UNAUTHORIZED
                    logger.error("Unauthorized", context);
                    break;
                case 403: // FORBIDDEN
                    logger.error("Forbidden", context);
                    break;
                case 404: // NOT_FOUND
                    logger.error("Not Found", context);
                    break;
                case 500: // INTERNAL_SERVER_ERROR
                    logger.error("Internal Server Error", context);
                    break;
                default:
                    logger.error("Response error occurred", context);
            }
        }
    }
}
