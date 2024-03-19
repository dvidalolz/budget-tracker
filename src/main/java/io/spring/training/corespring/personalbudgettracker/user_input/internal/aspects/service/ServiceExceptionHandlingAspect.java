package io.spring.training.corespring.personalbudgettracker.user_input.internal.aspects.service;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ServiceExceptionHandlingAspect {
    public static final String FAILURE_MSG = "Exception occured in service: ";
}
