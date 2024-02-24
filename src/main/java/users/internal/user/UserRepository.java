package users.internal.user;

import java.util.Optional;

/**
 * Handles User entity persistence operations.
 */
public interface UserRepository {
    
    // Save a user to the database. This method can handle both creating a new user
    // and updating an existing user
    User save(User user);

    // Find a user by their unique ID.
    Optional<User> findById(Long id);

    // Find a user by their username. This method assumes usernames are unique.
    Optional<User> findByUsername(String username);

    // Delete a user by their unique ID.
    void deleteById(Long id);

}
