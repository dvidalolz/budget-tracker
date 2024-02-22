package users.internal.user;


/**
 * Handles User entity persistence operations.
 */
public interface UserRepository {
    
    public User findByUserName(String userName);

    // Save a user to the repo
    public User save(User user);

}
