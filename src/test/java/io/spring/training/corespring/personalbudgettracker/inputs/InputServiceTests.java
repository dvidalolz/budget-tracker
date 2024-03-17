package io.spring.training.corespring.personalbudgettracker.inputs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

import io.spring.training.corespring.personalbudgettracker.TestInfrastructureConfig;
import io.spring.training.corespring.personalbudgettracker.common.date.SimpleDate;
import io.spring.training.corespring.personalbudgettracker.common.money.MonetaryAmount;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.InputService;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.InputTypeService;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.UserService;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.input.Input;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.input_subtype.InputSubType;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.input_type.InputType;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.user.User;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.user.UserDetails;

public class InputServiceTests {

    private InputService inputService;
    private UserService userService;
    private InputTypeService inputTypeService;

    @BeforeEach
    void setUp() throws Exception {
        ApplicationContext context = SpringApplication.run(TestInfrastructureConfig.class);
        inputService = context.getBean(InputService.class);
        userService = context.getBean(UserService.class);
        inputTypeService = context.getBean(InputTypeService.class);
    }

    /**
     * Test addInputToUser(userId, input)
     * Should return a fully fleshed input with lightweight object attributes
     */
    @Test
    void testAddInputToUser() {
        UserDetails userDetails = new UserDetails("David", "dvidalolz@gmail.com", "TestPassword");
        User user = userService.createUser(userDetails);
        List<InputType> userInputTypes = inputTypeService.findAllInputTypesByUserId(user.getId());
        InputType expenseType = userInputTypes.get(0);
        InputSubType grocerySubType = inputTypeService.createInputSubType(expenseType.getId(), "Grocery");

        Input input = new Input(new MonetaryAmount(100.00), new SimpleDate(10, 18, 1993), user, expenseType,
                grocerySubType);
        input = inputService.addInputToUser(user.getId(), input);

        assertNotNull(input);
        assertNotNull(input.getId());
        assertTrue(input.getId() > 0);
        assertEquals(input.getAmount().asDouble(), 100.00);
        assertEquals(input.getType().getName(), "Expense");
        assertEquals(input.getSubtype().getName(), "Grocery");
        assertEquals(input.getDate(), new SimpleDate(10, 18, 1993));
        assertEquals(input.getUser(), user);
    }

    /**
     * Test that attempting to add input to non-existent user throws error
     */
    @Test
    void testAddInputToNonExistingUser() {
        UserDetails userDetails = new UserDetails("David", "dvidalolz@gmail.com", "TestPassword");
        User user = userService.createUser(userDetails);
        Input errorInput = new Input();

        assertThrows(Exception.class, () -> {
            inputService.addInputToUser(user.getId() + 50, errorInput);
        });
    }

    /**
     * Test getInputsByUserId and by doing so ensure that inputrepository holds
     * above-created inpput
     * Should return a list of inputs associated with UserId
     */
    @Test
    void testGetInputsByUserId() {
        UserDetails userDetails = new UserDetails("David", "dvidalolz@gmail.com", "TestPassword");
        User user = userService.createUser(userDetails);
        List<InputType> userInputTypes = inputTypeService.findAllInputTypesByUserId(user.getId());
        InputType expenseType = userInputTypes.get(0);
        InputSubType grocerySubType = inputTypeService.createInputSubType(expenseType.getId(), "Grocery");

        List<Input> inputList = new ArrayList<>();
        Input input = new Input(new MonetaryAmount(100.00), new SimpleDate(10, 18, 1993), user, expenseType,
                grocerySubType);
        inputList.add(input);
        inputService.addInputToUser(user.getId(), input);

        for (int i = 0; i < 9; i++) {
            Input newInput = new Input(new MonetaryAmount(i), new SimpleDate(i, 18, 1993), user, expenseType,
                    grocerySubType);
            inputList.add(newInput);
            inputService.addInputToUser(user.getId(), newInput);
        }

        List<Input> retrievedInputList = inputService.getInputsByUserId(user.getId());
        assertEquals(retrievedInputList.size(), inputList.size());
        assertTrue(retrievedInputList.containsAll(inputList));
    }

}
