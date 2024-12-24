package com.reliaquest.api.aspect;

import com.reliaquest.api.error.CustomResponseErrorHandler;
import java.io.IOException;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ErrorHandlingAspect {

    private final CustomResponseErrorHandler errorHandler = new CustomResponseErrorHandler();

    // Pointcut to target all public methods in the com.reliaquest.api.service package
    @AfterThrowing(pointcut = "execution(public * com.reliaquest.api.service..*(..))", throwing = "response")
    public void handleError(ClientHttpResponse response) throws IOException {
        errorHandler.handleError(response);
    }
}
