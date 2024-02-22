package input_types.internal.input_subtype;

import java.util.Objects;

import input_types.internal.input_type.InputType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;


/**
 * Provides a more granular classification under InputType, like "Salary" or "Groceries".
 */
@Entity
public class InputSubType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "type_id", nullable = false)
    private InputType type;

    // Constructors
    public InputSubType() {
    }

    public InputSubType(String name, InputType type) {
        this.name = name;
        this.type = type;
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
