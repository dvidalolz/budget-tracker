package io.spring.training.corespring.personalbudgettracker.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;



@Configuration
@ComponentScan("io.spring.training.corespring.personalbudgettracker.user_input.internal.aspects")
@EnableAspectJAutoProxy
public class AspectsConfig {

}
