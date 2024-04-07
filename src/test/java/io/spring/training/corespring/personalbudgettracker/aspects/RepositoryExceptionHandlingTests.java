package io.spring.training.corespring.personalbudgettracker.aspects;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import io.spring.training.corespring.personalbudgettracker.CaptureSystemOutput;
import io.spring.training.corespring.personalbudgettracker.CaptureSystemOutput.OutputCapture;
import io.spring.training.corespring.personalbudgettracker.TestInfrastructureConfig;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.aspects.RepositoryExceptionHandlingAspect;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.aspects.RepositoryLoggingAspect;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.exceptions.InputExceptions;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.exceptions.InputTypeExceptions;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.exceptions.UserExceptions;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.input.Input;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.input.InputRepository;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.input_subtype.InputSubType;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.input_subtype.InputSubTypeRepository;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.input_type.InputType;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.input_type.InputTypeRepository;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.user.User;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.user.UserDetails;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.user.UserRepository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { TestInfrastructureConfig.class })
@EnableAutoConfiguration
@ActiveProfiles({"jdbc", "test"})
public class RepositoryExceptionHandlingTests {

    @Autowired
    UserRepository userRepository;

    @Autowired
    InputRepository inputRepository;

    @Autowired
    InputTypeRepository inputTypeRepository;

    @Autowired
    InputSubTypeRepository inputSubTypeRepository;

    /**
     * Saving duplicate user should throw and log runtimeExceptions as log.error()
     */
    @Test
    @CaptureSystemOutput
    public void testUserSaveExceptionLogs(OutputCapture capture) {
        // First save to persist a new user
        UserDetails userDetails = new UserDetails("David", "david@example.com", "Password");
        User newUser = User.fromUserDetails(userDetails);
        User savedUser = userRepository.save(newUser);

        // Ensure the user was saved
        assertNotNull(savedUser.getId());
        assertEquals("David", savedUser.getUsername());

        // Attempt to save another user with the same username, which should trigger the
        // unique constraint violation
        User duplicateUser = new User();
        duplicateUser.setUsername("David");
        duplicateUser.setPasswordHash("anotherPassword");
        duplicateUser.setEmail("anotherEmail@example.com");

        // This should throw the unique constraint violation exception
        assertThrows(UserExceptions.UserSaveException.class, () -> {
            userRepository.save(duplicateUser);
        });

        // Assert that aspect logs failure message
        String consoleOutput = capture.toString();
        assertThat(consoleOutput,
                containsString(RepositoryExceptionHandlingAspect.FAILURE_MSG));

        // Assert that it is an ERROR
        assertTrue(consoleOutput.contains("ERROR"));
        // Assert that aspect is referenced
        assertTrue(consoleOutput.contains("user_input.internal.aspects.RepositoryExceptionHandlingAspect"));

        // Assert that logging aspect is logging appropriately
        assertThat(consoleOutput, containsString(RepositoryLoggingAspect.AROUND));
        assertTrue(consoleOutput.contains("user_input.internal.aspects.RepositoryLoggingAspect"));

    }

    /**
     * Attempting to find user by non-existent id should log an empty optional as
     * log.warn()
     */
    @Test
    @CaptureSystemOutput
    public void testUserFindByIdExceptionLogs(OutputCapture capture) {
        Optional<User> nonExistentIdUser = userRepository.findById(Long.MAX_VALUE);
        // Assert that the result is empty
        assertFalse(nonExistentIdUser.isPresent());

        // Assert that aspect logs message not found
        String consoleOutput = capture.toString();
        assertThat(consoleOutput,
                containsString(RepositoryExceptionHandlingAspect.NOT_FOUND_MSG));

        // Assert that it is a WARN
        assertTrue(consoleOutput.contains("WARN"));
        // Assert that aspect is referenced
        assertTrue(consoleOutput.contains("user_input.internal.aspects.RepositoryExceptionHandlingAspect"));

        // Assert that logging aspect is logging appropriately
        assertThat(consoleOutput, containsString(RepositoryLoggingAspect.BEFORE));
        assertTrue(consoleOutput.contains("user_input.internal.aspects.RepositoryLoggingAspect"));

    }

