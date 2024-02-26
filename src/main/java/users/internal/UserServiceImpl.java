package users.internal;


import org.apache.catalina.User;

import common.userdetails.UserDetails;
import input_types.internal.input_type.InputTypeRepository;
import users.UserService;
import users.internal.user.UserRepository;



// TODO : Testing for userservice impl
/**
 * Implementation for : Handles user-specific functionalities such as creating, deleting, updating and retrieving user
 * 
 * This object is an applicaton-layer service responsible for fetching and managing user accounts
 */
public class UserServiceImpl implements UserService {

    private final InputTypeRepository inputTypeRepository;

    private final UserRepository userRepository;

    /**
     * Manage user availability and account details
     * @param userRepository the repository for work with user accounts
     * @param inputTypeRepository necesary for default inputtype initialization during user creation
     */
    public UserServiceImpl(UserRepository userRepository, InputTypeRepository inputTypeRepository) {
        this.userRepository = userRepository;
        this.inputTypeRepository = inputTypeRepository;
    }
    
    @Override
    public User createUser(UserDetails userDetails) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createUser'");
    }

    @Override
    public User updateUser(Long userId, UserDetails userDetails) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateUser'");
    }

    @Override
    public void deleteUser(Long userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteUser'");
    }

    @Override
    public User getUserById(Long userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getUserById'");
    }

    private void createDefaultTypesForUser(Long userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createDefaultTypesForUser'");
    }

    
}
