package io.spring.training.corespring.personalbudgettracker.user_input.internal.input;

import org.springframework.context.annotation.Profile;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import io.spring.training.corespring.personalbudgettracker.common.date.SimpleDate;
import io.spring.training.corespring.personalbudgettracker.common.money.MonetaryAmount;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.exceptions.InputExceptions;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.input_subtype.InputSubType;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.input_type.InputType;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.user.User;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Repository
@Profile("jdbc")
public class JdbcInputRepository implements InputRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcInputRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Input> inputRowMapper = (rs, rowNum) -> {
        Input input = new Input();
        input.setId(rs.getLong("id"));
        input.setAmount(new MonetaryAmount(rs.getBigDecimal("amount")));
        input.setDate(SimpleDate.valueOf(rs.getDate("input_date")));
        input.setType(new InputType(rs.getLong("type_id"), rs.getString("type_name")));
        input.setUser(new User(rs.getLong("user_id")));
        
        Long subtypeId = rs.getLong("subtype_id");
        if (!rs.wasNull()) {
            input.setSubType(new InputSubType(subtypeId, rs.getString("subtype_name")));
        }
        
        return input;
    };

    /**
     * Returns an input with these limitations:
     * User with only userid, inputType with only id, subtype with only id
     */
    @Override
    public Input save(Input input) {
        if (input.getId() == null) {
            String sql = "INSERT INTO T_Input (amount, input_date, user_id, input_type_id, input_subtype_id) VALUES (?, ?, ?, ?, ?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(con -> {
                PreparedStatement ps = con.prepareStatement(sql, new String[]{"id"});
                ps.setBigDecimal(1, input.getAmount().asBigDecimal());
                ps.setDate(2, java.sql.Date.valueOf(input.getDate().toString()));
                ps.setLong(3, input.getUser().getId());
                ps.setLong(4, input.getType().getId());
                if (input.getSubtype() != null) {
                    ps.setLong(5, input.getSubtype().getId());
                } else {
                    ps.setNull(5, java.sql.Types.NULL);
                }
                return ps;
            }, keyHolder);
            input.setId(keyHolder.getKey().longValue());
        } else {
            String sql = "UPDATE T_Input SET amount = ?, input_date = ?, user_id = ?, input_type_id = ?, input_subtype_id = ? WHERE id = ?";
            jdbcTemplate.update(sql,
                    input.getAmount().asBigDecimal(),
                    java.sql.Date.valueOf(input.getDate().toString()),
                    input.getUser().getId(),
                    input.getType().getId(),
                    input.getSubtype() != null ? input.getSubtype().getId() : null,
                    input.getId());
        }
        return input;
    }

    /**
     * Returns an empty optional if not found 
     * Returns an input with user, type/subtype with ids only
     */
    @Override
    public Optional<Input> findById(Long id) {
        try {
            String sql = "SELECT * FROM T_Input WHERE id = ?";
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, inputRowMapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    /**
     * Returns an empty list if not found 
     * Returns inputs with user, type/subtype with ids only
     */
    @Override
    public List<Input> findAllByUserId(Long userId) {
        String sql = "SELECT i.*, it.id as type_id, it.type_name, ist.id as subtype_id, ist.subtype_name FROM T_Input i " +
                     "INNER JOIN T_InputType it ON i.input_type_id = it.id " +
                     "LEFT JOIN T_InputSubType ist ON i.input_subtype_id = ist.id " +
                     "WHERE i.user_id = ?";
        return jdbcTemplate.query(sql,inputRowMapper, userId);
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM T_Input WHERE id = ?";
        int rows = jdbcTemplate.update(sql, id);
        if (rows == 0) {
            throw new InputExceptions.InputDeletionException("Could not delete input with ID " + id);
        }
    }
}
