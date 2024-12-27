package com.reliaquest.api.aspect;

import com.reliaquest.api.error.CustomResponseErrorHandler;
import java.io.IOException;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ErrorHandlingAspect {

    private final CustomResponseErrorHandler errorHandler;

    // Constructor injection of CustomResponseErrorHandler
    @Autowired
    public ErrorHandlingAspect(CustomResponseErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    // Pointcut to target all public methods in the com.reliaquest.api.service package
    @AfterThrowing(pointcut = "execution(public * com.reliaquest.api.service..*(..))", throwing = "response")
    public void handleError(ClientHttpResponse response) throws IOException {
        errorHandler.handleError(response);
    }
}
