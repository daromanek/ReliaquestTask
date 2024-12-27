package com.reliaquest.api.logger;

import com.reliaquest.api.config.AppLoggerProperties;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

/**
 * Interceptor for logging request and response details.
 */
@Component
public class LoggingInterceptor implements ClientHttpRequestInterceptor {

    // Logger instance for logging requests and responses
    private final AppLogger logger;

    // Constructor to allow dependency injection of the AppLoggerProperties by Spring
    @Autowired
    public LoggingInterceptor(AppLoggerProperties loggerProperties) {
        this.logger = new AppLogger(LoggingInterceptor.class);
        // Set the log level based on the configuration property
        this.logger.setLogLevel(
                AppLogger.LogLevel.valueOf(loggerProperties.getLogLevel().toUpperCase()));
    }

    // Constructor to be used by tests
    public LoggingInterceptor(AppLogger logger) {
        this.logger = logger;
    }

    @Override
    public ClientHttpResponse intercept(
            org.springframework.http.HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
            throws IOException {

        Map<String, Object> requestContext = new HashMap<>();
        requestContext.put("method", request.getMethod());
        requestContext.put("url", request.getURI());
        requestContext.put("headers", request.getHeaders());
        logger.debug("Sending request", requestContext);
        logger.debug("Request body", Map.of("body", new String(body, StandardCharsets.UTF_8)));

        ClientHttpResponse response = execution.execute(request, body);

        Map<String, Object> responseContext = new HashMap<>();
        responseContext.put("statusCode", response.getStatusCode());
        responseContext.put("headers", response.getHeaders());
        logger.debug("Received response", responseContext);

        return response;
    }
}
