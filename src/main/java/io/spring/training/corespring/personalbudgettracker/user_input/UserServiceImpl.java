package io.spring.training.corespring.personalbudgettracker.user_input;

import org.springframework.stereotype.Service;

import io.spring.training.corespring.personalbudgettracker.user_input.internal.UserService;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.exceptions.UserExceptions;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.input_type.InputType;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.input_type.InputTypeRepository;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.user.User;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.user.UserDetails;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.user.UserRepository;

import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation for : Handles user-specific functionalities such as creating,
 * deleting, updating and retrieving user
 * 
 * This object is an applicaton-layer service responsible for fetching and
 * managing user accounts
 */
@Service("userService")
public class UserServiceImpl implements UserService {

    private final InputTypeRepository inputTypeRepository;

    private final UserRepository userRepository;

    /**
     * Manage user availability and account details
     * 
     * @param userRepository      the repository for work with user accounts
     * @param inputTypeRepository necesary for default inputtype initialization
     *                            during user creation
     */
    public UserServiceImpl(UserRepository userRepository, InputTypeRepository inputTypeRepository) {
        this.userRepository = userRepository;
        this.inputTypeRepository = inputTypeRepository;
    }

    /**
     * Important point : Default types "expense" and "income" are created for each new user created
     */
    @Transactional
    @Override
    public User addUser(UserDetails userDetails) {
        try {
            User newUser = User.fromUserDetails(userDetails);
            // Remember : user id is generated by database according to spring annotation
            newUser = userRepository.save(newUser);
            createDefaultTypesForUser(newUser);
            return newUser;
        } catch (RuntimeException e) {
            throw new UserExceptions.UserCreationException("Failed to create user " + userDetails.getUsername() + " due to an unexpected error", e);
        }
    }

    @Transactional
    @Override
    public User updateUser(Long userId, UserDetails userDetails) {
        return userRepository.findById(userId).map(existingUser -> {
            // Update the existing user object with the new details
            if (userDetails.getUsername() != null) {
                existingUser.setUsername(userDetails.getUsername());
            }
            if (userDetails.getEmail() != null) {
                existingUser.setEmail(userDetails.getEmail());
            }
            if (userDetails.getPassword() != null) {
                existingUser.setPassword(userDetails.getPassword());
            }

            return userRepository.save(existingUser);
        })
                .orElseThrow(() -> new UserExceptions.UserNotFoundException("User with ID " + userId + " not found"));
    }

    @Transactional
    @Override
    public void deleteUser(Long userId) {
        try {
            userRepository.deleteById(userId);
        } catch (RuntimeException ex) {
            throw new UserExceptions.UserDeletionException("Failed to delete user with ID: " + userId, ex);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserExceptions.UserNotFoundException("User with ID " + userId + " not found."));
    }

    @Transactional(readOnly = true)
    @Override
    public User findUserByUserName(String userName) {
        return userRepository.findByUsername(userName)
                .orElseThrow(() -> new UserExceptions.UserNotFoundException("User with user name " + userName + " not found"));
    }

    /**
     * Creates default input type of "expense" and "income" for a user upon creation
     * of user BECAUSE any subtypes created in subtype repo must have a type_id foreign 
     * key. This would be impossible without having an input type with given user_id
     */
    private void createDefaultTypesForUser(User newUser) {
        try {
            InputType expense = new InputType("Expense", newUser);
            InputType income = new InputType("Income", newUser);
            
            inputTypeRepository.save(expense);
            inputTypeRepository.save(income);
        } catch (RuntimeException e) {
            throw new UserExceptions.UserNotFoundException("Failed to create default types for user with ID: " + newUser.getId(), e);
        }
    }
    
}
