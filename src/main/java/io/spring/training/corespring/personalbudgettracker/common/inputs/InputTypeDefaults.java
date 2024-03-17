package io.spring.training.corespring.personalbudgettracker.common.inputs;

import java.util.Arrays;
import java.util.List;

import io.spring.training.corespring.personalbudgettracker.user_input.internal.input_type.InputType;


/**
 * Holder class for retrieving the input type defaults that every user must have 
 * 
 * This supports the domain-driven approach of ensuring all future services can retrieve the default input types
 * which are initialized within the user object at birth, but not found in the input type repository.
 */
public class InputTypeDefaults {
    public static List<InputType> getDefaultInputTypes() {
        return Arrays.asList(new InputType("Expense"), new InputType("Income"));
    }
}