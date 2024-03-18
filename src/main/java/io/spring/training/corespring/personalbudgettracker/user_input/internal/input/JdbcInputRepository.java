package io.spring.training.corespring.personalbudgettracker.user_input.internal.input;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import io.spring.training.corespring.personalbudgettracker.common.date.SimpleDate;
import io.spring.training.corespring.personalbudgettracker.common.money.MonetaryAmount;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.exceptions.InputExceptions;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.input_subtype.InputSubType;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.input_type.InputType;
import io.spring.training.corespring.personalbudgettracker.user_input.internal.user.User;


@Repository
public class JdbcInputRepository implements InputRepository {

    private DataSource dataSource;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Input save(Input input) {
        String insertSql = "INSERT INTO T_Input (amount, input_date, user_id, input_type_id, input_subtype_id) " +
                "VALUES (?, ?, ?, ?, ?)";
        String updateSql = "UPDATE T_Input SET amount = ?, input_date = ?, user_id = ?, " +
                "input_type_id = ?, input_subtype_id = ? WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
                PreparedStatement ps = (input.getId() == null)
                        ? conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)
                        : conn.prepareStatement(updateSql)) {

            ps.setBigDecimal(1, input.getAmount().asBigDecimal());
            ps.setDate(2, new java.sql.Date(input.getDate().asDate().getTime()));
            ps.setLong(3, input.getUser().getId());
            ps.setLong(4, input.getType().getId());

            if (input.getSubtype() != null) {
                ps.setLong(5, input.getSubtype().getId());
            } else {
                ps.setNull(5, java.sql.Types.BIGINT); // Handle case where subtype is null
            }

            // For update, set the ID parameter
            if (input.getId() != null) {
                ps.setLong(6, input.getId());
            }

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new InputExceptions.InputSaveException("Saving input failed, no rows affected.");
            }

            // Retrieve the auto-generated ID for new inputs
            if (input.getId() == null) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        input.setId(generatedKeys.getLong(1)); // Set the generated key as the input's ID
                    } else {
                        throw new InputExceptions.InputSaveException("Creating input failed, no ID obtained.");
                    }
                }
            }

            return input; // Return the input, now with an ID set if it was a new insertion

        } catch (SQLException e) {
            throw new InputExceptions.InputSaveException("Error saving input " + input.getId() + ": " + e.getMessage(), e);
        }
    }

    /**
     * Important note : Inputs returned has type with no subtype set or user,
     *  has user with only id, subtype with no type
     * Returns an empty list if no inputs found
     */
    @Override
    public List<Input> findAllByUserId(Long userId) {
        String sql = "SELECT i.id, i.amount, i.input_date, i.user_id, " + 
        "it.id AS type_id, it.type_name, " +
        "ist.id AS subtype_id, ist.subtype_name " +
        "FROM T_Input i " +
        "JOIN T_InputType it ON i.input_type_id = it.id " +
        "LEFT JOIN T_InputSubType ist ON i.input_subtype_id = ist.id " +
        "WHERE i.user_id = ?";

        List<Input> inputs = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    inputs.add(mapInput(rs));
                }
            }
        } catch (SQLException e) {
            throw new InputExceptions.InputNotFoundException("Error fetching inputs by user ID: " + userId, e);
        }

        return inputs;
    }

    /*
     * Important Note : The input returned has user with only id, type with no
     * user/subtype set subtype with no type (lightweight versions, all have id and
     * name)
     */
    public Optional<Input> findById(Long id) {
        String sql = "SELECT i.id, i.amount, i.input_date, " +
                "it.id AS type_id, it.type_name, " +
                "ist.id AS subtype_id, ist.subtype_name, " +
                "u.id AS user_id " +
                "FROM T_Input i " +
                "JOIN T_InputType it ON i.input_type_id = it.id " +
                "LEFT JOIN T_InputSubType ist ON i.input_subtype_id = ist.id " +
                "JOIN T_User u ON i.user_id = u.id " +
                "WHERE i.id = ?";

        try (Connection conn = dataSource.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapInput(rs));
                }
            }
        } catch (SQLException e) {
            throw new InputExceptions.InputNotFoundException("Error fetching input by ID: " + id, e);
        }

        return Optional.empty();
    }

    @Override
    public void deleteById(Long inputId) {

        String sql = "DELETE FROM T_Input WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, inputId);

            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                throw new InputExceptions.InputDeletionException("Deleting input failed, no rows affected.");
            }

        } catch (SQLException e) {
            throw new InputExceptions.InputDeletionException("Error delete input with id: " + inputId, e);
        }
    }
    
    // Input contains id, amount, date, type(lightweight), user(lightweight), and subtype if provided
    private Input mapInput(ResultSet rs) throws SQLException {
        Input input = new Input();
        input.setId(rs.getLong("id"));
        input.setAmount(new MonetaryAmount(rs.getBigDecimal("amount")));
        input.setDate(SimpleDate.valueOf(rs.getDate("input_date")));
        input.setType(mapInputType(rs));
        input.setUser(mapUser(rs));

        // Only set subtype if it exists in the result set
        if (rs.getLong("subtype_id") > 0) {
            input.setSubType(mapInputSubType(rs));
        }

        return input;
    }

    // Inputtype contains type id and typename (no user attribute)
    private InputType mapInputType(ResultSet rs) throws SQLException {
        InputType inputType = new InputType();
        inputType.setId(rs.getLong("type_id"));
        inputType.setName(rs.getString("type_name"));
        // No user details are set for InputType in this context
        return inputType;
    }

    // Inputsubtype contains id and name (no type attribute)
    private InputSubType mapInputSubType(ResultSet rs) throws SQLException {
        InputSubType inputSubType = new InputSubType();
        inputSubType.setId(rs.getLong("subtype_id"));
        inputSubType.setName(rs.getString("subtype_name"));
        // No type details are set for InputSubType in this context
        return inputSubType;
    }

    // User contains only id
    private User mapUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("user_id"));

        // Password hash is intentionally not set for security reasons
        return user;
    }

}
