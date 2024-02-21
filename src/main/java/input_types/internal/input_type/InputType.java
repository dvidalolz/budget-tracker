package input_types.internal.input_type;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import input_types.internal.input_subtype.InputSubType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import users.internal.user.User;

public class InputType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "type")
    private Set<InputSubType> subtypes = new HashSet<>();

    private User account;

    // Constructors
    public InputType() {
    }

    public InputType(String name, User account) {
        this.name = name;
        this.account = account;
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

    public Set<InputSubType> getSubtypes() {
        return subtypes;
    }

    public void setSubtypes(Set<InputSubType> subtypes) {
        this.subtypes = subtypes;
    }

    public User getAccount() {
        return account;
    }

    public void setAccount(User account) {
        this.account = account;
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
