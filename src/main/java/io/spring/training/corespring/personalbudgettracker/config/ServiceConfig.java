package io.spring.training.corespring.personalbudgettracker.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;


/**
 * All repository datasources handled through DataConfig.class
 */
@Configuration
@Import(DataConfig.class)
@ComponentScan("io.spring.training.corespring.personalbudgettracker")
public class ServiceConfig {


}
