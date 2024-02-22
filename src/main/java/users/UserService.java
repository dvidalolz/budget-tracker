package users;

import java.util.List;
import java.util.Set;

import common.userdetails.UserDetails;
import input_types.internal.input_type.InputType;
import inputs.internal.input.Input;
import jakarta.transaction.Transactional;

/**
 * Handles user-specific functionalities such as creating, deleting, updating and retrieving user
 * 
 * (When refactoring : should consider ISP, for ex: userqueryservice, usermanagementservice)
 */
@Transactional // ensures that the changes are part of a transaction and that JPA can track and persist the changes automatically.
public interface UserService {



}
