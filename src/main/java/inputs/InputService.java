package inputs;

import java.util.List;

import inputs.internal.input.Input;

/**
 * Responsible for adding/removing inputs to and from a user's account and other input-related logic.
 */
public interface InputService {

    Input addInputToUser(Long userId, Input input);

    List<Input> getInputsByUserId(Long userId);

}
