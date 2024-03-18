package io.spring.training.corespring.personalbudgettracker.user_input.internal.aspects;

import org.aspectj.lang.annotation.Aspect;

import java.util.Optional;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class RepositoryExceptionHandlingAspect {

    public static final String FAILURE_MSG = "Exception occured in repository: ";
    public static final String NOT_FOUND_MSG = "Query returned no results: ";

    private static final Logger logger = LoggerFactory.getLogger(RepositoryExceptionHandlingAspect.class);

    @AfterThrowing(pointcut = "execution(* io.spring.training.corespring.personalbudgettracker.user_input.internal.*.*Repository.*(..))", throwing = "e")
    public void handleRepositoryException(Exception e) {
        // Here you can use instanceof checks to log more specific information based on
        // the exception type
        logger.error(FAILURE_MSG + "{}", e.getMessage(), e);

    }

    @AfterReturning(pointcut = "execution(* io.spring.training.corespring.personalbudgettracker.user_input.internal.*.*Repository.*(..))", returning = "result")
    public void handleEmptyOptional(JoinPoint joinPoint, Object result) {
        // Check if the result is an Optional and is empty
        if (result instanceof Optional && !((Optional<?>) result).isPresent()) {
            logger.warn(NOT_FOUND_MSG + "Method: {} with args: {}", joinPoint.getSignature(), joinPoint.getArgs());
        }
    }
}
