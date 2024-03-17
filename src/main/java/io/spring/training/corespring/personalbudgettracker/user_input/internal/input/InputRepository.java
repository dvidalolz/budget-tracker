package io.spring.training.corespring.personalbudgettracker.user_input.internal.input;

import java.util.List;
import java.util.Optional;

/**
 * Manages Input entity persistence operations.
 */
public interface InputRepository {

    // This method can handle both creating a new input and updating an existing input
    Input save(Input input);

    // Fetch all inputs associated with user account by id
    List<Input> findAllByUserId(Long userId);

    public Optional<Input> findById(Long id);

    void deleteById(Long inputId);   
    
}
