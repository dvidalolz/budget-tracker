package io.spring.training.corespring.personalbudgettracker.user_input.internal.input_type;

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

import io.spring.training.corespring.personalbudgettracker.user_input.internal.exceptions.InputTypeExceptions.InputTypeNotFoundException;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.user.User;

@Repository
@Profile("jdbc")
public class JdbcInputTypeRepository implements InputTypeRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcInputTypeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<InputType> inputTypeRowMapper = (rs, rowNum) -> {
        InputType inputType = new InputType();
        inputType.setId(rs.getLong("input_type_id"));
        inputType.setName(rs.getString("type_name"));
        // Set only the user ID
        User user = new User();
        user.setId(rs.getLong("user_id"));
        inputType.setUser(user);
        return inputType;
    };

    /**
     * Returns a fully fleshed, saved inputtype object
     */
    @Override
    public InputType save(InputType inputType) {
        if (inputType.getId() == null) {
            // Insert new InputType
            String insertSql = "INSERT INTO T_InputType (type_name, user_id) VALUES (?, ?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();
            int rowsAffected = jdbcTemplate.update(
                    connection -> {
                        PreparedStatement ps = connection.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
                        ps.setString(1, inputType.getName());
                        ps.setLong(2, inputType.getUser().getId());
                        return ps;
                    },
                    keyHolder);
            if (rowsAffected > 0) {
                Number key = keyHolder.getKey();
                if (key != null) {
                    inputType.setId(key.longValue());
                }
            }
        } else {
            // Update existing InputType
            String updateSql = "UPDATE T_InputType SET type_name = ?, user_id = ? WHERE id = ?";
            jdbcTemplate.update(updateSql, inputType.getName(), inputType.getUser().getId(), inputType.getId());
        }
        return inputType;
    }

    /**
     * Important Note: this method returns a list of types whose user has only id
     * Exception handling is returning an empty list
     */
    @Override
    public List<InputType> findAllByUserId(Long userId) {
        String sql = "SELECT it.id AS input_type_id, it.type_name, u.id AS user_id " +
                     "FROM T_InputType it " +
                     "JOIN T_User u ON it.user_id = u.id " +
                     "WHERE it.user_id = ?";
        return jdbcTemplate.query(sql, inputTypeRowMapper, userId);
    }

    /**
     * Important Note : Important Note: this method returns lightweight type
     * Exception handling is returning an empty optional
     */
    @Override
    public Optional<InputType> findById(Long typeId) {
        String sql = "SELECT it.id AS input_type_id, it.type_name, u.id AS user_id " +
                     "FROM T_InputType it " +
                     "JOIN T_User u ON it.user_id = u.id " +
                     "WHERE it.id = ?";
        return jdbcTemplate.query(sql, inputTypeRowMapper, typeId)
                           .stream()
                           .findFirst();
    }

    /**
     * Jdbctemplate.update() returns affected rows if update occurred. 
     * If no rows were affected, input type was not found (exception thrown)
     */
    @Override
    public void deleteById(Long typeId) {
        String sql = "DELETE FROM T_InputType WHERE id = ?";
        int affectedRows = jdbcTemplate.update(sql, typeId);
        if (affectedRows == 0) {
            throw new InputTypeNotFoundException("Deleting input type failed, no rows affected");
        }
    }

}
