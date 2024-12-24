package com.reliaquest.api.aspect;

import com.reliaquest.api.config.AppLoggerProperties;
import com.reliaquest.api.logger.AppLogger;
import java.util.Map;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

/**
 * LoggingAspect is an aspect that handles logging for the test classes.
 * It logs method entry, exit, and exceptions.
 */
@Aspect // Indicates that this class is an Aspect
@Component // Marks this class as a Spring component, allowing it to be detected by Spring's component scanning
public class LoggingAspect {

    protected final AppLogger logger; // Logger instance for logging messages

    /**
     * Constructor for LoggingAspect.
     * Initializes the logger with the specified log level from the application properties.
     *
     * @param loggerProperties Configuration properties for the logger
     */
    public LoggingAspect(AppLoggerProperties loggerProperties) {
        if (loggerProperties == null) {
            System.out.println(
                    "*****************************************************************************************");
            System.out.println(
                    "*************************************LoggerProperties passed into LoggingAspect was null");
            System.out.println(
                    "******************************************************************************************");
        }
        this.logger = new AppLogger(LoggingAspect.class); // Create a new logger for this aspect
        // Set the log level based on the configuration property
        this.logger.setLogLevel(
                AppLogger.LogLevel.valueOf(loggerProperties.getLogLevel().toUpperCase()));

        logger.info("***********************GetLogLevel in Test Logging Aspect: " + this.logger.getLogLevel());
    }

    /**
     * Pointcut that matches all methods in the test packages.
     * This is used to apply logging advice to these methods.
     */
    @Pointcut("execution(* com.reliaquest.api.test..*(..)) || "
            + "execution(* com.reliaquest.api.test.model..*(..)) || "
            + "execution(* com.reliaquest.api.test.config..*(..)) || "
            + "execution(* com.reliaquest.api.test.aspect..*(..)) || "
            + "execution(* com.reliaquest.api.test.service..*(..)) || "
            + "execution(* com.reliaquest.api.test.controller..*(..))")
    public void aspectedMethods() {}

    /**
     * Pointcut that matches all methods in the test packages.
     * This is used to apply logging advice to these methods.
     */
    //    @Pointcut("execution(* com.reliaquest.api..*(..)) || "
    //            + "execution(* com.reliaquest.api.service..*(..)) || "
    //            + "execution(* com.reliaquest.api.model..*(..)) || "
    //            + "execution(* com.reliaquest.api.controller..*(..))")
    //    public void aspectedMethods() {}

    /**
     * After throwing advice that handles exceptions thrown by methods in the
     * Test classes. It maps the caught exceptions to custom exceptions.
     *
     * @param joinPoint The join point providing reflective access to both the state available at a join point
     *                  and the static part of the join point (method signature, etc.).
     * @param ex        The exception that was thrown.
     * @throws Throwable The appropriate custom exception based on the method that was executed.
     */
    @AfterThrowing(pointcut = "aspectedMethods()", throwing = "ex")
    public void logMethodException(JoinPoint joinPoint, Throwable ex) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature(); // Get method signature
        String methodName = methodSignature.getName(); // Get method name

        // Log the exception with its name and message
        logger.error("***********************Exception in test: " + methodName, Map.of("exception", ex.getMessage()));
    }

    /**
     * Logs the entry of a method in the Test classes.
     *
     * @param joinPoint provides reflective access to both the state available at a join point
     */
    @Before("aspectedMethods()") // Advice that runs before the matched methods
    public void logMethodEntry(JoinPoint joinPoint) {
        logger.info("*************Entered logMethodEntry");
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature(); // Get method signature
        String methodName = methodSignature.getName(); // Get method name
        Object[] args = joinPoint.getArgs(); // Get method arguments

        // Log the method entry with its name and parameters
        logger.info("***********************Entering test: " + methodName, Map.of("args", args));
    }

    /**
     * Logs the exit of a method in the StracService.
     *
     * @param joinPoint provides reflective access to both the state available at a join point
     * @param result    the result returned by the method
     */
    @AfterReturning(
            pointcut = "aspectedMethods()",
            returning = "result") // Advice that runs after the matched methods return
    public void logMethodExit(JoinPoint joinPoint, Object result) {
        logger.info("*************Entered logMethodExit");
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature(); // Get method signature
        String methodName = methodSignature.getName(); // Get method name

        // Log the method exit with its name and return value
        logger.info(
                "***********************Exiting test: " + methodName,
                Map.of("result", result != null ? result : "null"));
    }
}
