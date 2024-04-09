package io.spring.training.corespring.personalbudgettracker.user_input.internal.input_type;


import java.util.Objects;

import io.spring.training.corespring.personalbudgettracker.user_input.internal.user.User;




/**
 * Categorizes inputs into high-level classifications such as "Income" or "Expense".
 */

public class InputType {


    private Long id;

    private String name;
    private User user;

    // Constructors
    public InputType() {
    }

    public InputType(String name) {
        this.name = name;
    }

    public InputType(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public InputType(String name, User user) {
        this.name = name;
        this.user = user;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public static InputType inputTypeWithOnlyId(Long id) {
        InputType inputType = new InputType();
        inputType.setId(id);
        return inputType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof InputType))
            return false;
        InputType that = (InputType) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "InputType{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
