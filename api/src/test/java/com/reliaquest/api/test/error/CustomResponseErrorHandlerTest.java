package com.reliaquest.api.test.config;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.reliaquest.api.config.AppLoggerProperties;
import com.reliaquest.api.error.CustomResponseErrorHandler;
import com.reliaquest.api.logger.AppLogger;
import java.io.IOException;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;

class CustomResponseErrorHandlerTest {

    private CustomResponseErrorHandler errorHandler;

    @Mock
    private ClientHttpResponse response;

    @Mock
    private AppLoggerProperties loggerProperties;

    @Mock
    private AppLogger logger; // Mock the AppLogger

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Mock the logger properties to return a specific log level
        when(loggerProperties.getLogLevel()).thenReturn("DEBUG");

        // Create the CustomResponseErrorHandler with mocked properties
        errorHandler = new CustomResponseErrorHandler(loggerProperties);

        // Set the logger to the errorHandler (you may need to adjust the constructor to accept a logger)
        errorHandler = new CustomResponseErrorHandler(logger); // Use the new constructor
    }

    @Test
    void testHasErrorWith4xxStatus() throws IOException {
        // Arrange
        when(response.getStatusCode()).thenReturn(HttpStatus.BAD_REQUEST);

        // Act
        boolean result = errorHandler.hasError(response);

        // Assert
        assertTrue(result);
    }

    @Test
    void testHasErrorWith5xxStatus() throws IOException {
        // Arrange
        when(response.getStatusCode()).thenReturn(HttpStatus.INTERNAL_SERVER_ERROR);

        // Act
        boolean result = errorHandler.hasError(response);

        // Assert
        assertTrue(result);
    }

    @Test
    void testHasErrorWith2xxStatus() throws IOException {
        // Arrange
        when(response.getStatusCode()).thenReturn(HttpStatus.OK);

        // Act
        boolean result = errorHandler.hasError(response);

        // Assert
        assertFalse(result);
    }

    @Test
    void testHandleErrorWith400Status() throws IOException {
        // Arrange
        when(response.getStatusCode()).thenReturn(HttpStatus.BAD_REQUEST);
        when(response.getStatusText()).thenReturn("Bad Request");

        // Act
        errorHandler.handleError(response);

        // Assert
        verify(logger).error(eq("Bad Request"), any(Map.class));
    }

    @Test
    void testHandleErrorWith401Status() throws IOException {
        // Arrange
        when(response.getStatusCode()).thenReturn(HttpStatus.UNAUTHORIZED);
        when(response.getStatusText()).thenReturn("Unauthorized");

        // Act
        errorHandler.handleError(response);

        // Assert
        verify(logger).error(eq("Unauthorized"), any(Map.class));
    }

    @Test
    void testHandleErrorWith403Status() throws IOException {
        // Arrange
        when(response.getStatusCode()).thenReturn(HttpStatus.FORBIDDEN);
        when(response.getStatusText()).thenReturn("Forbidden");

        // Act
        errorHandler.handleError(response);

        // Assert
        verify(logger).error(eq("Forbidden"), any(Map.class));
    }

    @Test
    void testHandleErrorWith404Status() throws IOException {
        // Arrange
        when(response.getStatusCode()).thenReturn(HttpStatus.NOT_FOUND);
        when(response.getStatusText()).thenReturn("Not Found");

        // Act
        errorHandler.handleError(response);

        // Assert
        verify(logger).error(eq("Not Found"), any(Map.class));
    }

    @Test
    void testHandleErrorWith500Status() throws IOException {
        // Arrange
        when(response.getStatusCode()).thenReturn(HttpStatus.INTERNAL_SERVER_ERROR);
        when(response.getStatusText()).thenReturn("Internal Server Error");

        // Act
        errorHandler.handleError(response);

        // Assert
        verify(logger).error(eq("Internal Server Error"), any(Map.class));
    }

    @Test
    void testHandleErrorWithOtherStatus() throws IOException {
        // Arrange
        when(response.getStatusCode()).thenReturn(HttpStatus.SERVICE_UNAVAILABLE);
        when(response.getStatusText()).thenReturn("Service Unavailable");

        // Act
        errorHandler.handleError(response);

        // Assert
        verify(logger).error(eq("Service Unavailable"), any(Map.class)); // Change this line
    }
}
