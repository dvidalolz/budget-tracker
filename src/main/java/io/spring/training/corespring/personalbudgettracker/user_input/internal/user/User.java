package io.spring.training.corespring.personalbudgettracker.user_input.internal.user;

import java.util.Objects;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Represents an application user, containing user information and associated
 * InputTypes.
 */
public class User {

    private Long id;

    private String username;

    private String passwordHash;

    private String email;

    public User() {

    }

    public User(Long id) {
        this.id = id;
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

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
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
    
    // UserDetail specific factory method
    public static User fromUserDetails(UserDetails userDetails) {
        User user = new User();
        user.setUsername(userDetails.getUsername());
        user.setPassword(userDetails.getPassword()); // Consider hashing the password
        user.setEmail(userDetails.getEmail());
        // Set other fields as necessary...
        return user;
    }

    public static User userWithOnlyId(Long userId) {
        User user = new User();
        user.setId(userId);
        return user;
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