    /**
     * Attempting to find user by non-existent username should log an empty optional
     * as
     * log.warn()
     */
    @Test
    @CaptureSystemOutput
    public void testUserFindByNameExceptionLogs(OutputCapture capture) {
        Optional<User> nonExistentNameUser = userRepository.findByUsername("NonExistentUser");
        // Assert that the result is empty
        assertFalse(nonExistentNameUser.isPresent());

        // Assert that aspect logs message not found
        String consoleOutput = capture.toString();
        assertThat(consoleOutput,
                containsString(RepositoryExceptionHandlingAspect.NOT_FOUND_MSG));

        // Assert that it is a WARN
        assertTrue(consoleOutput.contains("WARN"));
        // Assert that aspect is referenced
        assertTrue(consoleOutput.contains("user_input.internal.aspects.RepositoryExceptionHandlingAspect"));

        // Assert that logging aspect is logging appropriately
        assertThat(consoleOutput, containsString(RepositoryLoggingAspect.BEFORE));
        assertTrue(consoleOutput.contains("user_input.internal.aspects.RepositoryLoggingAspect"));
    }

    /**
     * Attempting to delete non-existent user should log thrown exception as
     * log.error()
     */
    @Test
    @CaptureSystemOutput
    public void testUserDeleteByIdExceptionLogs(OutputCapture capture) {
        // Assert that deleting non-existent user throws error
        assertThrows(UserExceptions.UserDeletionException.class, () -> {
            userRepository.deleteById(Long.MAX_VALUE);
        });
        // Asert that user not found error is logged
        String consoleOutput = capture.toString();
        assertThat(consoleOutput,
                containsString(RepositoryExceptionHandlingAspect.FAILURE_MSG));

        // Assert that it is an ERROR
        assertTrue(consoleOutput.contains("ERROR"));
        // Assert that aspect is referenced
        assertTrue(consoleOutput.contains("user_input.internal.aspects.RepositoryExceptionHandlingAspect"));

        // Assert that logging aspect is logging appropriately
        assertThat(consoleOutput, containsString(RepositoryLoggingAspect.AFTER));
        assertTrue(consoleOutput.contains("user_input.internal.aspects.RepositoryLoggingAspect"));
    }

    /**
     * Attempting to save duplicate input should log exception thrown as log.error()
     */
    @Test
    @CaptureSystemOutput
    public void testInputTypeSaveExceptionLog(OutputCapture capture) {
        // Create a new user for the test to ensure a clean state
        UserDetails userDetails = new UserDetails("testUser", "testUser@example.com", "testPassword");
        User user = User.fromUserDetails(userDetails);
        user = userRepository.save(user); // Save the user to the database

        // Create the first InputType
        InputType inputType1 = new InputType("Expense", user);
        inputTypeRepository.save(inputType1); // This should succeed

        // Attempt to create a second InputType with the same type_name and user_id
        InputType inputType2 = new InputType("Expense", user);

        // Expecting an exception due to the unique constraint violation
        assertThrows(InputTypeExceptions.InputTypeSaveException.class, () -> inputTypeRepository.save(inputType2));

        // Assert that aspect logs failure message
        String consoleOutput = capture.toString();
        assertThat(consoleOutput,
                containsString(RepositoryExceptionHandlingAspect.FAILURE_MSG));

        // Assert that it is an ERROR
        assertTrue(consoleOutput.contains("ERROR"));
        // Assert that aspect is referenced
        assertTrue(consoleOutput.contains("user_input.internal.aspects.RepositoryExceptionHandlingAspect"));

        // Assert that logging aspect is logging appropriately
        assertThat(consoleOutput, containsString(RepositoryLoggingAspect.AROUND));
        assertTrue(consoleOutput.contains("user_input.internal.aspects.RepositoryLoggingAspect"));
    }

