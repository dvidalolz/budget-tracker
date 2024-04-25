package io.spring.training.corespring.personalbudgettracker.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;



@Configuration
@ComponentScan("io.spring.training.corespring.personalbudgettracker.user_input") // scan components (repos, services, controllers) and create beans automatically
public class UserInputConfig {
	

	/**
	 * JDBC template bean (convenience class) created for sql queries
	 * @param dataSource // connection to database, automatically injected into jdbcTemplate
	 */
	@Bean
	public JdbcTemplate jdbcTemplate(DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}

}
