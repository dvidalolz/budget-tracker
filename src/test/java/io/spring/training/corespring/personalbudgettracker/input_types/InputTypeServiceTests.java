package io.spring.training.corespring.personalbudgettracker.input_types;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

import io.spring.training.corespring.personalbudgettracker.input_types.internal.input_subtype.InputSubType;
import io.spring.training.corespring.personalbudgettracker.input_types.internal.input_type.InputType;
import io.spring.training.corespring.personalbudgettracker.testconfig.TestInfrastructureConfig;
import io.spring.training.corespring.personalbudgettracker.users.UserService;
import io.spring.training.corespring.personalbudgettracker.users.internal.user.User;
import io.spring.training.corespring.personalbudgettracker.users.internal.user.UserDetails;

public class InputTypeServiceTests {

    private UserService userService;
    private InputTypeService inputTypeService;

    @BeforeEach
    void setUp() throws Exception {
        ApplicationContext context = SpringApplication.run(TestInfrastructureConfig.class);
        inputTypeService = context.getBean(InputTypeService.class);
        userService = context.getBean(UserService.class);
        // Create inputtype with newly created user

    }

    /**
     * Test createInputType : Creates input type for associated user
     * Should return fully fleshed inputtype with lightweight user (id only)
     * Should save the inputtypes in inputtyperepository for user
     * Throws exception for if user not found
     */
    @Test
    void testCreateInputType() {
        UserDetails userDetails = new UserDetails("David", "dvidalolz@gmail.com", "testpassword");
        User user = userService.createUser(userDetails);
        InputType inputType = inputTypeService.createInputTypeForUser(user.getId(), "TestType");

        assertNotNull(inputType);
        assertNotNull(inputType.getId());
        assertTrue(inputType.getId() > 0);
        assertEquals(inputType.getName(), "TestType");
        assertEquals(inputType.getUser().getId(), user.getId());
    }

    /**
     * Test findAllInputTypesByUserId(userId), and in doing so, test that above
     * inputtype was
     * properly associated with above created user
     */
    @Test
    void testFindAllInputTypesByUserId() {
        UserDetails userDetails = new UserDetails("David", "dvidalolz@gmail.com", "testpassword");
        User user = userService.createUser(userDetails);
        inputTypeService.createInputTypeForUser(user.getId(), "TestType");

        List<InputType> userInputTypes = inputTypeService.findAllInputTypesByUserId(user.getId());
        assertTrue(userInputTypes.size() == 3);
        assertTrue(userInputTypes.stream().anyMatch(type -> "TestType".equals(type.getName())));

        for (InputType it : userInputTypes) {
            assertNotNull(it.getId());
            assertNotNull(it.getName());
            assertNotNull(it.getUser());
            assertNotNull(it.getUser().getId());
            assertNull(it.getUser().getUsername());
        }
    }

    /**
     * Test that error is thrown for attempting to create inputtype for non-existent
     * user
     */
    @Test
    void testCreateInputTypeForNonExistingUser() {
        UserDetails userDetails = new UserDetails("David", "dvidalolz@gmail.com", "testpassword");
        User user = userService.createUser(userDetails);

        assertThrows(Exception.class, () -> {
            inputTypeService.createInputTypeForUser(user.getId() + 50, "ErrorType");
        });
    }

    /**
     * Test updateInputType(typeId, newTypeName)
     * Should return fully fleshed inputtype object with lightweight user
     */
    @Test
    void testUpdateInputType() {
        UserDetails userDetails = new UserDetails("David", "dvidalolz@gmail.com", "testpassword");
        User user = userService.createUser(userDetails);
        InputType inputType = inputTypeService.createInputTypeForUser(user.getId(), "TestType");

        InputType updatedInputType = inputTypeService.updateInputType(inputType.getId(), "UpdatedTestType");

        assertNotNull(updatedInputType);
        assertNotNull(updatedInputType.getId());
        assertTrue(updatedInputType.getId() > 0);
        assertEquals(updatedInputType.getName(), "UpdatedTestType");
        assertEquals(updatedInputType.getUser().getId(), user.getId());
    }

