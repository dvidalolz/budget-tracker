package input.internal.inputs;

import java.util.Objects;

import common.date.SimpleDate;
import common.money.MonetaryAmount;
import input_types.InputSubType;
import input_types.InputType;

/**
 * Represents a financial transaction event within an account, characterized by
 * an amount, date, and type.
 */
public class Input {

    private final Long id;

    private final MonetaryAmount amount;

    private final SimpleDate date;

    private final InputType type;

    private final InputSubType subType;

    /**
     * Constructs a Transaction instance.
     * 
     * @param id      The unique id of input
     * @param amount  The monetary amount for the transaction.
     * @param date    The date the transaction was made.
     * @param type    The type of transaction.
     * @param subType The subType of transaction (can be null)
     */
    public Input(Long id, MonetaryAmount amount, SimpleDate date, InputType type, InputSubType subType) {
        if (type == null) {
            throw new IllegalArgumentException("Type cannot be null");
        }
        this.id = id;
        this.amount = amount;
        this.date = date;
        this.type = type;
        this.subType = subType;
    }

    // Constructor for if no subtype provided
    public Input(Long id, MonetaryAmount amount, SimpleDate date, InputType type) {
        this(id, amount, date, type, null);
    }

    // Getters for accessing the private fields.
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
                Objects.equals(subType, other.subType);
    }

    /**
     * Generates a hash code for this Transaction.
     * 
     * @return An integer hash code value.
     */
    @Override
    public int hashCode() {
        return Objects.hash(amount, date, type, subType); // Includes subType in hash calculation
    }

    /**
     * Returns a string representation of this Transaction.
     * 
     * @return A string that textually represents this Transaction.
     */
    @Override
    public String toString() {
        String subTypeString = (subType != null) ? " - " + subType : "";
        return "Transaction of " + amount + " on " + date + " as " + type + subTypeString;
    }
    
    
}
