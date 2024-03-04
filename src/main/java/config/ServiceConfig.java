package config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import input_types.InputTypeService;
import input_types.internal.InputTypeServiceImpl;
import input_types.internal.input_subtype.InputSubTypeRepository;
import input_types.internal.input_type.InputTypeRepository;
import inputs.InputService;
import inputs.internal.InputServiceImpl;
import inputs.internal.input.InputRepository;
import users.UserService;
import users.internal.UserServiceImpl;
import users.internal.user.UserRepository;

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
