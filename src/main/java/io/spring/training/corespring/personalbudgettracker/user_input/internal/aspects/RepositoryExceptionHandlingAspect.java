package io.spring.training.corespring.personalbudgettracker.user_input.internal.aspects;

import org.aspectj.lang.annotation.Aspect;

import java.util.List;
import java.util.Optional;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Aspect for handling low-level repository exceptions such as query issues,
 * connection issues
 * and data integrity violations.
 */
@Aspect
public class RepositoryExceptionHandlingAspect {

    public static final String FAILURE_MSG = "Exception occured in repository: ";
    public static final String NOT_FOUND_MSG = "Query returned no results: ";

    private static final Logger logger = LoggerFactory.getLogger(RepositoryExceptionHandlingAspect.class);

    /**
     * Pointcut Expression : all repositories under user_input.internal
     * Advice : log all exceptions thrown as errors
     */
    @AfterThrowing(pointcut = "execution(* io.spring.training.corespring.personalbudgettracker.user_input.internal.*.*Repository.*(..))", throwing = "e")
    public void handleRepositoryException(Exception e) {
        // Log exceptions which occur for all repository functionality
        logger.error(FAILURE_MSG + "{}", e.getMessage(), e);

    }

    /**
     * Pointcut Expression : all repositories under user_input.internal
     * Advice : log all empty returns as warnings
     */
    @AfterReturning(pointcut = "execution(* io.spring.training.corespring.personalbudgettracker.user_input.internal.*.*Repository.*(..))", returning = "result")
    public void handleEmpty(JoinPoint joinPoint, Object result) {
        // Log empty optionals/lists returned for all repository functionality
        if (result instanceof Optional && !((Optional<?>) result).isPresent()) {
            logger.warn(NOT_FOUND_MSG + "Method: {} with args: {}", joinPoint.getSignature(), joinPoint.getArgs());
        } else if (result instanceof List && ((List<?>) result).isEmpty()) {
            logger.warn(NOT_FOUND_MSG + "Method: {} with args: {}", joinPoint.getSignature(), joinPoint.getArgs());
        }
    }
}
