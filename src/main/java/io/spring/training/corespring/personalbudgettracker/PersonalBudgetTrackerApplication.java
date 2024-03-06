package io.spring.training.corespring.personalbudgettracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import config.ServiceConfig;
import input_types.InputTypeService;
import inputs.InputService;
import users.UserService;

@SpringBootApplication
public class PersonalBudgetTrackerApplication {

	public static void main(String[] args) {
		// SpringApplication.run(PersonalBudgetTrackerApplication.class, args);


		final UserService userService;
		final InputService inputService;
		final InputTypeService inputTypeService;
		
		// Instantiate user service with application context bean configurations
		ApplicationContext context = SpringApplication.run(ServiceConfig.class);
	
		// Load services using beans
		userService = context.getBean(UserService.class);
		inputService = context.getBean(InputService.class);
		inputTypeService = context.getBean(InputTypeService.class);

	}

}
