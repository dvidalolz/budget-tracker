package io.spring.training.corespring.personalbudgettracker.user_input.internal.user;

import java.sql.PreparedStatement;

import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import io.spring.training.corespring.personalbudgettracker.user_input.internal.exceptions.UserExceptions.UserNotFoundException;

@Repository
@Profile("jdbc")
public class JdbcUserRepository implements UserRepository {

    private JdbcTemplate jdbcTemplate;

    public JdbcUserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User save(User user) {

            if (user.getId() == null) {
                // Insert new user
                String insertSql = "INSERT INTO T_User (user_name, password_hash, email) VALUES (?, ?, ?)";
                KeyHolder keyHolder = new GeneratedKeyHolder();
                jdbcTemplate.update(connection -> {
                    PreparedStatement ps = connection.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
                    ps.setString(1, user.getUsername());
                    ps.setString(2, user.getPasswordHash());
                    ps.setString(3, user.getEmail());
                    return ps;
                }, keyHolder);

                Long generatedId = keyHolder.getKey().longValue();
                user.setId(generatedId);
            } else {
                // Update existing user
                String updateSql = "UPDATE T_User SET user_name = ?, password_hash = ?, email = ? WHERE id = ?";
                jdbcTemplate.update(updateSql, user.getUsername(), user.getPasswordHash(), user.getEmail(),
                        user.getId());
            }

            return user;

    }

    /**
     * Return empty optional if none found
     */
    @Override
    public Optional<User> findById(Long id) {
        String sql = "SELECT id, user_name, email, password_hash FROM T_User WHERE id = ?";
        List<User> users = jdbcTemplate.query(sql, userRowMapper, id);
    
        return users.stream().findFirst();
    }
    
    /**
     * Return empty optional if none found
     */
    @Override
    public Optional<User> findByUsername(String username) {
        String sql = "SELECT id, user_name, email, password_hash FROM T_User WHERE user_name = ?";
        List<User> users = jdbcTemplate.query(sql, userRowMapper, username);
    
        return users.stream().findFirst();
    }
    

    @Override
    public void deleteById(Long id) {
        String deleteSql = "DELETE FROM T_User WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(deleteSql, id);
        
        if (rowsAffected == 0) {
            throw new UserNotFoundException("User not found with ID: " + id);
        }
    }
    

    private RowMapper<User> userRowMapper = (rs, rowNum) -> {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setUsername(rs.getString("user_name"));
        user.setEmail(rs.getString("email"));
        user.setPasswordHash(rs.getString("password_hash"));
        return user;
    };

}
