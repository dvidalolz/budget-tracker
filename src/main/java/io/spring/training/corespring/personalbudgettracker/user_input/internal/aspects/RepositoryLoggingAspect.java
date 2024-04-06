package io.spring.training.corespring.personalbudgettracker.user_input.internal.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import io.spring.training.corespring.personalbudgettracker.user_input.internal.monitor.Monitor;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.monitor.MonitorFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Aspect for logging successful save (create, update), find, and delete
 * operations
 * for all repositories
 */
@Aspect
public class RepositoryLoggingAspect {
    public final static String BEFORE = "'Before'";
    public final static String AROUND = "'Around'";
    public final static String AFTER = "'After'";

    private Logger logger = LoggerFactory.getLogger(getClass());

    private MonitorFactory monitorFactory;

    public RepositoryLoggingAspect(MonitorFactory monitorFactory) {
        super();
        this.monitorFactory = monitorFactory;
    }

    /**
     * Before find operations, log its execution
     */
    @Before("execution(* io.spring.training.corespring.personalbudgettracker.user_input.internal.*.*Repository.find*(..)))")
    public void logFind(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();

        logger.info(BEFORE + " advice implementation - " + className + 
                "; Executing before " + methodName + 
                "() method");
    }

    /**
     * Around save operations (create and update), log its execution and provide
     * monitoring information
     */
    @Around("execution(* io.spring.training.corespring.personalbudgettracker.user_input.internal.*.*Repository.save*(..)))")
    public Object logSave(ProceedingJoinPoint repositoryMethod) throws Throwable {
        String name = createJoinPointTraceName(repositoryMethod);
        // Monitor performance
		Monitor monitor = monitorFactory.start(name);
		try {
			// Invoke repository method ...
			return repositoryMethod.proceed();
		} finally {
			monitor.stop();
			logger.info(AROUND + " advice implementation - " + monitor);
		}
    }

    /**
     * After delete operations, log its execution and provide some monitoring
     * information
     */
    @After("execution(* io.spring.training.corespring.personalbudgettracker.user_input.internal.*.*Repository.delete*(..)))")
    public void logDelete(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        
        logger.info(AFTER + " advice implementation - " + className + 
                    "; Executed after " + methodName + "() method");
    }

    private String createJoinPointTraceName(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        StringBuilder sb = new StringBuilder();
        sb.append(signature.getDeclaringType().getSimpleName());
        sb.append('.').append(signature.getName());
        return sb.toString();
    }
}
