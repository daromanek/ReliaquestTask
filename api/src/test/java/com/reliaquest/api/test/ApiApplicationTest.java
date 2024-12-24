package com.reliaquest.api.test;

import static org.junit.jupiter.api.Assertions.*;

import com.reliaquest.api.config.AppLoggerProperties;
import com.reliaquest.api.logger.AppLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ApiApplicationTest {
    protected final AppLogger logger; // Logger instance for logging messages

    /**
     * Constructor for ApiApplicationTest.
     * Initializes the logger with the specified log level from the application properties.
     *
     * @param loggerProperties Configuration properties for the logger
     */
    @Autowired // Use @Autowired to indicate that Spring should inject this dependency
    public ApiApplicationTest(AppLoggerProperties loggerProperties) {
        this.logger = new AppLogger(ApiApplicationTest.class); // Create logger
        // Set the log level based on the configuration property
        this.logger.setLogLevel(
                AppLogger.LogLevel.valueOf(loggerProperties.getLogLevel().toUpperCase()));

        logger.info("***********************GetLogLevel in ApiApplicationTest: " + this.logger.getLogLevel());
    }
}
