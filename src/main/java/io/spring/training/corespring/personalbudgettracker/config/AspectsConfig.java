package io.spring.training.corespring.personalbudgettracker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import io.spring.training.corespring.personalbudgettracker.user_input.internal.monitor.MonitorFactory;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.monitor.jamon.JamonMonitorFactory;



@Configuration
@ComponentScan("io.spring.training.corespring.personalbudgettracker.user_input.internal.aspects")
@EnableAspectJAutoProxy
public class AspectsConfig {
	@Bean
	public MonitorFactory monitorFactory(){
		return new JamonMonitorFactory();
	}
}
