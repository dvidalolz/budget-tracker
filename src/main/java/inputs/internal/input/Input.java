package inputs.internal.input;

import java.util.Objects;

import org.apache.catalina.User;

import common.date.SimpleDate;
import common.money.MonetaryAmount;
import input_types.internal.input_subtype.InputSubType;
import input_types.internal.input_type.InputType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

/**
 * Represents a financial transaction with attributes like amount, date, 
 * and associations with InputType and InputSubType.
 */
public class Input {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private MonetaryAmount amount;

    private SimpleDate date;

    @ManyToOne
    private InputType type;

    @ManyToOne
    private InputSubType subType;

    @ManyToOne
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
     * @param user    The user associated with this particular input (useful for input retrieval)
     */
    public Input(Long id, MonetaryAmount amount, SimpleDate date, User user, InputType type, InputSubType subType) {
        if (type == null) {
            throw new IllegalArgumentException("Type cannot be null");
        }
        this.id = id;
        this.amount = amount;
        this.date = date;
        this.type = type;
        this.subType = subType;
        this.user = user;
    }

    // Constructor for if no subtype provided
    public Input(Long id, MonetaryAmount amount, SimpleDate date, User user, InputType type) {
        this(id, amount, date, user, type, null);
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
