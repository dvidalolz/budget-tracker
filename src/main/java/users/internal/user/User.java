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

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String passwordHash;

    @OneToMany(mappedBy = "user")
    private Set<InputType> inputTypes = new HashSet<>();

    public User() {
        // Initialize with default input types
        initializeDefaultInputTypes();
    }

    private void initializeDefaultInputTypes() {
        inputTypes.add(new InputType("Expense", this));
        inputTypes.add(new InputType("Income", this));
    }

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

    public boolean checkPassword(String password) {
        return BCrypt.checkpw(password, this.passwordHash);
    }

    public Set<InputType> getInputTypes() {
        return inputTypes;
    }

    public void setInputTypes(Set<InputType> inputTypes) {
        this.inputTypes = inputTypes;
    }

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