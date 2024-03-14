package io.spring.training.corespring.personalbudgettracker.users;



import io.spring.training.corespring.personalbudgettracker.users.internal.user.User;
import io.spring.training.corespring.personalbudgettracker.users.internal.user.UserDetails;


/**
 * Handles user-specific functionalities such as creating, deleting, updating
 * and retrieving user
 * 
 * (When refactoring : should consider ISP, for ex: userqueryservice,
 * usermanagementservice)
 */
public interface UserService {

    /**
     * @param userDetails a data transfer object created during registration process
     */
    User createUser(UserDetails userDetails);

    User updateUser(Long userId, UserDetails userDetails);

    void deleteUser(Long userId);

    User getUserById(Long userId);

    User getUserByUserName(String userName);

}
