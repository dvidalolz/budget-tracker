package inputs.internal.input;

import java.util.List;

/**
 * Manages Input entity persistence operations.
 */
public interface InputRepository {

    // Fetch all inputs associated with user account by id
    public List<Input> findByUserId(Long userId);
}
