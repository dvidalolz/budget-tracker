package io.spring.training.corespring.personalbudgettracker.inputs.internal;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;

import io.spring.training.corespring.personalbudgettracker.inputs.InputService;
import io.spring.training.corespring.personalbudgettracker.inputs.internal.input.Input;
import io.spring.training.corespring.personalbudgettracker.inputs.internal.input.InputRepository;
import io.spring.training.corespring.personalbudgettracker.users.internal.user.User;


/**
 * Implementation for : Responsible for adding inputs to a user's account and other input-related logic.
 * 
 * This object is an applicaton-layer service responsible for creating/inputting input to a user and fetching all inputs.
 */
public class InputServiceImpl implements InputService {

    private final InputRepository inputRepository;

    /**
     * Manage input-related logic like creation and fetch
     * @param inputRepository the data access repo for input
     * @param userService service for user account access
     */
    public InputServiceImpl(InputRepository inputRepository) {
        this.inputRepository = inputRepository;
    }

    @Transactional
    @Override
    public Input addInputToUser(Long userId, Input input) {
        try {
            User user = new User();
            user.setId(userId);
            input.setUser(user); 
            return inputRepository.save(input);
        } catch (RuntimeException e) {
            throw new RuntimeException("Error adding input for user with ID: " + userId, e);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<Input> getInputsByUserId(Long userId) {
        try {
            return inputRepository.findAllByUserId(userId);
        } catch (RuntimeException e) {
            throw new RuntimeException("Error fetching inputs for user with ID: " + userId, e);
        }

    }

}