    /**
     * Test that inputtype was updated for user using findAllByUserId()
     * Should return fully fleshed and updated inputtype object with lightweightuser
     */
    @Test
    void testUpdateInputTypeAndVerify() {
        UserDetails userDetails = new UserDetails("David", "dvidalolz@gmail.com", "testpassword");
        User user = userService.createUser(userDetails);
        InputType inputType = inputTypeService.createInputTypeForUser(user.getId(), "TestType");

        inputTypeService.updateInputType(inputType.getId(), "UpdatedTestType");

        List<InputType> updatedUserInputTypes = inputTypeService.findAllInputTypesByUserId(user.getId());
        assertTrue(updatedUserInputTypes.size() == 3);
        assertTrue(updatedUserInputTypes.stream().anyMatch(type -> "UpdatedTestType".equals(type.getName())));
        assertTrue(updatedUserInputTypes.stream().anyMatch(type -> "Expense".equals(type.getName())));
        assertTrue(updatedUserInputTypes.stream().anyMatch(type -> "Income".equals(type.getName())));
        assertFalse(updatedUserInputTypes.stream().anyMatch(type -> "TestType".equals(type.getName())));
    }

    /**
     * Test deleteInputTypeByTypeId(typeId) : should delete that input type
     * Verify persistence using findAllInputTypesByUserId()
     */
    @Test
    void testDeleteInputType() {
        UserDetails userDetails = new UserDetails("David", "dvidalolz@gmail.com", "testpassword");
        User user = userService.createUser(userDetails);
        InputType inputType = inputTypeService.createInputTypeForUser(user.getId(), "TestType");
        inputTypeService.updateInputType(inputType.getId(), "UpdatedTestType");

        inputTypeService.deleteInputTypeById(inputType.getId());
        List<InputType> inputTypesWithDeletion = inputTypeService.findAllInputTypesByUserId(user.getId());

        assertFalse(inputTypesWithDeletion.stream().anyMatch(type -> "UpdatedTestType".equals(type.getName())));
        assertTrue(inputTypesWithDeletion.stream().anyMatch(type -> "Expense".equals(type.getName())));
        assertTrue(inputTypesWithDeletion.stream().anyMatch(type -> "Income".equals(type.getName())));

        assertThrows(Exception.class, () -> {
            inputTypeService.deleteInputTypeById(inputType.getId() + 50);
        });
    }

    /**
     * Test create input subtype
     * Should return fully fleshed inputsubtype object with lightweight inputtype(id
     * only)
     */
    @Test
    void testCreateInputSubType() {
        UserDetails userDetails = new UserDetails("David", "dvidalolz@gmail.com", "testpassword");
        User user = userService.createUser(userDetails);
        List<InputType> userInputTypes = inputTypeService.findAllInputTypesByUserId(user.getId());
        InputType expenseType = userInputTypes.get(0);

        InputSubType inputSubType = inputTypeService.createInputSubType(expenseType.getId(), "TestSubType");

        assertNotNull(inputSubType);
        assertNotNull(inputSubType.getId());
        assertTrue(inputSubType.getId() > 0);
        assertEquals(inputSubType.getName(), "TestSubType");
        assertEquals(inputSubType.getType().getId(), expenseType.getId());
    }

    /**
     * Test findAllInputSubTypes by typeId, and in doing so, test that the above
     * created subtype was properly associated with inputtype
     */
    @Test
    void testFindAllInputSubTypesByTypeId() {
        UserDetails userDetails = new UserDetails("David", "dvidalolz@gmail.com", "testpassword");
        User user = userService.createUser(userDetails);
        List<InputType> userInputTypes = inputTypeService.findAllInputTypesByUserId(user.getId());
        InputType expenseType = userInputTypes.get(0);
        inputTypeService.createInputSubType(expenseType.getId(), "TestSubType");

        List<InputSubType> typeSubTypes = inputTypeService.findAllInputSubTypesByTypeId(expenseType.getId());
        assertTrue(typeSubTypes.size() == 1);
        assertTrue(typeSubTypes.stream().anyMatch(type -> "TestSubType".equals(type.getName())));

        InputSubType retrievedSubType = typeSubTypes.get(0);
        assertEquals(retrievedSubType.getName(), "TestSubType");
        assertEquals(retrievedSubType.getType(), expenseType);
    }

