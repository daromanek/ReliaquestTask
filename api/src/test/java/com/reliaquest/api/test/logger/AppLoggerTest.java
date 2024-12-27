package com.reliaquest.api.test.logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.reliaquest.api.logger.AppLogger;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.MDC;

class AppLoggerTest {
    private Logger mockLogger;
    private AppLogger appLogger;

    @BeforeEach
    void setUp() {
        mockLogger = mock(Logger.class);
        appLogger = new AppLogger(mockLogger);
    }

    @Test
    void testDefaultLogLevel() {
        // Assert
        assertEquals(AppLogger.LogLevel.DEBUG, appLogger.getLogLevel());
    }

    @Test
    void testSetLogLevel() {
        // Act
        appLogger.setLogLevel(AppLogger.LogLevel.INFO);

        // Assert
        assertEquals(AppLogger.LogLevel.INFO, appLogger.getLogLevel());
    }

    @Test
    void testInfoLogging() {
        // Act
        appLogger.info("Info message");

        // Assert
        verify(mockLogger).info("Info message");
    }

    @Test
    void testInfoLoggingAtLowerLogLevel() {
        // Arrange
        appLogger.setLogLevel(AppLogger.LogLevel.WARN);

        // Act
        appLogger.info("Info message");

        // Assert: No interaction should occur if log level is too low
        verify(mockLogger, never()).info(anyString());
    }

    @Test
    void testDebugLogging() {
        // Act
        appLogger.debug("Debug message");

        // Assert
        verify(mockLogger).debug("Debug message");
    }

    @Test
    void testWarnLogging() {
        // Act
        appLogger.warn("Warning message");

        // Assert
        verify(mockLogger).warn("Warning message");
    }

    @Test
    void testErrorLogging() {
        // Act
        appLogger.error("Error message");

        // Assert
        verify(mockLogger).error("Error message");
    }

    @Test
    void testErrorLoggingWithThrowable() {
        // Arrange
        Throwable throwable = new RuntimeException("Test exception");

        // Act
        appLogger.error("Error message", throwable);

        // Assert
        verify(mockLogger).error("Error message", throwable);
    }

    @Test
    void testInfoLoggingWithContext() {
        // Arrange
        Map<String, Object> context = new HashMap<>();
        context.put("key1", "value1");
        context.put("key2", "value2");

        // Act
        appLogger.info("Info message", context);

        // Assert
        verify(mockLogger).info(contains("Info message | key1=value1, key2=value2"));
    }

    @Test
    void testDebugLoggingWithContext() {
        // Arrange
        Map<String, Object> context = new HashMap<>();
        context.put("key1", "value1");

        // Act
        appLogger.debug("Debug message", context);

        // Assert
        verify(mockLogger).debug(contains("Debug message | key1=value1"));
    }

    @Test
    void testWarnLoggingWithContext() {
        // Arrange
        Map<String, Object> context = new HashMap<>();
        context.put("key1", "value2");

        // Act
        appLogger.warn("Warning message", context);

        // Assert
        verify(mockLogger).warn(contains("Warning message | key1=value2"));
    }

    @Test
    void testErrorLoggingWithThrowableAndContext() {
        // Arrange
        Throwable throwable = new RuntimeException("Test exception");
        Map<String, Object> context = new HashMap<>();
        context.put("key1", "value1");

        // Act
        appLogger.error("Error message", throwable, context);

        // Assert
        verify(mockLogger).error(contains("Error message | key1=value1"), eq(throwable));
    }

    // Removed the testShouldLog method

    @Test
    void testMDCLoggingWithContext() {
        // Arrange
        Map<String, Object> context = new HashMap<>();
        context.put("foo", "bar");
        MDC.put("mdcKey", "mdcValue");

        // Act
        appLogger.info("Info message with MDC", context);

        // Assert
        verify(mockLogger).info(contains("Info message with MDC | MDC: mdcKey=mdcValue | foo=bar"));
        MDC.clear(); // Clean up MDC after test
    }
}
