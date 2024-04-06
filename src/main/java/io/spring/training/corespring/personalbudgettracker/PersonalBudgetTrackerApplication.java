package io.spring.training.corespring.personalbudgettracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class PersonalBudgetTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(PersonalBudgetTrackerApplication.class, args);
	}

}
