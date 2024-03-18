package io.spring.training.corespring.personalbudgettracker.aspects;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import io.spring.training.corespring.personalbudgettracker.CaptureSystemOutput;
import io.spring.training.corespring.personalbudgettracker.CaptureSystemOutput.OutputCapture;
import io.spring.training.corespring.personalbudgettracker.TestInfrastructureConfig;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.aspects.RepositoryExceptionHandlingAspect;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.exceptions.InputExceptions;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.exceptions.UserExceptions;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.input.Input;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.input.InputRepository;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.input_type.InputTypeRepository;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.user.User;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.user.UserRepository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { TestInfrastructureConfig.class })
@EnableAutoConfiguration
public class RepositoryExceptionHandlingAspectTests {

    @Autowired
    UserRepository userRepository;

    @Autowired
    InputRepository inputRepository;

    @Autowired
    InputTypeRepository inputTypeRepository;

    @Test
    @CaptureSystemOutput
    public void testReportException(OutputCapture capture) {

        assertThrows(RuntimeException.class, () -> {
            userRepository.deleteById((long) 12334);
        });

        assertThrows(RuntimeException.class, () -> {
            inputRepository.deleteById((long) 12334);
        });

        assertThrows(RuntimeException.class, () -> {
            inputTypeRepository.deleteById((long) 12334);
        });

        assertThat(capture.toString(), containsString(RepositoryExceptionHandlingAspect.FAILURE_MSG));

    }

    @Test
    @CaptureSystemOutput
    public void testEmptyException(OutputCapture capture) {

        Optional<User> foundUser = userRepository.findById(Long.MAX_VALUE);

        // Assert that the result list is empty
        assertFalse(foundUser.isPresent(), "User should not be found with non-existing ID");

        assertThat(capture.toString(), containsString(RepositoryExceptionHandlingAspect.NOT_FOUND_MSG));

    }

}
