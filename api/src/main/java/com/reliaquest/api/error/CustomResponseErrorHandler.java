package com.reliaquest.api.error;

import com.reliaquest.api.config.AppLoggerProperties;
import com.reliaquest.api.logger.AppLogger;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResponseErrorHandler;

@Component
public class CustomResponseErrorHandler implements ResponseErrorHandler {

    private final AppLogger logger;

    @Autowired
    public CustomResponseErrorHandler(AppLoggerProperties loggerProperties) {
        this.logger = new AppLogger(CustomResponseErrorHandler.class);
        this.logger.setLogLevel(
                AppLogger.LogLevel.valueOf(loggerProperties.getLogLevel().toUpperCase()));
    }

    public CustomResponseErrorHandler(AppLogger logger) {
        this.logger = logger;
    }

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return response.getStatusCode().is4xxClientError()
                || response.getStatusCode().is5xxServerError();
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        Map<String, Object> context = new HashMap<>();
        context.put("statusCode", response.getStatusCode());
        context.put("statusText", response.getStatusText());

        HttpStatusCode statusCode = response.getStatusCode();

        switch (statusCode.value()) {
            case 400:
                logger.error("Bad Request", context);
                // Do not throw an exception; do not retry
                break;
            case 401:
                logger.error("Unauthorized", context);
                // Do not throw an exception; do not retry
                break;
            case 403:
                logger.error("Forbidden", context);
                // Do not throw an exception; do not retry
                break;
            case 404:
                logger.error("Not Found", context);
                // Do not throw an exception; do not retry
                break;
            case 408:
                logger.error("Request Timeout", context);
                // Consider throwing an exception if you want to retry
                throw new HttpClientErrorException(statusCode);
            case 429:
                logger.error("Too Many Requests", context);
                // Throw an exception to trigger the retry logic
                throw new HttpClientErrorException(statusCode);
            case 500:
                logger.error("Internal Server Error", context);
                // Throw an exception to trigger the retry logic
                throw new HttpClientErrorException(statusCode);
            case 502:
                logger.error("Bad Gateway", context);
                // Throw an exception to trigger the retry logic
                throw new HttpClientErrorException(statusCode);
            case 503:
                logger.error("Service Unavailable", context);
                // Throw an exception to trigger the retry logic
                throw new HttpClientErrorException(statusCode);
            case 504:
                logger.error("Gateway Timeout", context);
                // Throw an exception to trigger the retry logic
                throw new HttpClientErrorException(statusCode);
            default:
                logger.error("Response error occurred: ", context);
                // Do not throw an exception; do not retry
                break;
        }
    }

    public void handleError(String responseBody, HttpStatusCode statusCode) throws IOException {
        Map<String, Object> context = new HashMap<>();
        context.put("statusCode", statusCode);
        context.put("statusText", HttpStatus.valueOf(statusCode.value()).getReasonPhrase());

        switch (statusCode.value()) {
            case 400:
                logger.error("Bad Request: " + responseBody, context);
                // Do not throw an exception; do not retry
                break;
            case 401:
                logger.error("Unauthorized: " + responseBody, context);
                // Do not throw an exception; do not retry
                break;
            case 403:
                logger.error("Forbidden: " + responseBody, context);
                // Do not throw an exception; do not retry
                break;
            case 404:
                logger.error("Not Found: " + responseBody, context);
                // Do not throw an exception; do not retry
                break;
            case 408:
                logger.error("Request Timeout: " + responseBody, context);
                // Consider throwing an exception if you want to retry
                throw new HttpClientErrorException(statusCode);
            case 429:
                logger.error("Too Many Requests: " + responseBody, context);
                // Throw an exception to trigger the retry logic
                throw new HttpClientErrorException(statusCode);
            case 500:
                logger.error("Internal Server Error: " + responseBody, context);
                // Throw an exception to trigger the retry logic
                throw new HttpClientErrorException(statusCode);
            case 502:
                logger.error("Bad Gateway: " + responseBody, context);
                // Throw an exception to trigger the retry logic
                throw new HttpClientErrorException(statusCode);
            case 503:
                logger.error("Service Unavailable: " + responseBody, context);
                // Throw an exception to trigger the retry logic
                throw new HttpClientErrorException(statusCode);
            case 504:
                logger.error("Gateway Timeout: " + responseBody, context);
                // Throw an exception to trigger the retry logic
                throw new HttpClientErrorException(statusCode);
            default:
                logger.error("Response error occurred: " + responseBody, context);
                // Do not throw an exception; do not retry
                break;
        }
    }
}
