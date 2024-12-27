package com.reliaquest.api.test.logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.reliaquest.api.logger.AppLogger;
import com.reliaquest.api.logger.LoggingInterceptor;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpResponse;

@ExtendWith(MockitoExtension.class)
class LoggingInterceptorTest {

    @Mock
    private AppLogger logger; // Mock the logger

    //    @Mock
    //    private AppLoggerProperties loggerProperties; // Mock the AppLoggerProperties

    private LoggingInterceptor loggingInterceptor; // Use Spring to inject the interceptor

    @Mock
    private ClientHttpRequestExecution execution;

    @Mock
    private ClientHttpResponse response;

    private HttpHeaders headers;

    @BeforeEach
    void setUp() {
        headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        // Set up the logger properties mock to return a valid log level
        //        when(loggerProperties.getLogLevel()).thenReturn("DEBUG");

        // Manually instantiate the LoggingInterceptor with the mocked logger
        loggingInterceptor = new LoggingInterceptor(logger);
    }

    @Test
    void testInterceptLogsRequestAndResponse() throws IOException {
        // Arrange
        URI uri = URI.create("http://localhost/test");
        byte[] body = "{\"key\":\"value\"}".getBytes(StandardCharsets.UTF_8);
        when(execution.execute(any(), any(byte[].class))).thenReturn(response);
        when(response.getStatusCode()).thenReturn(HttpStatus.OK);
        when(response.getHeaders()).thenReturn(headers);

        // Act
        loggingInterceptor.intercept(new MockHttpRequest(HttpMethod.POST, uri, headers), body, execution);

        // Assert
        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
        //        ArgumentCaptor<Map<String, Object>> requestCaptor = ArgumentCaptor.forClass(Map.class);
        //        ArgumentCaptor<Map<String, Object>> responseCaptor = ArgumentCaptor.forClass(Map.class);
        ArgumentCaptor<Map<String, Object>> contextCaptor = ArgumentCaptor.forClass(Map.class);

        // Expect 3 calls to logger.debug
        verify(logger, times(3)).debug(messageCaptor.capture(), contextCaptor.capture());

        // Verify request logs
        assertEquals("Sending request", messageCaptor.getAllValues().get(0));
        assertEquals(HttpMethod.POST, contextCaptor.getAllValues().get(0).get("method"));
        assertEquals(
                "http://localhost/test",
                contextCaptor.getAllValues().get(0).get("url").toString());
        assertEquals(headers, contextCaptor.getAllValues().get(0).get("headers"));

        // Verify request body log
        assertEquals("Request body", messageCaptor.getAllValues().get(1)); // Check for request body log

        // Verify response logs
        assertEquals("Received response", messageCaptor.getAllValues().get(2));
        assertEquals(HttpStatus.OK, contextCaptor.getAllValues().get(2).get("statusCode"));
        assertEquals(headers, contextCaptor.getAllValues().get(2).get("headers"));
    }

    @Test
    void testInterceptWithEmptyBody() throws IOException {
        // Arrange
        URI uri = URI.create("http://localhost/test");
        byte[] body = new byte[0]; // Empty body
        when(execution.execute(any(), any(byte[].class))).thenReturn(response);
        when(response.getStatusCode()).thenReturn(HttpStatus.BAD_REQUEST);
        when(response.getHeaders()).thenReturn(headers);

        // Act
        loggingInterceptor.intercept(new MockHttpRequest(HttpMethod.PUT, uri, headers), body, execution);

        // Assert
        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
        //        ArgumentCaptor<Map<String, Object>> requestCaptor = ArgumentCaptor.forClass(Map.class);
        //        ArgumentCaptor<Map<String, Object>> responseCaptor = ArgumentCaptor.forClass(Map.class);
        ArgumentCaptor<Map<String, Object>> contextCaptor = ArgumentCaptor.forClass(Map.class);

        // Expect 3 calls to logger.debug
        verify(logger, times(3)).debug(messageCaptor.capture(), contextCaptor.capture());

        // Verify request logs
        assertEquals("Sending request", messageCaptor.getAllValues().get(0));
        assertEquals(HttpMethod.PUT, contextCaptor.getAllValues().get(0).get("method"));
        assertEquals(
                "http://localhost/test",
                contextCaptor.getAllValues().get(0).get("url").toString());
        assertEquals(headers, contextCaptor.getAllValues().get(0).get("headers"));
        assertEquals("", new String(body, StandardCharsets.UTF_8)); // Verify that the body is empty

        // Verify response logs
        assertEquals("Received response", messageCaptor.getAllValues().get(2));
        assertEquals(HttpStatus.BAD_REQUEST, contextCaptor.getAllValues().get(2).get("statusCode"));
        assertEquals(headers, contextCaptor.getAllValues().get(2).get("headers"));
    }
}

// MockHttpRequest is a helper class
class MockHttpRequest implements org.springframework.http.HttpRequest {
    private final HttpMethod method;
    private final URI uri;
    private final HttpHeaders headers;

    public MockHttpRequest(HttpMethod method, URI uri, HttpHeaders headers) {
        this.method = method;
        this.uri = uri;
        this.headers = headers;
    }

    @Override
    public HttpMethod getMethod() {
        return method;
    }

    @Override
    public URI getURI() {
        return uri;
    }

    @Override
    public HttpHeaders getHeaders() {
        return headers;
    }
}
