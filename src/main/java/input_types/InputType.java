package input_types;

import java.util.HashSet;
import java.util.Set;

import input.internal.user.User;

public class InputType {

    private Long id;

    private String name;

    private Set<InputSubType> subtypes = new HashSet<>();

    private User account;

}