    /**
     * Attempting to find all input types of non-existent user should log empty list
     * exception
     */
    @Test
    @CaptureSystemOutput
    public void testInputTypeFindAllByUserIdExceptionLogs(OutputCapture capture) {
        // Attempt to find input types of non existent user (should return empty list)
        List<InputType> inputTypes = inputTypeRepository.findAllByUserId(Long.MAX_VALUE);
        assertTrue(inputTypes.isEmpty());

        // Assert that aspect logs not found message
        String consoleOutput = capture.toString();
        assertThat(consoleOutput,
                containsString(RepositoryExceptionHandlingAspect.NOT_FOUND_MSG));

        // Assert that it is an ERROR
        assertTrue(consoleOutput.contains("WARN"));
        // Assert that aspect is referenced
        assertTrue(consoleOutput.contains("user_input.internal.aspects.RepositoryExceptionHandlingAspect"));

        // Assert that logging aspect is logging appropriately
        assertThat(consoleOutput, containsString(RepositoryLoggingAspect.BEFORE));
        assertTrue(consoleOutput.contains("user_input.internal.aspects.RepositoryLoggingAspect"));
    }

    /**
     * Attempting to find input type of non-existent id should log empty optional
     * exception
     */
    @Test
    @CaptureSystemOutput
    public void testInputTypeFindByIdExceptionLogs(OutputCapture capture) {
        // Attempt to find non existent inputtype, should return empty optional
        Optional<InputType> inputType = inputTypeRepository.findById(Long.MAX_VALUE);
        assertFalse(inputType.isPresent());

        // Assert that aspect logs not found message
        String consoleOutput = capture.toString();
        assertThat(consoleOutput,
                containsString(RepositoryExceptionHandlingAspect.NOT_FOUND_MSG));

        // Assert that it is an ERROR
        assertTrue(consoleOutput.contains("WARN"));
        // Assert that aspect is referenced
        assertTrue(consoleOutput.contains("user_input.internal.aspects.RepositoryExceptionHandlingAspect"));

        // Assert that logging aspect is logging appropriately
        assertThat(consoleOutput, containsString(RepositoryLoggingAspect.BEFORE));
        assertTrue(consoleOutput.contains("user_input.internal.aspects.RepositoryLoggingAspect"));
    }

    /**
     * Attempting to delete non-existent inputtype should log thrown exception as
     * log.error()
     */
    @Test
    @CaptureSystemOutput
    public void testInputTypeDeleteByIdExceptionLogs(OutputCapture capture) {
        // Assert that deleting non-existent inputtype throws error
        assertThrows(InputTypeExceptions.InputTypeDeletionException.class, () -> {
            inputTypeRepository.deleteById(Long.MAX_VALUE);
        });
        // Asert that inputtype not found error is logged
        String consoleOutput = capture.toString();
        assertThat(consoleOutput,
                containsString(RepositoryExceptionHandlingAspect.FAILURE_MSG));

        // Assert that it is an ERROR
        assertTrue(consoleOutput.contains("ERROR"));
        // Assert that aspect is referenced
        assertTrue(consoleOutput.contains("user_input.internal.aspects.RepositoryExceptionHandlingAspect"));

        // Assert that logging aspect is logging appropriately
        assertThat(consoleOutput, containsString(RepositoryLoggingAspect.AFTER));
        assertTrue(consoleOutput.contains("user_input.internal.aspects.RepositoryLoggingAspect"));
    }

