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
            setPrivateField("maxAttempts", 20);
            setPrivateField("fixedInterval", 5000L);
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
        assertEquals(20, retryTemplateConfig.getRetryPolicy().getMaxAttempts());
    }

    @Test
    void testFixedIntervalDefaultValue() {
        // Act
        retryTemplateConfig.retryTemplate();

        // Assert
        assertEquals(5000, retryTemplateConfig.getBackOffPolicy().getBackOffPeriod());
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
        setPrivateField("fixedInterval", 2000L);

        // Act
        retryTemplateConfig.retryTemplate();

        // Assert
        assertEquals(2000, retryTemplateConfig.getBackOffPolicy().getBackOffPeriod());
    }

    private void setPrivateField(String fieldName, Object value) throws Exception {
        Field field = RetryTemplateConfig.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(retryTemplateConfig, value);
    }
}
