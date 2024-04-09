package io.spring.training.corespring.personalbudgettracker.user_input.internal.input;

import java.util.Objects;

import io.spring.training.corespring.personalbudgettracker.common.date.SimpleDate;
import io.spring.training.corespring.personalbudgettracker.common.money.MonetaryAmount;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.input_subtype.InputSubType;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.input_type.InputType;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.user.User;

/**
 * Represents a financial transaction with attributes like amount, date,
 * and associations with InputType and InputSubType.
 */
public class Input {

    private Long id;

    private MonetaryAmount amount;

    private SimpleDate date;

    private InputType type;

    private InputSubType subType;

    private User user;

    public Input() {

    }

    /**
     * Constructs a Transaction instance.
     * 
     * @param id      The unique id of input
     * @param amount  The monetary amount for the input
     * @param date    The date the input was made.
     * @param type    The type of an input. (Expense, income, etc..)
     * @param subType The subType of transaction (Groceries, Job, etc..can be null)
     * @param user    The user associated with this particular input (useful for
     *                input retrieval)
     */
    public Input(MonetaryAmount amount, SimpleDate date, User user, InputType type, InputSubType subType) {
        if (type == null) {
            throw new IllegalArgumentException("Type cannot be null");
        }
        this.amount = amount;
        this.date = date;
        this.type = type;
        this.subType = subType;
        this.user = user;
    }

    // Constructor for if no subtype provided
    public Input(MonetaryAmount amount, SimpleDate date, User user, InputType type) {
        this(amount, date, user, type, null);
    }

    // Getters
    public Long getId() {
        return id;
    }

    public MonetaryAmount getAmount() {
        return amount;
    }

    public SimpleDate getDate() {
        return date;
    }

    public InputType getType() {
        return type;
    }

    public InputSubType getSubtype() {
        return subType;
    }

    public User getUser() {
        return user;
    }

    // setters
    public void setId(Long id) {
        this.id = id;
    }


    public void setAmount(MonetaryAmount amount) {
        this.amount = amount;
    }


    public void setDate(SimpleDate date) {
        this.date = date;
    }

    public void setType(InputType type) {
        if (type == null) {
            throw new IllegalArgumentException("Type cannot be null");
        }
        this.type = type;
    }

    public void setSubType(InputSubType subType) {
        this.subType = subType; // It's okay for subType to be null
    }

    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Compares this Transaction with another object for equality.
     * 
     * @param o The object to compare this Transaction against.
     * @return true if the given object represents a Transaction equivalent to this
     *         transaction, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Input))
            return false;
        Input other = (Input) o;
        return Objects.equals(amount, other.amount) &&
                Objects.equals(date, other.date) &&
                Objects.equals(type, other.type) &&
                Objects.equals(subType, other.subType) &&
                Objects.equals(user, other.user);
    }

    /**
     * Generates a hash code for this Input.
     * 
     * @return An integer hash code value.
     */
    @Override
    public int hashCode() {
        return Objects.hash(amount, date, user, type, subType);
    }

    /**
     * Returns a string representation of this Input.
     * 
     * @return A string that textually represents this Transaction.
     */
    @Override
    public String toString() {
        String subTypeString = (subType != null) ? " - " + subType : "";
        return user + "'s " + "Input of " + amount + " on " + date + " as " + type + subTypeString;
    }

}