    /**
     * Test that error is thrown for attempting to create input subtype
     * for non-existent user
     */
    @Test
    void testCreateInputSubTypeForNonExistingInputType() {
        UserDetails userDetails = new UserDetails("David", "dvidalolz@gmail.com", "testpassword");
        User user = userService.createUser(userDetails);
        List<InputType> userInputTypes = inputTypeService.findAllInputTypesByUserId(user.getId());
        InputType expenseType = userInputTypes.get(0);

        assertThrows(Exception.class, () -> {
            inputTypeService.createInputSubType(expenseType.getId() + 50, "ErrorType");
        });
    }

    /**
     * Test updateInputSubType(subtypeId, newSubTypeName)
     * Should return fully fleshed inputsubtype object with lightweight inputtype
     * (id only)
     */
    @Test
    void testUpdateInputSubType() {
        UserDetails userDetails = new UserDetails("David", "dvidalolz@gmail.com", "testpassword");
        User user = userService.createUser(userDetails);
        List<InputType> userInputTypes = inputTypeService.findAllInputTypesByUserId(user.getId());
        InputType expenseType = userInputTypes.get(0);
        InputSubType inputSubType = inputTypeService.createInputSubType(expenseType.getId(), "TestSubType");

        InputSubType updatedInputSubType = inputTypeService.updateInputSubType(inputSubType.getId(), "UpdatedSubType");

        assertNotNull(updatedInputSubType);
        assertNotNull(updatedInputSubType.getId());
        assertTrue(updatedInputSubType.getId() > 0);
        assertEquals(updatedInputSubType.getName(), "UpdatedSubType");
        assertEquals(updatedInputSubType.getType().getId(), expenseType.getId());
    }

    /**
     * Test that input subtype was updated for inputtype using
     * findAllInputSubTypes(type.id)
     * Should return fully fleshed and updated inputsubtype object with lighweight
     * inputtype(id only)
     */
    @Test
    void testUpdateInputSubTypeAndVerify() {
        UserDetails userDetails = new UserDetails("David", "dvidalolz@gmail.com", "testpassword");
        User user = userService.createUser(userDetails);
        List<InputType> userInputTypes = inputTypeService.findAllInputTypesByUserId(user.getId());
        InputType expenseType = userInputTypes.get(0);
        InputSubType inputSubType = inputTypeService.createInputSubType(expenseType.getId(), "TestSubType");

        inputTypeService.updateInputSubType(inputSubType.getId(), "UpdatedSubType");

        List<InputSubType> updatedInputSubTypes = inputTypeService.findAllInputSubTypesByTypeId(expenseType.getId());
        assertTrue(updatedInputSubTypes.size() == 1);
        assertTrue(updatedInputSubTypes.stream().anyMatch(type -> "UpdatedSubType".equals(type.getName())));
        assertFalse(updatedInputSubTypes.stream().anyMatch(type -> "TestSubType".equals(type.getName())));
    }

    /**
     * Test deleteInputTypeByTypeId(typeId) : should delete that input type
     * Check using findAllInputTypesByUserId()
     */
    @Test
    void testDeleteInputSubType() {
        UserDetails userDetails = new UserDetails("David", "dvidalolz@gmail.com", "testpassword");
        User user = userService.createUser(userDetails);
        List<InputType> userInputTypes = inputTypeService.findAllInputTypesByUserId(user.getId());
        InputType expenseType = userInputTypes.get(0);
        InputSubType inputSubType = inputTypeService.createInputSubType(expenseType.getId(), "TestSubType");
        inputTypeService.updateInputSubType(inputSubType.getId(), "UpdatedSubType");

        inputTypeService.deleteInputSubType(inputSubType.getId());
        List<InputSubType> inputSubTypesWithDeletion = inputTypeService
                .findAllInputSubTypesByTypeId(expenseType.getId());

        assertFalse(inputSubTypesWithDeletion.stream().anyMatch(type -> "UpdatedSubType".equals(type.getName())));

        assertThrows(Exception.class, () -> {
            inputTypeService.deleteInputSubType(inputSubType.getId() + 50);
        });
    }

}
