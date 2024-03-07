package io.spring.training.corespring.personalbudgettracker.users;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Random;

import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import io.spring.training.corespring.personalbudgettracker.PersonalBudgetTrackerApplication;
import io.spring.training.corespring.personalbudgettracker.input_types.internal.input_type.InputType;
import io.spring.training.corespring.personalbudgettracker.input_types.internal.input_type.InputTypeRepository;
import io.spring.training.corespring.personalbudgettracker.testconfig.TestInfrastructureConfig;
import io.spring.training.corespring.personalbudgettracker.users.UserService;
import io.spring.training.corespring.personalbudgettracker.users.internal.UserServiceImpl;
import io.spring.training.corespring.personalbudgettracker.users.internal.user.User;
import io.spring.training.corespring.personalbudgettracker.users.internal.user.UserDetails;
import io.spring.training.corespring.personalbudgettracker.users.internal.user.UserRepository;

@SpringBootTest(classes = { PersonalBudgetTrackerApplication.class, TestInfrastructureConfig.class })
@ActiveProfiles("test")
class UserServiceTests {

    private UserRepository userRepository; // Mock the UserRepository

    private InputTypeRepository inputTypeRepository; // Mock the InputTypeRepository

    private UserService userService; // Inject mocks into UserService

    @BeforeEach
    void setUp() throws Exception {
        ApplicationContext context = SpringApplication.run(TestInfrastructureConfig.class);
        userService = context.getBean(UserService.class);


                                                                           
    }

    @Test
    void testCreateUser() {
        UserDetails userDetails = new UserDetails("David", "dvidalolz@gmail.com", "testpassword");

        // Call userService to test its createUser(userDetails) method
        User user = userService.createUser(userDetails);

        // Assert that user object isn't null and its id was automatically generated
        assertNotNull(user);
        assertNotNull(user.getId());
        assertTrue(user.getId() > 0);
        assertNotNull(user.getUsername());
        assertNotNull(user.getEmail());
        assertNotNull(user.getPasswordHash());

    }

    @Test
    void testUpdateUser() {

    }

    @Test
    void testDeleteUser() {

    }

    @Test
    void testGetUser() {

    }
}