    /**
     * Attempting to save duplicate input subtype should log exception thrown as
     * log.error()
     */
    @Test
    @CaptureSystemOutput
    public void testInputSubTypeSaveExceptionLog(OutputCapture capture) {
        // Create a new user for the test to ensure a clean state
        UserDetails userDetails = new UserDetails("testieUser", "testieUser@example.com", "testiePassword");
        User user = User.fromUserDetails(userDetails);
        user = userRepository.save(user); // Save the user to the database

        // Create nputType
        InputType inputType = new InputType("Savings", user);
        inputType = inputTypeRepository.save(inputType); // This should succeed

        // Create the first inputSubType
        InputSubType inputSubType1 = new InputSubType("IRA", inputType);
        inputSubTypeRepository.save(inputSubType1);

        // Create duplicate subtype
        InputSubType inputSubType2 = new InputSubType("IRA", inputType);
        // Expecting an exception due to the unique constraint violation
        assertThrows(InputTypeExceptions.InputSubTypeSaveException.class,
                () -> inputSubTypeRepository.save(inputSubType2));

        // Assert that aspect logs failure message
        String consoleOutput = capture.toString();
        assertThat(consoleOutput,
                containsString(RepositoryExceptionHandlingAspect.FAILURE_MSG));

        // Assert that it is an ERROR
        assertTrue(consoleOutput.contains("ERROR"));
        // Assert that aspect is referenced
        assertTrue(consoleOutput.contains("user_input.internal.aspects.RepositoryExceptionHandlingAspect"));

        // Assert that logging aspect is logging appropriately
        assertThat(consoleOutput, containsString(RepositoryLoggingAspect.AROUND));
        assertTrue(consoleOutput.contains("user_input.internal.aspects.RepositoryLoggingAspect"));
    }

    /**
     * Attempting to find all input subtypes of non-existent typeid should log empty
     * list
     * exception
     */
    @Test
    @CaptureSystemOutput
    public void testInputSubTypeFindAllByTypeIdExceptionLogs(OutputCapture capture) {
        // Attempt to find input types of non existent user (should return empty list)
        List<InputSubType> inputSubTypes = inputSubTypeRepository.findAllByTypeId(Long.MAX_VALUE);
        assertTrue(inputSubTypes.isEmpty());

        // Assert that aspect logs not found message
        String consoleOutput = capture.toString();
        assertThat(consoleOutput,
                containsString(RepositoryExceptionHandlingAspect.NOT_FOUND_MSG));

        // Assert that it is an ERROR
        assertTrue(consoleOutput.contains("WARN"));
        // Assert that aspect is referenced
        assertTrue(consoleOutput.contains("user_input.internal.aspects.RepositoryExceptionHandlingAspect"));

        // Assert that logging aspect is logging appropriately
        assertThat(consoleOutput, containsString(RepositoryLoggingAspect.BEFORE));
        assertTrue(consoleOutput.contains("user_input.internal.aspects.RepositoryLoggingAspect"));
    }

    /**
     * Attempting to find input subtype of non-existent id should log empty optional
     * exception
     */
    @Test
    @CaptureSystemOutput
    public void testInputSubTypeFindByIdExceptionLogs(OutputCapture capture) {
        // Attempt to find non existent inputtype, should return empty optional
        Optional<InputSubType> inputSubType = inputSubTypeRepository.findById(Long.MAX_VALUE);
        assertFalse(inputSubType.isPresent());

        // Assert that aspect logs not found message
        String consoleOutput = capture.toString();
        assertThat(consoleOutput,
                containsString(RepositoryExceptionHandlingAspect.NOT_FOUND_MSG));

        // Assert that it is an ERROR
        assertTrue(consoleOutput.contains("WARN"));
        // Assert that aspect is referenced
        assertTrue(consoleOutput.contains("user_input.internal.aspects.RepositoryExceptionHandlingAspect"));

        // Assert that logging aspect is logging appropriately
        assertThat(consoleOutput, containsString(RepositoryLoggingAspect.BEFORE));
        assertTrue(consoleOutput.contains("user_input.internal.aspects.RepositoryLoggingAspect"));
    }

