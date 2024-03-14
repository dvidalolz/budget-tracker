package io.spring.training.corespring.personalbudgettracker.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import io.spring.training.corespring.personalbudgettracker.input_types.InputTypeService;
import io.spring.training.corespring.personalbudgettracker.input_types.internal.InputTypeServiceImpl;
import io.spring.training.corespring.personalbudgettracker.input_types.internal.input_subtype.InputSubTypeRepository;
import io.spring.training.corespring.personalbudgettracker.input_types.internal.input_type.InputTypeRepository;
import io.spring.training.corespring.personalbudgettracker.inputs.InputService;
import io.spring.training.corespring.personalbudgettracker.inputs.internal.InputServiceImpl;
import io.spring.training.corespring.personalbudgettracker.inputs.internal.input.InputRepository;
import io.spring.training.corespring.personalbudgettracker.users.UserService;
import io.spring.training.corespring.personalbudgettracker.users.internal.UserServiceImpl;
import io.spring.training.corespring.personalbudgettracker.users.internal.user.UserRepository;

/**
 * All repository datasources handled through DataConfig.class
 */
@Configuration
@Import(DataConfig.class)
public class ServiceConfig {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InputRepository inputRepository;

    @Autowired
    private InputTypeRepository inputTypeRepository;

    @Autowired
    private InputSubTypeRepository inputSubTypeRepository;
    
    @Bean
    public UserService userService() {
        return new UserServiceImpl(userRepository, inputTypeRepository);
    }

    @Bean 
    public InputService inputService() {
        return new InputServiceImpl(inputRepository);
    }

    @Bean 
    public InputTypeService inputTypeService() {
        return new InputTypeServiceImpl(inputTypeRepository, inputSubTypeRepository);
    }

}
