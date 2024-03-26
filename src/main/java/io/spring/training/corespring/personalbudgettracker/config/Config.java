package io.spring.training.corespring.personalbudgettracker.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.jdbc.core.JdbcTemplate;

import io.spring.training.corespring.personalbudgettracker.user_input.internal.monitor.MonitorFactory;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.monitor.jamon.JamonMonitorFactory;

@Configuration
@ComponentScan("io.spring.training.corespring.personalbudgettracker.user_input")
@EnableAspectJAutoProxy
public class Config {
	@Bean
	public MonitorFactory monitorFactory() {
		return new JamonMonitorFactory();
	}

	@Bean
	public JdbcTemplate jdbcTemplate(DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}

}
