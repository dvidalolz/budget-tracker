package io.spring.training.corespring.personalbudgettracker.user_input.internal.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import io.spring.training.corespring.personalbudgettracker.user_input.internal.exceptions.UserExceptions;


@Repository
@Profile("jdbc")
public class JdbcUserRepository implements UserRepository {

    private DataSource dataSource;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    @Override
    public User save(User user) {

        String insertSql = "INSERT INTO T_User (user_name, password_hash, email) VALUES (?, ?, ?)";

        String updateSql = "UPDATE T_User SET user_name = ?, password_hash = ?, email = ? WHERE id = ?";

        // If user.id not exist, do insert, else, do update
        try (Connection conn = dataSource.getConnection();
                PreparedStatement ps = (user.getId() == null)
                        ? conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)
                        : conn.prepareStatement(updateSql)) {

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPasswordHash());
            ps.setString(3, user.getEmail());
            

            // If user.id does exist, do update - include user id
            if (user.getId() != null) {
                ps.setLong(4, user.getId());
            }

            // execution confirmation
            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new UserExceptions.UserSaveException("Creating/updating user failed, no rows affected.");
            }

            // if insert, check auto-generated key (id) and set return user object
            if (user.getId() == null) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        user.setId(generatedKeys.getLong(1));
                    } else {
                        throw new UserExceptions.UserSaveException("Creating user failed, no ID obtained.");
                    }
                }
            }

            return user;
        } catch (SQLException e) {
            throw new UserExceptions.UserSaveException("Error saving user: " + user.getUsername(), e);
        }
    }

    @Override
    public Optional<User> findById(Long id) {
        // SQL query to fetch a user by ID
        String sql = "SELECT id, user_name, email, password_hash FROM T_User WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            // Set param for prepped statement
            ps.setLong(1, id);

            // Execute query
            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    User user = new User();
                    user.setId(rs.getLong("id"));
                    user.setUsername(rs.getString("user_name"));
                    user.setEmail(rs.getString("email"));
                    user.setPasswordHash(rs.getString("password_hash"));

                    return Optional.of(user);
                }
            }

        } catch (SQLException e) {
            throw new UserExceptions.UserNotFoundException("Error fetching user by ID: " + id, e);
        }

        // Return empty if user not found
        return Optional.empty();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        // SQL query to fetch a user by username
        String sql = "SELECT id, user_name, email, password_hash FROM T_User WHERE user_name = ?";

        try (Connection conn = dataSource.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            // Set param for prepped statement
            ps.setString(1, username);

            // Execute query
            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    User user = new User();
                    user.setId(rs.getLong("id"));
                    user.setUsername(rs.getString("user_name"));
                    user.setEmail(rs.getString("email"));
                    user.setPasswordHash(rs.getString("password_hash"));

                    return Optional.of(user);
                }
            }

        } catch (SQLException e) {
            throw new UserExceptions.UserNotFoundException("Error fetching user by name: " + username, e);
        }

        // Return empty if user not found
        return Optional.empty();
    }

    @Override
    public void deleteById(Long id) {
        // SQL query to delete a user by ID
        String sql = "DELETE FROM T_User WHERE id = ?";
    
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
    
            // Set parameter for the prepared statement
            ps.setLong(1, id);
    
            // Execute the update
            int affectedRows = ps.executeUpdate();
    
            if (affectedRows == 0) {
                throw new UserExceptions.UserDeletionException("Deleting user failed, no rows affected.");
            }
    
        } catch (SQLException e) {
            throw new UserExceptions.UserDeletionException("Error deleting user with ID: " + id, e);
        }
    }

    
}
