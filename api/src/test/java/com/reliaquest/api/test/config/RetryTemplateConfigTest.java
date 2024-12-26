package com.reliaquest.api.test.config;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.reliaquest.api.config.RetryTemplateConfig;
import java.lang.reflect.Field;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.retry.support.RetryTemplate;

class RetryTemplateConfigTest {

    @InjectMocks
    private RetryTemplateConfig retryTemplateConfig;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        try {
            // Set default values directly
            setPrivateField("maxAttempts", 3);
            setPrivateField("initialInterval", 1000L);
            setPrivateField("multiplier", 2.0);
            setPrivateField("maxInterval", 10000L);
        } catch (Exception e) {
            fail("Failed to set up test: " + e.getMessage());
        }
    }

    @Test
    void testRetryTemplateBeanCreation() {
        // Act
        RetryTemplate result = retryTemplateConfig.retryTemplate();

        // Assert
        assertNotNull(result);
    }

    @Test
    void testMaxAttemptsDefaultValue() {
        // Act
        retryTemplateConfig.retryTemplate();

        // Assert
        assertEquals(3, retryTemplateConfig.getRetryPolicy().getMaxAttempts());
    }

    @Test
    void testInitialIntervalDefaultValue() {
        // Act
        retryTemplateConfig.retryTemplate();

        // Assert
        assertEquals(1000, retryTemplateConfig.getBackOffPolicy().getInitialInterval());
    }

    @Test
    void testMultiplierDefaultValue() {
        // Act
        retryTemplateConfig.retryTemplate();

        // Assert
        assertEquals(2, retryTemplateConfig.getBackOffPolicy().getMultiplier());
    }

    @Test
    void testMaxIntervalDefaultValue() {
        // Act
        retryTemplateConfig.retryTemplate();

        // Assert
        assertEquals(10000, retryTemplateConfig.getBackOffPolicy().getMaxInterval());
    }

    @Test
    void testMaxAttemptsValue() throws Exception {
        // Arrange
        setPrivateField("maxAttempts", 5);

        // Act
        retryTemplateConfig.retryTemplate();

        // Assert
        assertEquals(5, retryTemplateConfig.getRetryPolicy().getMaxAttempts());
    }

    @Test
    void testInitialIntervalValue() throws Exception {
        // Arrange
        setPrivateField("initialInterval", 2000L);

        // Act
        retryTemplateConfig.retryTemplate();

        // Assert
        assertEquals(2000, retryTemplateConfig.getBackOffPolicy().getInitialInterval());
    }

    @Test
    void testMultiplierValue() throws Exception {
        // Arrange
        setPrivateField("multiplier", 3.0);

        // Act
        retryTemplateConfig.retryTemplate();

        // Assert
        assertEquals(3, retryTemplateConfig.getBackOffPolicy().getMultiplier());
    }

    @Test
    void testMaxIntervalValue() throws Exception {
        // Arrange
        setPrivateField("maxInterval", 15000L);

        // Act
        retryTemplateConfig.retryTemplate();

        // Assert
        assertEquals(15000, retryTemplateConfig.getBackOffPolicy().getMaxInterval());
    }

    private void setPrivateField(String fieldName, Object value) throws Exception {
        Field field = RetryTemplateConfig.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(retryTemplateConfig, value);
    }
}
