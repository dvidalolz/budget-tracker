package config;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

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
