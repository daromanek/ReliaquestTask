package com.reliaquest.api.test.config;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.reliaquest.api.config.RestTemplateConfig;
import com.reliaquest.api.error.CustomResponseErrorHandler;
import com.reliaquest.api.logger.LoggingInterceptor;
import java.lang.reflect.Field;
import java.time.Duration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

class RestTemplateConfigTest {

    @InjectMocks
    private RestTemplateConfig restTemplateConfig;

    @Mock
    private RestTemplateBuilder restTemplateBuilder;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(restTemplateBuilder.setConnectTimeout(any(Duration.class))).thenReturn(restTemplateBuilder);
        when(restTemplateBuilder.setReadTimeout(any(Duration.class))).thenReturn(restTemplateBuilder);
        when(restTemplateBuilder.additionalInterceptors(any(LoggingInterceptor.class)))
                .thenReturn(restTemplateBuilder);
        when(restTemplateBuilder.errorHandler(any(CustomResponseErrorHandler.class)))
                .thenReturn(restTemplateBuilder);
        when(restTemplateBuilder.build()).thenReturn(restTemplate);
    }

    @Test
    void testRestTemplateBeanCreation() {
        // Act
        RestTemplate result = restTemplateConfig.restTemplate(restTemplateBuilder);

        // Assert
        assertNotNull(result);
        verify(restTemplateBuilder).setConnectTimeout(any(Duration.class));
        verify(restTemplateBuilder).setReadTimeout(any(Duration.class));
        verify(restTemplateBuilder).additionalInterceptors(any(LoggingInterceptor.class));
        verify(restTemplateBuilder).errorHandler(any(CustomResponseErrorHandler.class));
        verify(restTemplateBuilder).build();
    }

    @Test
    void testConnectTimeout() throws Exception {
        // Arrange
        long expectedConnectTimeout = 30; // Default value
        setPrivateField("connectTimeout", expectedConnectTimeout);

        // Act
        restTemplateConfig.restTemplate(restTemplateBuilder);

        // Assert
        verify(restTemplateBuilder).setConnectTimeout(Duration.ofSeconds(expectedConnectTimeout));
    }

    @Test
    void testReadTimeout() throws Exception {
        // Arrange
        long expectedReadTimeout = 30; // Default value
        setPrivateField("readTimeout", expectedReadTimeout);

        // Act
        restTemplateConfig.restTemplate(restTemplateBuilder);

        // Assert
        verify(restTemplateBuilder).setReadTimeout(Duration.ofSeconds(expectedReadTimeout));
    }

    @Test
    void testLoggingInterceptorAdded() {
        // Act
        restTemplateConfig.restTemplate(restTemplateBuilder);

        // Assert
        verify(restTemplateBuilder).additionalInterceptors(any(LoggingInterceptor.class));
    }

    @Test
    void testCustomErrorHandlerAdded() {
        // Act
        restTemplateConfig.restTemplate(restTemplateBuilder);

        // Assert
        verify(restTemplateBuilder).errorHandler(any(CustomResponseErrorHandler.class));
    }

    @Test
    void testLoggerInitialization() {
        // Act
        RestTemplate result = restTemplateConfig.restTemplate(restTemplateBuilder);

        // Assert
        assertNotNull(result);
    }

    @Test
    void testDefaultTimeoutValues() throws Exception {
        // Arrange
        setPrivateField("connectTimeout", 30);
        setPrivateField("readTimeout", 30);

        // Act
        RestTemplate result = restTemplateConfig.restTemplate(restTemplateBuilder);

        // Assert
        assertNotNull(result);
        verify(restTemplateBuilder).setConnectTimeout(Duration.ofSeconds(30));
        verify(restTemplateBuilder).setReadTimeout(Duration.ofSeconds(30));
    }

    private void setPrivateField(String fieldName, long value) throws Exception {
        Field field = RestTemplateConfig.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.setLong(restTemplateConfig, value);
    }
}
