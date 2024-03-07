package io.spring.training.corespring.personalbudgettracker.users;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.*;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

import io.spring.training.corespring.personalbudgettracker.input_types.internal.input_type.InputType;
import io.spring.training.corespring.personalbudgettracker.input_types.internal.input_type.InputTypeRepository;
import io.spring.training.corespring.personalbudgettracker.testconfig.TestInfrastructureConfig;
import io.spring.training.corespring.personalbudgettracker.users.internal.user.User;
import io.spring.training.corespring.personalbudgettracker.users.internal.user.UserDetails;


class UserServiceTests {

    private InputTypeRepository inputTypeRepository;
    private UserService userService;

    @BeforeEach
    void setUp() throws Exception {
        ApplicationContext context = SpringApplication.run(TestInfrastructureConfig.class);
        userService = context.getBean(UserService.class);
        inputTypeRepository = context.getBean(InputTypeRepository.class);

    }

    @Test
    void testUserServices() {
        UserDetails userDetails = new UserDetails("David", "dvidalolz@gmail.com", "testpassword");

        // Call userService to test its createUser(userDetails) method
        User user = userService.createUser(userDetails);

        // Assert that user object not null
        assertNotNull(user);
        // Assertions for user attributes not null
        assertNotNull(user.getId());
        assertTrue(user.getId() > 0);
        assertNotNull(user.getUsername());
        assertNotNull(user.getEmail());
        assertNotNull(user.getPasswordHash());

        // Fetch input types for the user
        List<InputType> inputTypes = inputTypeRepository.findAllByUserId(user.getId());
        // Assertions for input types
        assertNotNull(inputTypes);
        assertEquals(2, inputTypes.size());
        // Check if the input types contain "Expense" and "Income"
        assertTrue(inputTypes.stream().anyMatch(type -> "Expense".equals(type.getName())));
        assertTrue(inputTypes.stream().anyMatch(type -> "Income".equals(type.getName())));

        // Test getUserById() against user that was just created
        User retrievedUserById = userService.getUserById(user.getId());
        // Assert that retrieved user object not null
        assertNotNull(retrievedUserById);
        // Assertions to check retrieved user attributes not null
        assertTrue(retrievedUserById.getId() > 0);
        assertNotNull(retrievedUserById.getUsername());
        assertNotNull(retrievedUserById.getEmail());
        assertNotNull(retrievedUserById.getPasswordHash());
        // Assert that user and retrieved user have same attributes
        assertEquals(user, retrievedUserById);
        assertEquals(user.getUsername(), retrievedUserById.getUsername());
        assertEquals(user.getEmail(), retrievedUserById.getEmail());
        assertEquals(user.getPasswordHash(), retrievedUserById.getPasswordHash());

    }

}
