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

    @Test
    void testInputTypeServices() {
        /**
         * Test createInputType : Creates input type for associated user
         * Should return fully fleshed inputtype with lightweight user (id only)
         * Should save the inputtypes in inputtyperepository for user
         * Throws exception for if user not found
         */
        UserDetails userDetails = new UserDetails("David", "dvidalolz@gmail.com", "testpassword");
        User user = userService.createUser(userDetails);
        InputType inputType = inputTypeService.createInputTypeForUser(user.getId(), "TestType");
        // Test returned input type
        assertNotNull(inputType);
        assertNotNull(inputType.getId());
        assertTrue(inputType.getId() > 0);
        assertEquals(inputType.getName(), "TestType");
        assertEquals(inputType.getUser().getId(), user.getId());

        /**
         * Test findAllInputTypesByUserId(userId), and in doing so, test that above
         * inputtype was
         * properly associated with above created user
         */
        // Retrieve list of inputtypes from userservice to ensure it holds newly created
        // inputtype
        List<InputType> userInputTypes = inputTypeService.findAllInputTypesByUserId(user.getId());
        assertTrue(userInputTypes.size() == 3); // Newly created user holds 3 inputtypes (expense, income default)
        assertTrue(userInputTypes.stream().anyMatch(type -> "TestType".equals(type.getName())));

        // Test all inputtypes come fully fleshed
        for (InputType it : userInputTypes) {
            assertNotNull(it.getId());
            assertNotNull(it.getName());
            assertNotNull(it.getUser());
            assertNotNull(it.getUser().getId()); // user should be lightweight (id only)
            assertNull(it.getUser().getUsername());
        }

        // Test user not found error
        assertThrows(Exception.class, () -> {
            inputTypeService.createInputTypeForUser(user.getId() + 50, "ErrorType");
        });

        /**
         * Test updateInputType(typeId, newTypeName)
         * Should return fully fleshed inputtype object with lightweight user
         */
        InputType updatedInputType = inputTypeService.updateInputType(inputType.getId(), "UpdatedTestType");
        // Tested updated and returned inputtype
        assertNotNull(updatedInputType);
        assertNotNull(updatedInputType.getId());
        assertTrue(updatedInputType.getId() > 0);
        assertEquals(updatedInputType.getName(), "UpdatedTestType");
        assertEquals(updatedInputType.getUser().getId(), user.getId());

        /**
         * Test that inputtype was updated for user using findAllByUserId()
         * Should return fully fleshed and updated inputtype object with lightweightuser
         */
        List<InputType> updatedUserInputTypes = inputTypeService.findAllInputTypesByUserId(user.getId());
        // newly created user holds 3 input types (expense, income default)
        assertTrue(updatedUserInputTypes.size() == 3); 
        // Test updated test type exist
        assertTrue(updatedUserInputTypes.stream().anyMatch(type -> "UpdatedTestType".equals(type.getName()))); 
        assertTrue(updatedUserInputTypes.stream().anyMatch(type -> "Expense".equals(type.getName())));
        assertTrue(updatedUserInputTypes.stream().anyMatch(type -> "Income".equals(type.getName())));
        // test pre-update type no longer exist
        assertFalse(updatedUserInputTypes.stream().anyMatch(type -> "TestType".equals(type.getName()))); 

        /**
         * Test deleteInputTypeByTypeId(typeId) : should delete that input type
         * Check using findAllInputTypesByUserId()
         */
        inputTypeService.deleteInputTypeById(updatedInputType.getId());
        List<InputType> inputTypesWithDeletion = inputTypeService
                .findAllInputTypesByUserId(updatedInputType.getUser().getId());
        // Test deleted type is gone
        assertFalse(inputTypesWithDeletion.stream().anyMatch(type -> "UpdatedTestType".equals(type.getName()))); 
        assertTrue(inputTypesWithDeletion.stream().anyMatch(type -> "Expense".equals(type.getName())));
        assertTrue(inputTypesWithDeletion.stream().anyMatch(type -> "Income".equals(type.getName())));
        // Test delete throws error on nonexistent inputtype
        assertThrows(Exception.class, () -> {
            inputTypeService.deleteInputTypeById(updatedInputType.getId() + 50);
        });

    }

    @Test
    void testInputSubTypeServices() {
        /**
         * Test create input subtype
         * Should return fully fleshed inputsubtype object with lightweight inputtype(id
         * only)
         */
        // Create dummy user to use associated default input types (expense and income)
        UserDetails userDetails = new UserDetails("David", "dvidalolz@gmail.com", "testpassword");
        User user = userService.createUser(userDetails);
        List<InputType> userInputTypes = inputTypeService.findAllInputTypesByUserId(user.getId());
        // Extract expense inputtype and use to create subtype for it
        InputType expenseType = userInputTypes.get(0);
        InputSubType inputSubType = inputTypeService.createInputSubType(expenseType.getId(), "TestSubType");
        // Test returned inputsubtype
        assertNotNull(inputSubType);
        assertNotNull(inputSubType.getId());
        assertTrue(inputSubType.getId() > 0);
        assertEquals(inputSubType.getName(), "TestSubType");
        assertEquals(inputSubType.getType().getId(), expenseType.getId()); // id only (bi directional)

        /**
         * Test findAllInputSubTypes by typeId, and in doing so, test that the above
         * created subtype was properly associated with inputtype
         */
        // Retrieve list of input subtypes to ensure inputservice holds newly created
        // subtype
        List<InputSubType> typeSubTypes = inputTypeService.findAllInputSubTypesByTypeId(expenseType.getId());
        assertTrue(typeSubTypes.size() == 1);
        assertTrue(typeSubTypes.stream().anyMatch(type -> "TestSubType".equals(type.getName())));
        // Test Subtypes come fully flesh with lightweight type (id only)
        InputSubType retrievedSubType = typeSubTypes.get(0);
        assertEquals(retrievedSubType.getId(), inputSubType.getId());
        assertEquals(retrievedSubType.getName(), inputSubType.getName());
        assertEquals(retrievedSubType.getType(), inputSubType.getType());

        // Test input subtype not found error(inputtype id not found)
        assertThrows(Exception.class, () -> {
            inputTypeService.createInputSubType(expenseType.getId() + 50, "ErrorType");
        });

        /**
         * Test updateInputSubType(subtypeId, newSubTypeName)
         * Should return fully fleshed inputsubtype object with lightweight inputtype
         * (id only)
         */
        InputSubType updatedInputSubType = inputTypeService.updateInputSubType(inputSubType.getId(), "UpdatedSubType");
        // Test updated inputsubtype
        assertNotNull(updatedInputSubType);
        assertNotNull(updatedInputSubType.getId());
        assertTrue(updatedInputSubType.getId() > 0);
        assertEquals(updatedInputSubType.getName(), "UpdatedSubType");
        assertEquals(updatedInputSubType.getType().getId(), expenseType.getId());

        /**
         * Test that input subtype was updated for inputtype using
         * findAllInputSubTypes(type.id)
         * Should return fully fleshed and updated inputsubtype object with lighweight
         * inputtype(id only)
         */
        List<InputSubType> updatedInputSubTypes = inputTypeService.findAllInputSubTypesByTypeId(expenseType.getId());
        assertTrue(updatedInputSubTypes.size() == 1);
        // Test updated subtype exist
        assertTrue(updatedInputSubTypes.stream().anyMatch(type -> "UpdatedSubType".equals(type.getName())));
        // test pre-update subtype no longer exist                                                                                                  
        assertFalse(updatedInputSubTypes.stream().anyMatch(type -> "TestSubType".equals(type.getName())));

        /**
         * Test deleteInputTypeByTypeId(typeId) : should delete that input type
         * Check using findAllInputTypesByUserId()
         */
        inputTypeService.deleteInputSubType(inputSubType.getId());
        List<InputSubType> inputSubypesWithDeletion = inputTypeService.findAllInputSubTypesByTypeId(expenseType.getId());
        // test deleted type is gone
        assertFalse(inputSubypesWithDeletion.stream().anyMatch(type -> "UpdatedSubType".equals(type.getName()))); 
        // assert delete on nonexistent subtype throws error
        assertThrows(Exception.class, () -> {
            inputTypeService.deleteInputSubType(inputSubType.getId() + 50);
        });


    }


}
