package io.spring.training.corespring.personalbudgettracker.inputs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;

import io.spring.training.corespring.personalbudgettracker.TestInfrastructureConfig;
import io.spring.training.corespring.personalbudgettracker.common.date.SimpleDate;
import io.spring.training.corespring.personalbudgettracker.common.money.MonetaryAmount;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.InputService;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.InputTypeService;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.UserService;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.exceptions.InputExceptions.InputCreationException;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.exceptions.InputExceptions.InputRetrievalException;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.input.Input;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.input_subtype.InputSubType;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.input_type.InputType;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.user.User;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.user.UserDetails;


@SpringJUnitConfig(TestInfrastructureConfig.class)
@ActiveProfiles({"jdbc", "test"})
@Transactional
public class InputServiceTests {

    @Autowired
    private InputService inputService;

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
        List<InputType> userInputTypes = inputTypeService.findAllInputTypesByUserId(testUser.getId());
        testType = userInputTypes.get(0);
    }


    /**
     * Test addInputToUser(userId, input)
     * Should return a fully fleshed input with lightweight object attributes
     */
    @Test
    void testAddInputToUser() {

        InputSubType grocerySubType = inputTypeService.addInputSubType(testType.getId(), "Grocery");

        Input input = new Input(new MonetaryAmount(100.00), new SimpleDate(10, 18, 1993), testUser, testType,
                grocerySubType);
        input = inputService.addInputToUser(testUser.getId(), input);

        assertNotNull(input);
        assertNotNull(input.getId());
        assertTrue(input.getId() > 0);
        assertEquals(input.getAmount().asDouble(), 100.00);
        assertEquals(input.getType().getName(), "Expense");
        assertEquals(input.getSubtype().getName(), "Grocery");
        assertEquals(input.getDate(), new SimpleDate(10, 18, 1993));
        assertEquals(input.getUser(), testUser);
    }

    /**
     * Test that attempting to add input to non-existent user throws error
     */
    @Test
    void testAddInputToNonExistingUser() {
        Input input = new Input(new MonetaryAmount(100.00), new SimpleDate(10, 18, 1993), testUser, testType);

        assertThrows(InputCreationException.class, () -> {
            inputService.addInputToUser(testUser.getId() + 50, input);
        });
    }

    /**
     * Test getInputsByUserId and by doing so ensure that inputrepository holds
     * above-created inpput
     * Should return a list of inputs associated with UserId
     */
    @Test
    void testGetInputsByUserId() {
        InputSubType grocerySubType = inputTypeService.addInputSubType(testType.getId(), "Grocery");

        List<Input> inputList = new ArrayList<>();
        Input input = new Input(new MonetaryAmount(100.00), new SimpleDate(10, 18, 1993), testUser, testType,
                grocerySubType);
        inputList.add(input);
        inputService.addInputToUser(testUser.getId(), input);

        for (int i = 0; i < 9; i++) {
            Input newInput = new Input(new MonetaryAmount(i), new SimpleDate(i, 18, 1993), testUser, testType,
                    grocerySubType);
            inputList.add(newInput);
            inputService.addInputToUser(testUser.getId(), newInput);
        }

        List<Input> retrievedInputList = inputService.findInputsByUserId(testUser.getId());
        assertTrue(retrievedInputList.containsAll(inputList));


        // Test exceptions
        assertThrows(InputRetrievalException.class, () -> {
            inputService.findInputsByUserId(testUser.getId() + 50);
        });
        
    }

}
