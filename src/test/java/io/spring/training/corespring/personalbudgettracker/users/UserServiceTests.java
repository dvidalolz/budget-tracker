package io.spring.training.corespring.personalbudgettracker.users;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;


import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import io.spring.training.corespring.personalbudgettracker.TestInfrastructureConfig;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.UserService;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.input_type.InputType;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.input_type.InputTypeRepository;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.user.User;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.user.UserDetails;

/**
 * Tests must be run independently to avoid failure due to state sharing
 */
@SpringJUnitConfig(TestInfrastructureConfig.class)
@ActiveProfiles({ "jdbc", "local" })
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class UserServiceTests {

    @Autowired
    private InputTypeRepository inputTypeRepository;

    @Autowired
    private UserService userService;

    private static User testUser;

    @BeforeEach
    public void setup() {
        UserDetails userDetails = new UserDetails("David", "dvidalolz@gmail.com", "TestPassword");
        testUser = userService.addUser(userDetails);
    }

    /**
     * Tests
     */
    /**
     * Test create user
     * Should return fully fleshed user object, save user to userrepository, and
     * create
     * and save 2 default input types ("Expense" and "Income") to
     * inputTypeRepository
     */
    @Test
    void testCreateUser() {
        assertNotNull(testUser);
        assertNotNull(testUser.getId());
        assertTrue(testUser.getId() > 0);
        assertEquals(testUser.getUsername(), "David");
        assertEquals(testUser.getEmail(), "dvidalolz@gmail.com");
        assertTrue(testUser.checkPassword("TestPassword"));
    }

    /**
     * Test that createUser(userDetails) creates 2 default input types ("Expense"
     * and "Income"),
     * and saves it to inputRepository with userId as foreign key
     */
    @Test
    void testCreateUserInputTypes() {
        List<InputType> inputTypes = inputTypeRepository.findAllByUserId(testUser.getId());

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
        User retrievedUserById = userService.findUserById(testUser.getId());

        assertNotNull(retrievedUserById);
        assertTrue(retrievedUserById.getId() > 0);
        assertNotNull(retrievedUserById.getUsername());
        assertNotNull(retrievedUserById.getEmail());
        assertNotNull(retrievedUserById.getPasswordHash());
        assertEquals(testUser, retrievedUserById);
    }

    /**
     * Test getUserByUserName(userName)
     * Should receive a fully fleshed user object : test using user that was just
     * created above
     */
    @Test
    void testGetUserByUserName() {
        User retrievedUserByUserName = userService.findUserByUserName("David");

        assertNotNull(retrievedUserByUserName);
        assertTrue(retrievedUserByUserName.getId() > 0);
        assertNotNull(retrievedUserByUserName.getUsername());
        assertNotNull(retrievedUserByUserName.getEmail());
        assertNotNull(retrievedUserByUserName.getPasswordHash());
        assertEquals(testUser, retrievedUserByUserName);
    }

    /**
     * Test updateUser(userId, userDetails)
     * Should receive fully fleshed and updated user object
     * Use getUserById to ensure new information was saved to repo
     */
    @Test
    void testUpdateUser() {
        UserDetails userUpdateDetails = new UserDetails("DavidUpdated", "updatedEmail@gmail.com", "UpdatePassword");
        User updatedUser = userService.updateUser(testUser.getId(), userUpdateDetails);

        assertNotNull(updatedUser);
        assertEquals(testUser, updatedUser);
        assertTrue(updatedUser.checkPassword("UpdatePassword"));

        User fetchedUpdatedUser = userService.findUserById(updatedUser.getId());

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
        userService.deleteUser(testUser.getId());

        assertThrows(Exception.class, () -> {
            userService.findUserById(testUser.getId());
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
        User scriptUser = userService.findUserByUserName("JohnDoe");
        assertNotNull(scriptUser);
        assertEquals(scriptUser.getEmail(), "john.doe@example.com");
        assertEquals(scriptUser.getPasswordHash(), "hash1");
    }


}
