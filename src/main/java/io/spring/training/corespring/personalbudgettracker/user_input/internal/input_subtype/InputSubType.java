package io.spring.training.corespring.personalbudgettracker.user_input.internal.input_subtype;

import java.util.Objects;

import io.spring.training.corespring.personalbudgettracker.user_input.internal.input_type.InputType;



/**
 * Provides a more granular classification under InputType, like "Salary" or "Groceries".
 */

public class InputSubType {


    private Long id;

    private String name;


    private InputType type;

    // Constructors
    public InputSubType() {
    }

    public InputSubType(String name, InputType type) {
        this.name = name;
        this.type = type;
    }

    public InputSubType(Long id, String name) {
        this.id = id;
        this.name = name;
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

    public InputType getType() {
        return type;
    }

    public void setType(InputType type) {
        this.type = type;
    }

    // Overrides
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof InputSubType))
            return false;
        InputSubType that = (InputSubType) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "InputSubType{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type=" + (type != null ? type.getName() : "null") +
                '}';
    }
}
