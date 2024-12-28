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

@Aspect // This annotation specifies that the class is an aspect for dealing with cross-cutting concerns
@Component // This annotation registers the class as a Spring bean, making it eligible for dependency injection
public class ErrorHandlingAspect {

    private final CustomResponseErrorHandler
            errorHandler; // Declaration of a field to hold an instance of CustomResponseErrorHandler

    @Autowired // This annotation enables Spring to inject an instance of CustomResponseErrorHandler into this aspect
    public ErrorHandlingAspect(CustomResponseErrorHandler errorHandler) {
        this.errorHandler = errorHandler; // Assigning the injected instance to the field for use in handling errors
    }

    /**
     * This method is executed after an exception is thrown from any method in the service package.
     *
     * The pointcut expression below defines the conditions under which this advice will be applied.
     *
     * - "execution(public * com.reliaquest.api.service..*(..))":
     *    - "execution": This is a pointcut designator that matches method execution join points.
     *    - "public": This indicates that we are interested in methods that are public.
     *    - "*": This is a wildcard that matches any return type.
     *    - "com.reliaquest.api.service..*": This specifies the package where the methods are located.
     *      The ".." means that we are also matching all sub-packages within the service package.
     *    - "(..)": This indicates that we are matching any method with any number and type of parameters.
     *
     * The "throwing = 'ex'" part specifies that the thrown exception will be accessible in the advice method
     * as the variable 'ex'.
     */
    @AfterThrowing(pointcut = "execution(public * com.reliaquest.api.service..*(..))", throwing = "ex")
    public void handleError(Exception ex) {
        try {
            if (ex instanceof HttpClientErrorException) { // Check if the exception is a client error
                HttpClientErrorException clientError = (HttpClientErrorException) ex; // Cast to client error type
                HttpStatusCode statusCode = clientError.getStatusCode(); // Get the HTTP status code from the exception

                if (statusCode.value() == 429) { // Check for '429 Too Many Requests' status code
                    // Handle 429 Too Many Requests appropriately
                } else {
                    // For other client errors, call the custom error handler
                    errorHandler.handleError(clientError.getResponseBodyAsString(), statusCode);
                }
            } else if (ex instanceof HttpServerErrorException) { // Check if the exception is a server error
                HttpServerErrorException serverError = (HttpServerErrorException) ex; // Cast to server error type
                // Call the custom error handler with server error details
                errorHandler.handleError(serverError.getResponseBodyAsString(), serverError.getStatusCode());
            } else {
                // Handle any other exceptions that are not related to HTTP errors
                // Additional handling logic can be placed here (e.g., logging or rethrowing the exception)
            }
        } catch (IOException e) { // Handle any IOException that might be thrown by the error handler
            // Log the IOException to provide debugging information
            System.err.println("IOException occurred while handling error: " + e.getMessage());
        }
    }
}
