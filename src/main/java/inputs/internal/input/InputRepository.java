package inputs.internal.input;

import java.util.List;

/**
 * Manages Input entity persistence operations.
 */
public interface InputRepository {

    // Save input to the database. This method can handle both creating a new input
    // and updating an existing input
    Input save(Input input);

    // Fetch all inputs associated with user account by id
    List<Input> findByUserId(Long userId);

    // Delete an inpt using unique input id
    void deleteById(Long inputId);   
    
}
