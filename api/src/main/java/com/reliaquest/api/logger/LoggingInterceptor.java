package com.reliaquest.api.logger;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

/**
 * Interceptor for logging request and response details.
 */
public class LoggingInterceptor implements ClientHttpRequestInterceptor {

    // Logger instance for logging requests and responses
    private final AppLogger logger = new AppLogger(LoggingInterceptor.class);

    /**
     * Intercept the HTTP request and log its details.
     *
     * @param request The HTTP request to intercept.
     * @param body The request body.
     * @param execution The request execution context.
     * @return The ClientHttpResponse received from the execution.
     * @throws IOException if an I/O error occurs.
     */
    @Override
    public ClientHttpResponse intercept(
            org.springframework.http.HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
            throws IOException {

        // Create a context map for the request details
        Map<String, Object> requestContext = new HashMap<>();
        requestContext.put("method", request.getMethod());
        requestContext.put("url", request.getURI());
        requestContext.put("headers", request.getHeaders());
        logger.debug("Sending request", requestContext); // Log request details
        logger.debug("Request body", Map.of("body", new String(body, StandardCharsets.UTF_8))); // Log request body

        // Execute the request and get the response
        ClientHttpResponse response = execution.execute(request, body);

        // Create a context map for the response details
        Map<String, Object> responseContext = new HashMap<>();
        responseContext.put("statusCode", response.getStatusCode());
        responseContext.put("headers", response.getHeaders());
        logger.debug("Received response", responseContext); // Log response details

        return response; // Return the response
    }
}
