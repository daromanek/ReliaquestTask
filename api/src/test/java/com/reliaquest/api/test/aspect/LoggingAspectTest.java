package com.reliaquest.api.test.aspect;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class LoggingAspectTest {
    //
    //    @MockBean
    //    private AppLoggerProperties loggerProperties; // Mocked logger properties
    //
    //    @MockBean
    //    private AppLogger logger; // Mocked logger
    //
    //    @Autowired
    //    private LoggingAspect loggingAspect; // The aspect being tested
    //
    //    @BeforeEach
    //    void setUp() {
    //        // Ensure the mocked loggerProperties returns a valid value
    //        when(loggerProperties.getLogLevel()).thenReturn("DEBUG");
    //    }
    //
    //    @Test
    //    void testLogMethodEntry() throws NoSuchMethodException {
    //        // Create an instance of the class that uses the aspect
    //        SampleTestClass sampleTestClass = new SampleTestClass();
    //
    //        // Call the method to log entry
    //        sampleTestClass.sampleMethod("arg1");
    //
    //        // Verify that the logging occurs correctly
    //        verify(logger)
    //                .info(
    //                        eq("***********************Entering test: sampleMethod"),
    //                        argThat(args -> args instanceof Map && ((Map<?, ?>) args).containsKey("args")));
    //    }
    //
    //    @Test
    //    void testLogMethodExit() throws NoSuchMethodException {
    //        // Create an instance of the class that uses the aspect
    //        SampleTestClass sampleTestClass = new SampleTestClass();
    //
    //        // Call the method to log exit
    //        sampleTestClass.sampleMethod("result");
    //
    //        // Verify that the logging occurs correctly
    //        verify(logger)
    //                .info(
    //                        eq("***********************Exiting test: sampleMethod"),
    //                        argThat(res -> res instanceof Map && ((Map<?, ?>) res).containsKey("result")));
    //    }
    //
    //    @Test
    //    void testLogMethodException() throws NoSuchMethodException {
    //        // Create an instance of the class that uses the aspect
    //        SampleTestClass sampleTestClass = new SampleTestClass();
    //
    //        // Call the method to log exception
    //        Exception exception = assertThrows(RuntimeException.class, () -> {
    //            sampleTestClass.sampleThrowingMethod();
    //        });
    //
    //        // Explicitly instantiate the expected Map
    //        Map<String, Object> expectedMap = Map.of("exception", "Test exception");
    //
    //        // Verify logging call
    //        verify(logger).error(eq("***********************Exception in test: sampleThrowingMethod"),
    // eq(expectedMap));
    //    }
    //
    //    // Sample class to represent methods with and without exceptions
    //    public static class SampleTestClass {
    //        public String sampleMethod(String arg) {
    //            // Simulate logging behavior
    //            return "test";
    //        }
    //
    //        public void sampleThrowingMethod() {
    //            throw new RuntimeException("Test exception");
    //        }
    //    }
}
