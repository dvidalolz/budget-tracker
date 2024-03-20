package io.spring.training.corespring.personalbudgettracker.user_input.internal;



import io.spring.training.corespring.personalbudgettracker.user_input.internal.user.User;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.user.UserDetails;


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
    User addUser(UserDetails userDetails);

    User updateUser(Long userId, UserDetails userDetails);

    void deleteUser(Long userId);

    User findUserById(Long userId);

    User findUserByUserName(String userName);

}
