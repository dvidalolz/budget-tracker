package io.spring.training.corespring.personalbudgettracker.user_input.internal.input_subtype;

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

import io.spring.training.corespring.personalbudgettracker.user_input.internal.exceptions.InputTypeExceptions;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.input_type.InputType;

@Repository
@Profile("jdbc")
public class JdbcInputSubTypeRepository implements InputSubTypeRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcInputSubTypeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<InputSubType> inputSubTypeRowMapper = (rs, rowNum) -> {
        InputSubType inputSubType = new InputSubType();
        inputSubType.setId(rs.getLong("subtype_id"));
        inputSubType.setName(rs.getString("subtype_name"));
        InputType inputType = new InputType();
        inputType.setId(rs.getLong("type_id"));
        inputType.setName(rs.getString("type_name"));
        inputSubType.setType(inputType);
        return inputSubType;
    };

    /**
     * Handles create and update
     */
    @Override
    public InputSubType save(InputSubType inputSubType) {
        if (inputSubType.getId() == null) {
            String insertSql = "INSERT INTO T_InputSubType (subtype_name, input_type_id) VALUES (?, ?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
                    ps.setString(1, inputSubType.getName());
                    ps.setLong(2, inputSubType.getType().getId());
                    return ps;
                },
                keyHolder);
            Number key = keyHolder.getKey();
            if (key != null) {
                inputSubType.setId(key.longValue());
            } else {
                throw new InputTypeExceptions.InputSubTypeSaveException("Creating input subtype failed, no ID obtained.");
            }
        } else {
            String updateSql = "UPDATE T_InputSubType SET subtype_name = ?, input_type_id = ? WHERE id = ?";
            jdbcTemplate.update(updateSql, inputSubType.getName(), inputSubType.getType().getId(), inputSubType.getId());
        }
        return inputSubType;
    }

    /**
     * Important note: Returns a list of inputsubtypes' which have a type which has
     * no user
     */
    @Override
    public List<InputSubType> findAllByTypeId(Long typeId) {
        String sql = "SELECT ist.id AS subtype_id, ist.subtype_name, it.id AS type_id, it.type_name " +
                     "FROM T_InputSubType ist " +
                     "JOIN T_InputType it ON ist.input_type_id = it.id " +
                     "WHERE ist.input_type_id = ?";
        return jdbcTemplate.query(sql, inputSubTypeRowMapper, typeId);
    }

    /**
     * Important note: Returns an input subtype which has a type, has no user or set
     * of subtypes
     */
    @Override
    public Optional<InputSubType> findById(Long subTypeId) {
        String sql = "SELECT ist.id AS subtype_id, ist.subtype_name, it.id AS type_id, it.type_name " +
                     "FROM T_InputSubType ist " +
                     "JOIN T_InputType it ON ist.input_type_id = it.id " +
                     "WHERE ist.id = ?";
        List<InputSubType> results = jdbcTemplate.query(sql, inputSubTypeRowMapper, subTypeId);
        return results.stream().findFirst();
    }

    @Override
    public void deleteById(Long subTypeId) {
        String sql = "DELETE FROM T_InputSubType WHERE id = ?";
        int affectedRows = jdbcTemplate.update(sql, subTypeId);
        if (affectedRows == 0) {
            throw new InputTypeExceptions.InputSubTypeDeletionException("Deleting input subtype failed, no rows affected");
        }
    }

}
