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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;

import io.spring.training.corespring.personalbudgettracker.TestInfrastructureConfig;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.InputTypeService;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.UserService;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.exceptions.InputTypeExceptions.InputSubTypeCreationException;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.exceptions.InputTypeExceptions.InputSubTypeDeletionException;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.exceptions.InputTypeExceptions.InputSubTypeNotFoundException;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.exceptions.InputTypeExceptions.InputSubTypeRetrievalException;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.exceptions.InputTypeExceptions.InputTypeCreationException;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.exceptions.InputTypeExceptions.InputTypeDeletionException;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.exceptions.InputTypeExceptions.InputTypeNotFoundException;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.exceptions.InputTypeExceptions.InputTypeRetrievalException;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.input_subtype.InputSubType;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.input_type.InputType;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.user.User;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.user.UserDetails;


@SpringJUnitConfig(TestInfrastructureConfig.class)
@ActiveProfiles({"jdbc", "test"})
@Transactional
public class InputTypeServiceTests {

    @Autowired
    private UserService userService;

    @Autowired
    private InputTypeService inputTypeService;

    private static User testUser;

    private static InputType testType;

    @BeforeEach
    public void setUp() {
        UserDetails userDetails = new UserDetails("David", "dvidalolz@gmail.com", "TestPassword");
        testUser = userService.addUser(userDetails);
        testType = inputTypeService.addInputTypeForUser(testUser.getId(), "TestType");
    }

    /**
     * Test createInputType : Creates input type for associated user
     * Should return fully fleshed inputtype with lightweight user (id only)
     * Should save the inputtypes in inputtyperepository for user
     * Throws exception for if user not found
     */
    @Test
    void testCreateInputType() {
        assertNotNull(testType);
        assertNotNull(testType.getId());
        assertTrue(testType.getId() > 0);
        assertEquals(testType.getName(), "TestType");
        assertEquals(testType.getUser().getId(), testUser.getId());
    }

    /**
     * Test findAllInputTypesByUserId(userId), and in doing so, test that above
     * inputtype was
     * properly associated with above created user
     */
    @Test
    void testFindAllInputTypesByUserId() {

        List<InputType> userInputTypes = inputTypeService.findAllInputTypesByUserId(testUser.getId());
        assertTrue(userInputTypes.size() == 3);
        assertTrue(userInputTypes.stream().anyMatch(type -> "TestType".equals(type.getName())));

        for (InputType it : userInputTypes) {
            assertNotNull(it.getId());
            assertNotNull(it.getName());
            assertNotNull(it.getUser());
            assertNotNull(it.getUser().getId());
            assertNull(it.getUser().getUsername());
        }

        // Exception test : retrieve for nonexistent user
        assertThrows(InputTypeRetrievalException.class, () -> {
            inputTypeService.findAllInputTypesByUserId(testUser.getId() + 10);
        });
        
    }

    /**
     * Test that error is thrown for attempting to create inputtype for non-existent
     * user
     */
    @Test
    void testCreateInputTypeForNonExistingUser() {

        assertThrows(InputTypeCreationException.class, () -> {
            inputTypeService.addInputTypeForUser(testUser.getId() + 50, "ErrorType");
        });
    }

    /**
     * Test updateInputType(typeId, newTypeName)
     * Should return fully fleshed inputtype object with lightweight user
     */
    @Test
    void testUpdateInputType() {
        InputType updatedInputType = inputTypeService.updateInputType(testType.getId(), "UpdatedTestType");

        assertNotNull(updatedInputType);
        assertNotNull(updatedInputType.getId());
        assertTrue(updatedInputType.getId() > 0);
        assertEquals(updatedInputType.getName(), "UpdatedTestType");
        assertEquals(updatedInputType.getUser().getId(), testUser.getId());

        // Exception test : should throw exception if attempting to update non-existent type
        assertThrows(InputTypeNotFoundException.class, () -> {
            inputTypeService.updateInputType(testType.getId() + 50, "ErrorType");
        });
    }

    /**
     * Test that inputtype was updated for user using findAllByUserId()
     * Should return fully fleshed and updated inputtype object with lightweight user
     */
    @Test
    void testUpdateInputTypeAndVerify() {

        inputTypeService.updateInputType(testType.getId(), "UpdatedTestType");

        List<InputType> updatedUserInputTypes = inputTypeService.findAllInputTypesByUserId(testUser.getId());
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
        inputTypeService.updateInputType(testType.getId(), "UpdatedTestType");

        inputTypeService.deleteInputTypeById(testType.getId());
        List<InputType> inputTypesWithDeletion = inputTypeService.findAllInputTypesByUserId(testUser.getId());

        assertFalse(inputTypesWithDeletion.stream().anyMatch(type -> "UpdatedTestType".equals(type.getName())));
        assertTrue(inputTypesWithDeletion.stream().anyMatch(type -> "Expense".equals(type.getName())));
        assertTrue(inputTypesWithDeletion.stream().anyMatch(type -> "Income".equals(type.getName())));

        assertThrows(InputTypeDeletionException.class, () -> {
            inputTypeService.deleteInputTypeById(testType.getId() + 50);
        });
    }

