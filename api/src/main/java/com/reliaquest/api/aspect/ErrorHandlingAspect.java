package com.reliaquest.api.aspect;

import com.reliaquest.api.error.CustomResponseErrorHandler;
import java.io.IOException;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

@Aspect
@Component
public class ErrorHandlingAspect {

    private final CustomResponseErrorHandler errorHandler;

    @Autowired
    public ErrorHandlingAspect(CustomResponseErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    @AfterThrowing(pointcut = "execution(public * com.reliaquest.api.service..*(..))", throwing = "ex")
    public void handleError(Exception ex) {
        try {
            if (ex instanceof HttpClientErrorException) {
                HttpClientErrorException clientError = (HttpClientErrorException) ex;
                HttpStatusCode statusCode = clientError.getStatusCode();

                if (statusCode.value() == 429) {
                    // Handle 429 Too Many Requests
                    System.out.println("Rate limit exceeded. Please try again later.");
                    // Implement backoff strategy or logging as needed
                } else {
                    errorHandler.handleError(clientError.getResponseBodyAsString(), statusCode);
                }
            } else if (ex instanceof HttpServerErrorException) {
                HttpServerErrorException serverError = (HttpServerErrorException) ex;
                errorHandler.handleError(serverError.getResponseBodyAsString(), serverError.getStatusCode());
            } else {
                // Handle other exceptions as needed
                // You might want to log or rethrow them
            }
        } catch (IOException e) {
            // Handle the IOException, for example, log it
            System.err.println("IOException occurred while handling error: " + e.getMessage());
        }
    }
}
