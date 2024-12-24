package com.reliaquest.api.logger;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

/**
 * AppLogger is a custom logger that wraps SLF4J's Logger.
 * It provides methods to log messages at different levels (INFO, DEBUG, WARN, ERROR)
 * and allows for context information to be included in the log messages.
 *
 * It also supports setting a log level to control which messages are printed.
 */
public class AppLogger {
    // SLF4J Logger instance for logging messages
    private final Logger logger;
    // Current log level to control which messages are logged
    private LogLevel currentLogLevel;

    // Enum to define log levels
    public enum LogLevel {
        ERROR, // Error level logs
        WARN, // Warning level logs
        INFO, // Informational level logs
        DEBUG // Debug level logs
    }

    // Method to set the log level
    public void setLogLevel(LogLevel logLevel) {
        this.currentLogLevel = logLevel; // Update the current log level
    }

    // Method to get the current log level
    public LogLevel getLogLevel() {
        return currentLogLevel; // Return the current log level
    }

    // Constructor initializes the logger and sets the default log level to DEBUG
    public AppLogger(Class<?> clazz) {
        this.currentLogLevel = LogLevel.DEBUG; // Default log level
        this.logger = LoggerFactory.getLogger(clazz); // Create a logger for the specified class
    }

    // Constructor needed by the JUNIT mock tests
    public AppLogger(Logger logger) {
        this.currentLogLevel = LogLevel.DEBUG; // Default log level
        this.logger = logger; // Use the injected logger
    }

    // Method to log an INFO message
    public void info(String message) {
        // Check if the current log level allows logging INFO messages
        if (shouldLog(LogLevel.INFO)) {
            logger.info(message); // Log the INFO message
        }
    }

    // Method to log an INFO message with additional context
    public void info(String message, Map<String, Object> context) {
        // Check if the current log level allows logging INFO messages
        if (shouldLog(LogLevel.INFO)) {
            logger.info(formatMessage(message, context)); // Log the formatted INFO message with context
        }
    }

    // Method to log a DEBUG message
    public void debug(String message) {
        // Check if the current log level allows logging DEBUG messages
        if (shouldLog(LogLevel.DEBUG)) {
            logger.debug(message); // Log the DEBUG message
        }
    }

    // Method to log a DEBUG message with additional context
    public void debug(String message, Map<String, Object> context) {
        // Check if the current log level allows logging DEBUG messages
        if (shouldLog(LogLevel.DEBUG)) {
            logger.debug(formatMessage(message, context)); // Log the formatted DEBUG message with context
        }
    }

    // Method to log a WARN message
    public void warn(String message) {
        // Check if the current log level allows logging WARN messages
        if (shouldLog(LogLevel.WARN)) {
            logger.warn(message); // Log the WARN message
        }
    }

    // Method to log a WARN message with additional context
    public void warn(String message, Map<String, Object> context) {
        // Check if the current log level allows logging WARN messages
        if (shouldLog(LogLevel.WARN)) {
            logger.warn(formatMessage(message, context)); // Log the formatted WARN message with context
        }
    }

    // Method to log an ERROR message without an underlying throwable
    public void error(String message) {
        logger.error(message); // Log the ERROR message
    }

    // Method to log an ERROR message with additional context
    public void error(String message, Map<String, Object> context) {
        logger.error(formatMessage(message, context)); // Log the formatted ERROR message with context
    }

    // Method to log an ERROR message with an underlying throwable
    public void error(String message, Throwable throwable) {
        // Check if the current log level allows logging ERROR messages
        if (shouldLog(LogLevel.ERROR)) {
            logger.error(message, throwable); // Log the ERROR message with the throwable
        }
    }

    // Method to log an ERROR message with an underlying throwable and additional context
    public void error(String message, Throwable throwable, Map<String, Object> context) {
        // Check if the current log level allows logging ERROR messages
        if (shouldLog(LogLevel.ERROR)) {
            logger.error(
                    formatMessage(message, context),
                    throwable); // Log the formatted ERROR message with context and throwable
        }
    }

    // Private method to format the log message with context information
    private String formatMessage(String message, Map<String, Object> context) {
        StringBuilder sb = new StringBuilder(message); // Start with the original message

        // Append MDC values to the message
        Map<String, String> mdcContext = MDC.getCopyOfContextMap(); // Get a copy of the current MDC context
        if (mdcContext != null && !mdcContext.isEmpty()) {
            sb.append(" | MDC: "); // Indicate that MDC values are being appended
            // Append each MDC key-value pair to the message
            MDC.getCopyOfContextMap()
                    .forEach((key, value) ->
                            sb.append(key).append("=").append(value).append(", "));
            sb.setLength(sb.length() - 2); // Remove last comma and space
        }

        // Append additional context if provided
        if (context != null && !context.isEmpty()) {
            sb.append(" | "); // Indicate that additional context is being appended
            // Append each context key-value pair to the message
            context.forEach(
                    (key, value) -> sb.append(key).append("=").append(value).append(", "));
            sb.setLength(sb.length() - 2); // Remove last comma and space
        }
        return sb.toString(); // Return the formatted message
    }

    // Private method to determine if a message should be logged based on the current log level
    protected boolean shouldLog(LogLevel logLevel) {
        // Compare the ordinal values of the log levels to determine if logging is allowed
        return logLevel.ordinal() <= currentLogLevel.ordinal();
    }
}
