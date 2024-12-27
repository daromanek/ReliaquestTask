package com.reliaquest.api.error;

import com.reliaquest.api.config.AppLoggerProperties;
import com.reliaquest.api.logger.AppLogger;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
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
                break;
            case 401:
                logger.error("Unauthorized", context);
                break;
            case 403:
                logger.error("Forbidden", context);
                break;
            case 404:
                logger.error("Not Found", context);
                break;
            case 408:
                logger.error("Request Timeout", context);
                break;
            case 429:
                logger.error("Too Many Requests", context);
                break;
            case 500:
                logger.error("Internal Server Error", context);
                break;
            case 502:
                logger.error("Bad Gateway", context);
                break;
            case 503:
                logger.error("Service Unavailable", context);
                break;
            case 504:
                logger.error("Gateway Timeout", context);
                break;
            default:
                logger.error("Response error occurred", context);
        }
    }

    public void handleError(String responseBody, HttpStatusCode statusCode) throws IOException {
        Map<String, Object> context = new HashMap<>();
        context.put("statusCode", statusCode);
        context.put("statusText", statusCode.getReasonPhrase());

        switch (statusCode.value()) {
            case 400:
                logger.error("Bad Request: " + responseBody, context);
                break;
            case 401:
                logger.error("Unauthorized: " + responseBody, context);
                break;
            case 403:
                logger.error("Forbidden: " + responseBody, context);
                break;
            case 404:
                logger.error("Not Found: " + responseBody, context);
                break;
            case 408:
                logger.error("Request Timeout: " + responseBody, context);
                break;
            case 429:
                logger.error("Too Many Requests: " + responseBody, context);
                break;
            case 500:
                logger.error("Internal Server Error: " + responseBody, context);
                break;
            case 502:
                logger.error("Bad Gateway: " + responseBody, context);
                break;
            case 503:
                logger.error("Service Unavailable: " + responseBody, context);
                break;
            case 504:
                logger.error("Gateway Timeout: " + responseBody, context);
                break;
            default:
                logger.error("Response error occurred: " + responseBody, context);
        }
    }
}
