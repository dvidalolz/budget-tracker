package inputs.internal;

import inputs.InputService;
import inputs.internal.input.Input;
import inputs.internal.input.InputRepository;
import users.internal.user.UserRepository;

public class InputServiceImpl implements InputService {

    private UserRepository accountRepository;

    private InputRepository inputRepository;

    public InputServiceImpl(UserRepository accountRepository, InputRepository inputRepository) {
        this.accountRepository = accountRepository;
        this.inputRepository = inputRepository;
    }

    @Override
    public void inputToAccount(Input input) {
        // Fetch account using account ID (get account ID using spring auth info) - session requires sign in so it should have all a session id or username or password
        //    User account = accountRepository.findByUserName(/* Parameter user name provided by spring auth */);

        // The method storeInput() stores input in account's inputRepository
        //    account.storeInput(input);
    }
    
}
