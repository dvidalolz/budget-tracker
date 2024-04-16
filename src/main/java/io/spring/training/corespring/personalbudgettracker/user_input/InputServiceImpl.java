package io.spring.training.corespring.personalbudgettracker.user_input;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import io.spring.training.corespring.personalbudgettracker.user_input.internal.InputService;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.exceptions.InputExceptions.InputRetrievalException;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.exceptions.InputExceptions.InputCreationException;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.input.Input;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.input.InputRepository;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.user.User;


/**
 * Implementation for : Responsible for adding inputs to a user's account and other input-related logic.
 * 
 * This object is an applicaton-layer service responsible for creating/inputting input to a user and fetching all inputs.
 */
@Service
public class InputServiceImpl implements InputService {

    private final InputRepository inputRepository;

    /**
     * Manage input-related logic like creation and fetch
     * @param inputRepository the data access repo for input
     */
    public InputServiceImpl(InputRepository inputRepository) {
        this.inputRepository = inputRepository;
    }

    /**
     * Adds input to associated user (via provided userId)
     * If userId does not exist, repo thrown exception caught, wrapped, and rethrown
     */
    @Override
    public Input addInputToUser(Long userId, Input input) {
        try {
            User user = new User();
            user.setId(userId);
            input.setUser(user); 
            return inputRepository.save(input);
        } catch (DataAccessException e) {
            throw new InputCreationException("Error adding input for user with ID: " + userId, e);
        }
    }

    /**
     * Repo function returns empty list if none found, this is handled with first exception throw
     * All other exceptions handled via second exception thrown
     */
    @Override
    public List<Input> findInputsByUserId(Long userId) {
        try {
            List<Input> inputs = inputRepository.findAllByUserId(userId);
            if (inputs.isEmpty()) {
                throw new InputRetrievalException("No inputs found for user with ID: " + userId);
            }
            return inputs;
        } catch (DataAccessException e) {
            throw new InputRetrievalException("Error fetching inputs for user with ID: " + userId, e);
        }
    }
    

}
