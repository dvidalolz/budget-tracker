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

    /**
     * Test create user
     * Should return fully fleshed user object, save user to userrepository, and
     * create
     * and save 2 default input types ("Expense" and "Income") to
     * inputTypeRepository
     */
    @Test
    void testCreateUser() {
        UserDetails userDetails = new UserDetails("David", "dvidalolz@gmail.com", "testpassword");
        User user = userService.createUser(userDetails);

        assertNotNull(user);
        assertNotNull(user.getId());
        assertTrue(user.getId() > 0);
        assertEquals(user.getUsername(), "David");
        assertEquals(user.getEmail(), "dvidalolz@gmail.com");
        assertTrue(user.checkPassword("testpassword"));
    }

    /**
     * Test that createUser(userDetails) creates 2 default input types ("Expense"
     * and "Income"),
     * and saves it to inputRepository with userId as foreign key
     */
    @Test
    void testCreateUserInputTypes() {
        UserDetails userDetails = new UserDetails("David", "dvidalolz@gmail.com", "testpassword");
        User user = userService.createUser(userDetails);

        List<InputType> inputTypes = inputTypeRepository.findAllByUserId(user.getId());

        assertEquals(2, inputTypes.size());
        assertTrue(inputTypes.stream().anyMatch(type -> "Expense".equals(type.getName())));
        assertTrue(inputTypes.stream().anyMatch(type -> "Income".equals(type.getName())));
    }

    /**
     * Test getUserById(userId)
     * Should receive a fully fleshed user object : Test using user that was just
     * created above
     */
    @Test
    void testGetUserById() {
        UserDetails userDetails = new UserDetails("David", "dvidalolz@gmail.com", "testpassword");
        User user = userService.createUser(userDetails);

        User retrievedUserById = userService.getUserById(user.getId());

        assertNotNull(retrievedUserById);
        assertTrue(retrievedUserById.getId() > 0);
        assertNotNull(retrievedUserById.getUsername());
        assertNotNull(retrievedUserById.getEmail());
        assertNotNull(retrievedUserById.getPasswordHash());
        assertEquals(user, retrievedUserById);
    }

    /**
     * Test getUserByUserName(userName)
     * Should receive a fully fleshed user object : test using user that was just
     * created above
     */
    @Test
    void testGetUserByUserName() {
        UserDetails userDetails = new UserDetails("David", "dvidalolz@gmail.com", "testpassword");
        User user = userService.createUser(userDetails);

        User retrievedUserByUserName = userService.getUserByUserName("David");

        assertNotNull(retrievedUserByUserName);
        assertTrue(retrievedUserByUserName.getId() > 0);
        assertNotNull(retrievedUserByUserName.getUsername());
        assertNotNull(retrievedUserByUserName.getEmail());
        assertNotNull(retrievedUserByUserName.getPasswordHash());
        assertEquals(user, retrievedUserByUserName);
    }

    /**
     * Test updateUser(userId, userDetails)
     * Should receive fully fleshed and updated user object
     * Use getUserById to ensure new information was saved to repo
     */
    @Test
    void testUpdateUser() {
        UserDetails userDetails = new UserDetails("David", "dvidalolz@gmail.com", "testpassword");
        User user = userService.createUser(userDetails);

        UserDetails userUpdateDetails = new UserDetails("DavidUpdated", "updatedEmail@gmail.com", "UpdatePassword");
        User updatedUser = userService.updateUser(user.getId(), userUpdateDetails);

        assertNotNull(updatedUser);
        assertEquals(user, updatedUser);
        assertTrue(updatedUser.checkPassword("UpdatePassword"));

        User fetchedUpdatedUser = userService.getUserById(updatedUser.getId());

        assertNotNull(fetchedUpdatedUser);
        assertEquals(updatedUser, fetchedUpdatedUser);
        assertTrue(fetchedUpdatedUser.checkPassword("UpdatePassword"));
    }

    /**
     * Test deleteUser(userId) using getUserById()
     * Attempting getUserById(userId) on a deleted user should throw exception
     */
    @Test
    void testDeleteUser() {
        UserDetails userDetails = new UserDetails("David", "dvidalolz@gmail.com", "testpassword");
        User user = userService.createUser(userDetails);

        userService.deleteUser(user.getId());
        assertThrows(Exception.class, () -> {
            userService.getUserById(user.getId());
        });
    }

    /**
     * Test testInfrastructureConfig(that scripts are running) as well as
     * getUserByUserName
     * Should return fully fleshed out user which was added via data.sql script
     * Important note : Ids are not generated when script built for
     * testinfrastructureconfig
     */
    @Test
    void testGetUserFromScript() {
        User scriptUser = userService.getUserByUserName("JohnDoe");
        assertNotNull(scriptUser);
        assertEquals(scriptUser.getEmail(), "john.doe@example.com");
        assertEquals(scriptUser.getPasswordHash(), "hash1");
    }

}