    /**
     * Test create input subtype
     * Should return fully fleshed inputsubtype object with lightweight inputtype(id
     * only)
     */
    @Test
    void testCreateInputSubType() {
        List<InputType> userInputTypes = inputTypeService.findAllInputTypesByUserId(testUser.getId());
        InputType expenseType = userInputTypes.get(0);

        InputSubType inputSubType = inputTypeService.addInputSubType(expenseType.getId(), "TestSubType");

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
        List<InputType> userInputTypes = inputTypeService.findAllInputTypesByUserId(testUser.getId());
        InputType expenseType = userInputTypes.get(0);
        inputTypeService.addInputSubType(expenseType.getId(), "TestSubType");

        List<InputSubType> typeSubTypes = inputTypeService.findAllInputSubTypesByTypeId(expenseType.getId());
        assertTrue(typeSubTypes.size() == 1);
        assertTrue(typeSubTypes.stream().anyMatch(type -> "TestSubType".equals(type.getName())));

        InputSubType retrievedSubType = typeSubTypes.get(0);
        assertEquals(retrievedSubType.getName(), "TestSubType");
        assertEquals(retrievedSubType.getType(), expenseType);

        // Exception testing : attempt findAll for typeId which doesn't exist
        assertThrows(InputSubTypeRetrievalException.class, () -> {
            inputTypeService.findAllInputSubTypesByTypeId(expenseType.getId() + 10);
        });
    }

    /**
     * Test that error is thrown for attempting to create input subtype
     * for non-existent user
     */
    @Test
    void testCreateInputSubTypeForNonExistingInputType() {
        List<InputType> userInputTypes = inputTypeService.findAllInputTypesByUserId(testUser.getId());
        InputType expenseType = userInputTypes.get(0);

        assertThrows(InputSubTypeCreationException.class, () -> {
            inputTypeService.addInputSubType(expenseType.getId() + 50, "ErrorType");
        });
    }

    /**
     * Test updateInputSubType(subtypeId, newSubTypeName)
     * Should return fully fleshed inputsubtype object with lightweight inputtype
     * (id only)
     */
    @Test
    void testUpdateInputSubType() {
        List<InputType> userInputTypes = inputTypeService.findAllInputTypesByUserId(testUser.getId());
        InputType expenseType = userInputTypes.get(0);
        InputSubType inputSubType = inputTypeService.addInputSubType(expenseType.getId(), "TestSubType");

        InputSubType updatedInputSubType = inputTypeService.updateInputSubType(inputSubType.getId(), "UpdatedSubType");

        assertNotNull(updatedInputSubType);
        assertNotNull(updatedInputSubType.getId());
        assertTrue(updatedInputSubType.getId() > 0);
        assertEquals(updatedInputSubType.getName(), "UpdatedSubType");
        assertEquals(updatedInputSubType.getType().getId(), expenseType.getId());

        // Exception testing : exception thown if update attempted on non existent subtype
        assertThrows(InputSubTypeNotFoundException.class, () -> {
            inputTypeService.updateInputSubType(inputSubType.getId() + 10, "Error");
        });
    }

    /**
     * Test that input subtype was updated for inputtype using
     * findAllInputSubTypes(type.id)
     * Should return fully fleshed and updated inputsubtype object with lighweight
     * inputtype(id only)
     */
    @Test
    void testUpdateInputSubTypeAndVerify() {
        List<InputType> userInputTypes = inputTypeService.findAllInputTypesByUserId(testUser.getId());
        InputType expenseType = userInputTypes.get(0);
        InputSubType inputSubType = inputTypeService.addInputSubType(expenseType.getId(), "TestSubType");

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
        // Retrieve expense type for given testUser
        List<InputType> userInputTypes = inputTypeService.findAllInputTypesByUserId(testUser.getId());
        InputType expenseType = userInputTypes.get(0);
        // Create input subtype for type
        InputSubType inputSubType = inputTypeService.addInputSubType(expenseType.getId(), "TestSubType");
        
        // Confirm created inputsubtype exists
        List<InputSubType> retrievedSubTypes = inputTypeService.findAllInputSubTypesByTypeId(expenseType.getId());
        assertEquals(retrievedSubTypes.get(0).getName(), "TestSubType");

        // Delete
        inputTypeService.deleteInputSubType(inputSubType.getId());

        // Assert find input subtype that just got deleted will throw exception
        assertThrows(InputSubTypeRetrievalException.class, () -> {
            inputTypeService.findAllInputSubTypesByTypeId(expenseType.getId());
        });

        // Assert attempting to deleted subtype which was just deleted throws exception
        assertThrows(InputSubTypeDeletionException.class, () -> {
            inputTypeService.deleteInputSubType(inputSubType.getId());
        });
    }

}
