package io.spring.training.corespring.personalbudgettracker.user_input.web;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.spring.training.corespring.personalbudgettracker.user_input.internal.InputService;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.exceptions.InputExceptions;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.input.Input;

@RestController
@RequestMapping("/users/{userId}/inputs")
public class InputController {

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


    // Exception Handles
    @ExceptionHandler(InputExceptions.InputCreationException.class)
    public ResponseEntity<Object> handleInputCreationException(InputExceptions.InputCreationException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(InputExceptions.InputRetrievalException.class)
    public ResponseEntity<Object> handleInputRetrievalException(InputExceptions.InputRetrievalException ex) {
        return ResponseEntity.notFound().build();
    }


}
