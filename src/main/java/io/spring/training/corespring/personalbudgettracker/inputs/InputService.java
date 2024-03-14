package io.spring.training.corespring.personalbudgettracker.inputs;

import java.util.List;

import io.spring.training.corespring.personalbudgettracker.inputs.internal.input.Input;

/**
 * Responsible for adding inputs to user's account and other input-related logic.
 */
public interface InputService {

    /**
     * Creates an input and associates with existing user 
     * @param userId unique id used to create lightweight userobject as input attribute
     * @param input object which does not have an associated user (user object), nor a generated id
     * Returned Input contains lightweight user (id only) and input id which was generated automatically by database
     */
    Input addInputToUser(Long userId, Input input);

    /**
     * Returns a list of all the inputs ever created and associated with given user
     * @param userId unique id used to obtain user 
     * Returned list of inputs come fully fleshed with its given attributes
     */
    List<Input> getInputsByUserId(Long userId);

}