    /**
     * Attempting to delete non-existent input subtype should log thrown exception
     * as
     * log.error()
     */
    @Test
    @CaptureSystemOutput
    public void testInputSubTypeDeleteByIdExceptionLogs(OutputCapture capture) {
        // Assert that deleting non-existent input subtype throws error
        assertThrows(InputTypeExceptions.InputSubTypeDeletionException.class, () -> {
            inputSubTypeRepository.deleteById(Long.MAX_VALUE);
        });
        // Asert that input subtype not found error is logged
        String consoleOutput = capture.toString();
        assertThat(consoleOutput,
                containsString(RepositoryExceptionHandlingAspect.FAILURE_MSG));

        // Assert that it is an ERROR
        assertTrue(consoleOutput.contains("ERROR"));
        // Assert that aspect is referenced
        assertTrue(consoleOutput.contains("user_input.internal.aspects.RepositoryExceptionHandlingAspect"));

        // Assert that logging aspect is logging appropriately
        assertThat(consoleOutput, containsString(RepositoryLoggingAspect.AFTER));
        assertTrue(consoleOutput.contains("user_input.internal.aspects.RepositoryLoggingAspect"));
    }

    /**
     * Attempting to find all inputs of non-existent user should log empty list
     * exception
     */
    @Test
    @CaptureSystemOutput
    public void testInputFindAllByUserIdExceptionLogs(OutputCapture capture) {
        // Attempt to find input types of non existent user (should return empty list)
        List<Input> inputs = inputRepository.findAllByUserId(Long.MAX_VALUE);
        assertTrue(inputs.isEmpty());

        // Assert that aspect logs not found message
        String consoleOutput = capture.toString();
        assertThat(consoleOutput,
                containsString(RepositoryExceptionHandlingAspect.NOT_FOUND_MSG));

        // Assert that it is an ERROR
        assertTrue(consoleOutput.contains("WARN"));
        // Assert that aspect is referenced
        assertTrue(consoleOutput.contains("user_input.internal.aspects.RepositoryExceptionHandlingAspect"));

        // Assert that logging aspect is logging appropriately
        assertThat(consoleOutput, containsString(RepositoryLoggingAspect.BEFORE));
        assertTrue(consoleOutput.contains("user_input.internal.aspects.RepositoryLoggingAspect"));
    }

    /**
     * Attempting to find input of non-existent id should log empty optional
     * exception
     */
    @Test
    @CaptureSystemOutput
    public void testInputFindByIdExceptionLogs(OutputCapture capture) {
        // Attempt to find non existent inputtype, should return empty optional
        Optional<Input> input = inputRepository.findById(Long.MAX_VALUE);
        assertFalse(input.isPresent());

        // Assert that aspect logs not found message
        String consoleOutput = capture.toString();
        assertThat(consoleOutput,
                containsString(RepositoryExceptionHandlingAspect.NOT_FOUND_MSG));

        // Assert that it is an ERROR
        assertTrue(consoleOutput.contains("WARN"));
        // Assert that aspect is referenced
        assertTrue(consoleOutput.contains("user_input.internal.aspects.RepositoryExceptionHandlingAspect"));

        // Assert that logging aspect is logging appropriately
        assertThat(consoleOutput, containsString(RepositoryLoggingAspect.BEFORE));
        assertTrue(consoleOutput.contains("user_input.internal.aspects.RepositoryLoggingAspect"));
    }

    /**
     * Attempting to delete non-existent input should log thrown exception as
     * log.error()
     */
    @Test
    @CaptureSystemOutput
    public void testInputDeleteByIdExceptionLogs(OutputCapture capture) {
        // Assert that deleting non-existent inputtype throws error
        assertThrows(InputExceptions.InputDeletionException.class, () -> {
            inputRepository.deleteById(Long.MAX_VALUE);
        });
        // Asert that inputtype not found error is logged
        String consoleOutput = capture.toString();
        assertThat(consoleOutput,
                containsString(RepositoryExceptionHandlingAspect.FAILURE_MSG));
        // Assert that it is an ERROR
        assertTrue(consoleOutput.contains("ERROR"));
        // Assert that aspect is referenced
        assertTrue(consoleOutput.contains("user_input.internal.aspects.RepositoryExceptionHandlingAspect"));

        // Assert that logging aspect is logging appropriately
        assertThat(consoleOutput, containsString(RepositoryLoggingAspect.AFTER));
        assertTrue(consoleOutput.contains("user_input.internal.aspects.RepositoryLoggingAspect"));
    }

}
