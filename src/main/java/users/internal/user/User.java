package users.internal.user;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import org.mindrot.jbcrypt.BCrypt;

import input_types.internal.input_type.InputType;


/** 
 * Represents an application user, containing user information and associated InputTypes.
 */
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String passwordHash;

    private String email;

    @OneToMany
    private Set<InputType> inputTypes = new HashSet<>();

    public User() {
        // Initialize with default input types
        initializeDefaultInputTypes();
    }

    // All users must have at least Expense and Income as input types
    private void initializeDefaultInputTypes() {
        inputTypes.add(new InputType("Expense", this));
        inputTypes.add(new InputType("Income", this));
    }

    // Setters and getters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.passwordHash = BCrypt.hashpw(password, BCrypt.gensalt());
    }

    // needed for (updating) password in repo
    public String getPasswordHash() {
        return passwordHash;
    }

    public boolean checkPassword(String password) {
        return BCrypt.checkpw(password, this.passwordHash);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<InputType> getInputTypes() {
        return inputTypes;
    }

    public void setInputTypes(Set<InputType> inputTypes) {
        this.inputTypes = inputTypes;
    }

    // Overrides
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof User))
            return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                '}';
    }
}
