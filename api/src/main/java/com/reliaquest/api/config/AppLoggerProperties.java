package com.reliaquest.api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

// This class is annotated with @Configuration, indicating that it is a source of bean definitions.
// It is also annotated with @ConfigurationProperties, which allows it to bind properties from application.properties or
// application.yml files.
@Configuration
@ConfigurationProperties(
        prefix = "app.logger") // The prefix 'app.logger' indicates that properties starting with this prefix will
// be
// mapped to this class.
public class AppLoggerProperties {

    // This field will hold the log level configuration.
    // It is initialized with a default value of "DEBUG".
    // This means if the property is not set in the configuration file, "DEBUG" will be used as the default log level.
    private String logLevel = "DEBUG"; // Default log level

    // Getter method for the logLevel property.
    // This method allows other parts of the application to access the current log level.
    public String getLogLevel() {
        return logLevel; // Returns the current log level
    }

    // Setter method for the logLevel property.
    // This method allows other parts of the application to set or update the log level.
    public void setLogLevel(String logLevel) {
        this.logLevel = logLevel; // Updates the log level with the provided value
    }
}
