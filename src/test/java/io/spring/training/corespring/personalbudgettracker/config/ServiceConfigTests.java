package io.spring.training.corespring.personalbudgettracker.config;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import io.spring.training.corespring.personalbudgettracker.config.ServiceConfig;
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

import static org.assertj.core.api.Assertions.assertThat;


public class ServiceConfigTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private InputRepository inputRepository;

    @Mock
    private InputTypeRepository inputTypeRepository;

    @Mock
    private InputSubTypeRepository inputSubTypeRepository;

    @InjectMocks
    private ServiceConfig serviceConfig;

    public ServiceConfigTests() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void userServiceBeanCreation() {
        UserService userService = serviceConfig.userService();
        assertThat(userService).isNotNull();
        assertThat(userService).isInstanceOf(UserServiceImpl.class);
    }

    @Test
    public void inputServiceBeanCreation() {
        InputService inputService = serviceConfig.inputService();
        assertThat(inputService).isNotNull();
        assertThat(inputService).isInstanceOf(InputServiceImpl.class);
    }

    @Test
    public void inputTypeServiceBeanCreation() {
        InputTypeService inputTypeService = serviceConfig.inputTypeService();
        assertThat(inputTypeService).isNotNull();
        assertThat(inputTypeService).isInstanceOf(InputTypeServiceImpl.class);
    }

}
