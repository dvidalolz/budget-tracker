package inputs.internal;

import java.util.List;

import inputs.InputService;
import inputs.internal.input.Input;
import inputs.internal.input.InputRepository;
import users.UserService;

/**
 * Implementation for : Responsible for adding inputs to a user's account and other input-related logic.
 * 
 * This object is an applicaton-layer service responsible for creating/inputting input to a user and fetching all inputs.
 */
public class InputServiceImpl implements InputService {

    private final InputRepository inputRepository;
    private final UserService userService;

    /**
     * Manage input-related logic like creation and fetch
     * @param inputRepository the data access repo for input
     * @param userService service for user account access
     */
    public InputServiceImpl(InputRepository inputRepository, UserService userService) {
        this.inputRepository = inputRepository;
        this.userService = userService;
    }

    @Override
    public Input addInputToUser(Long userId, Input input) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addInputToUser'");
    }

    @Override
    public List<Input> getInputsByUserId(Long userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getInputsByUserId'");
    }

}
