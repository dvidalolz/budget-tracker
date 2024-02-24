package users;

import org.apache.catalina.User;

import common.userdetails.UserDetails;

import jakarta.transaction.Transactional;

/**
 * Handles user-specific functionalities such as creating, deleting, updating
 * and retrieving user
 * 
 * (When refactoring : should consider ISP, for ex: userqueryservice,
 * usermanagementservice)
 */
public interface UserService {

    User createUser(UserDetails userDetails);

    User updateUser(Long userId, UserDetails userDetails);

    void deleteUser(Long userId);

    User getUserById(Long userId);

}
