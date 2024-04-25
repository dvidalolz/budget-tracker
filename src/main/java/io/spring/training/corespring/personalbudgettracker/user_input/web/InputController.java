package io.spring.training.corespring.personalbudgettracker.user_input.web;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.spring.training.corespring.personalbudgettracker.user_input.internal.InputService;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.exceptions.InputExceptions.InputCreationException;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.exceptions.InputExceptions.InputNotFoundException;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.exceptions.InputExceptions.InputRetrievalException;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.input.Input;

@RestController
@RequestMapping("/users/{userId}/inputs")
public class InputController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final InputService inputService;

    public InputController(InputService inputService) {
        this.inputService = inputService;
    }

    /**
     * Add a new input for a specific user
     */
    @PostMapping
    public ResponseEntity<Input> addInput(@PathVariable Long userId, @RequestBody Input input) {
        Input addedInput = inputService.addInputToUser(userId, input);
        return ResponseEntity.ok(addedInput); // Consider creating a location URI as done in UserController
    }

    /**
     * Get all inputs for a specific user
     */
    @GetMapping
    public ResponseEntity<List<Input>> getAllInputsByUser(@PathVariable Long userId) {
        List<Input> inputs = inputService.findInputsByUserId(userId);
        return ResponseEntity.ok(inputs);
    }


    // Exception Handlers
    @ExceptionHandler(InputNotFoundException.class)
    public ResponseEntity<Void> handleInputNotFoundException(InputNotFoundException ex) {
        logger.error("Exception is: ", ex);
        return ResponseEntity.notFound().build(); // 404 not found
    }

    @ExceptionHandler(InputCreationException.class)
    public ResponseEntity<Object> handleInputNotCreatedException(InputCreationException ex) {
        logger.error("Exception is: ", ex);
        return ResponseEntity.badRequest().body(ex.getMessage()); // 400 bad request
    }

    @ExceptionHandler(InputRetrievalException.class)
    public ResponseEntity<Object> handleInputRetrievalException(InputRetrievalException ex) {
        logger.error("Exception is: ", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage()); // 500 internal service error
    }


}
