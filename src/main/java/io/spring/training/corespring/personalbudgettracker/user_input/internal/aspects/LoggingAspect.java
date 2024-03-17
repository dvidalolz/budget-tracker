package io.spring.training.corespring.personalbudgettracker.user_input.internal.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import io.spring.training.corespring.personalbudgettracker.user_input.internal.monitor.Monitor;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.monitor.MonitorFactory;



// 	TODO-02: Use AOP to log a message before
//           any repository's find...() method is invoked.
//  - Add an appropriate annotation to this class to indicate this class is an aspect.
//	- Also make it as a component.
//	- Optionally place @Autowired annotation on the constructor
//    where `MonitorFactory` dependency is being injected.
//    (It is optional since there is only a single constructor in the class.)
@Aspect
@Component
public class LoggingAspect {
    public final static String BEFORE = "'Before'";
    public final static String AROUND = "'Around'";

	private Logger logger = LoggerFactory.getLogger(getClass());
	private MonitorFactory monitorFactory;

	
	public LoggingAspect(MonitorFactory monitorFactory) {
		super();
		this.monitorFactory = monitorFactory;
	}

    /**
     * Pointcut expression : all finds in all repositories under user_input.internal
     * Advice : log what happens before any repository find operation
     */
	@Before("execution(* user_input.internal.*.*Repository.find*(..))")
	public void implLogging(JoinPoint joinPoint) {
		// Do not modify this log message or the test will fail
		logger.info(BEFORE + " advice implementation - " + joinPoint.getTarget().getClass() + //
				"; Executing before " + joinPoint.getSignature().getName() + //
				"() method");
	}
	
    /**
     * Pointcut expression : all updates in all repositories under user_input.interal
     * Advice : log what happens around repository updates
     */
	@Around("execution(* user_input.internal.*.*Repository.update*(..))")
	public Object monitor(ProceedingJoinPoint repositoryMethod) throws Throwable {
		String name = createJoinPointTraceName(repositoryMethod);
		Monitor monitor = monitorFactory.start(name);
		try {
			// Important note : you're responsible for returning object type after proxy enhanced operations for "Around"
			return repositoryMethod.proceed();
		} finally {
			monitor.stop();
			logger.info(AROUND + " advice implementation - " + monitor);
		}
	}
		
	private String createJoinPointTraceName(JoinPoint joinPoint) {
		Signature signature = joinPoint.getSignature();
		StringBuilder sb = new StringBuilder();
		sb.append(signature.getDeclaringType().getSimpleName());
		sb.append('.').append(signature.getName());
		return sb.toString();
	} 
}