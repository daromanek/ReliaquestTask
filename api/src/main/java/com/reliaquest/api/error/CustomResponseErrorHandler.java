package com.reliaquest.api.error;

import com.reliaquest.api.logger.AppLogger;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

/**
 * Custom error handler for handling HTTP response errors.
 */
public class CustomResponseErrorHandler implements ResponseErrorHandler {

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
        HttpStatusCode statusCode = response.getStatusCode();

        // Use a standard switch statement
        switch (statusCode.value()) {
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
