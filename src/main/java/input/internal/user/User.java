package input.internal.user;

import java.util.HashSet;
import java.util.Set;

import input_types.InputType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import org.mindrot.jbcrypt.BCrypt;

@Entity
public class User {
    
    @Id
    private Long id;

    private String username;

    private String passwordHash;

    private Set<InputType> inputTypes = new HashSet<>();

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
Ni

}
