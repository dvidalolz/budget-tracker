package io.spring.training.corespring.personalbudgettracker.users;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
        /**
         * Test create user
         * Should return fully fleshed user object, save user to userrepository, and
         * create
         * and save 2 default input types ("Expense" and "Income") to
         * inputTypeRepository
         */
        UserDetails userDetails = new UserDetails("David", "dvidalolz@gmail.com", "testpassword");
        User user = userService.createUser(userDetails);

        assertNotNull(user);
        // Assertions for user attributes
        assertNotNull(user.getId());
        assertTrue(user.getId() > 0);
        assertEquals(user.getUsername(), "David");
        assertEquals(user.getEmail(), "dvidalolz@gmail.com");
        assertTrue(user.checkPassword("testpassword"));

        /**
         * Test that createUser(userDetails) creates 2 default input types ("Expense"
         * and "Income"),
         * and saves it to inputRepository with userId as foreign key
         */
        List<InputType> inputTypes = inputTypeRepository.findAllByUserId(user.getId());

        assertEquals(2, inputTypes.size());
        // Check if the input types contain "Expense" and "Income"
        assertTrue(inputTypes.stream().anyMatch(type -> "Expense".equals(type.getName())));
        assertTrue(inputTypes.stream().anyMatch(type -> "Income".equals(type.getName())));
        // TODO: Assert that error thrown if user not found (Do after you fix up errors
        // and logs)


        /**
         * Test getUserById(userId)
         * Should receive a fully fleshed user object : Test using user that was just
         * created above
         */
        User retrievedUserById = userService.getUserById(user.getId());

        assertNotNull(retrievedUserById);
        // Assertions for user object attributes
        assertTrue(retrievedUserById.getId() > 0);
        assertNotNull(retrievedUserById.getUsername());
        assertNotNull(retrievedUserById.getEmail());
        assertNotNull(retrievedUserById.getPasswordHash());
        // Assert that user and retrieved user have same attributes
        assertEquals(user, retrievedUserById);
        // TODO: Assert that error thrown if user not found (Do after you fix up errors
        // and logs)


        /**
         * Test getUserByUserName(userName)
         * Should receive a fully fleshed user object : test using user that was just 
         * created above
         */
        User retrievedUserByUserName = userService.getUserByUserName("David");

        assertNotNull(retrievedUserByUserName);
        // Assertions for user object attributes
        assertTrue(retrievedUserByUserName.getId() > 0);
        assertNotNull(retrievedUserByUserName.getUsername());
        assertNotNull(retrievedUserByUserName.getEmail());
        assertNotNull(retrievedUserByUserName.getPasswordHash());
        // Assert that user and retrieved user have same attributes
        assertEquals(user, retrievedUserByUserName);

        /**
         * Test updateUser(userId, userDetails)
         * Should receive fully fleshed and updated user object
         */
        UserDetails userUpdateDetails = new UserDetails("DavidUpdated", "updatedEmail@gmail.com", "UpdatePassword");
        User updatedUser = userService.updateUser(user.getId(), userUpdateDetails);

        assertNotNull(updatedUser);
        // Assertion for updated user attributes
        assertEquals(user, updatedUser);
        assertTrue(updatedUser.checkPassword("UpdatePassword"));

        /**
         * Test updateUser using getByUserId to ensure new information was in fact saved
         * to repository
         * Should receive fully fleshed and updated user object from repository
         */
        User fetchedUpdatedUser = userService.getUserById(updatedUser.getId());

        assertNotNull(fetchedUpdatedUser);
        // Assertions for fetched updated user attributes
        assertEquals(updatedUser, fetchedUpdatedUser);
        assertTrue(fetchedUpdatedUser.checkPassword("UpdatePassword"));

        /**
         * Test deleteUser(userId) using getUserById()
         * Attempting getUserById(userId) on a deleted user should throw exception
         */
        userService.deleteUser(user.getId());
        assertThrows(Exception.class, () -> {
            userService.getUserById(user.getId());
        });


        /**
         * Test testInfrastructureConfig(that scripts are running) as well as getUserByUserName
         * Should return fully fleshed out user which was added via data.sql script
         * Important note : Ids are not generated when script built for testinfrastructureconfig
         */
        User scriptUser = userService.getUserByUserName("JohnDoe");
        assertNotNull(scriptUser);
        assertEquals(scriptUser.getEmail(), "john.doe@example.com");
        assertEquals(scriptUser.getPasswordHash(), "hash1");
        
    }

}
