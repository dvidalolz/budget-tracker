package inputs.internal.input;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import common.date.SimpleDate;
import common.money.MonetaryAmount;
import input_types.internal.input_subtype.InputSubType;
import input_types.internal.input_type.InputType;
import users.internal.user.User;

public class JdbcInputRepository implements InputRepository {

    private DataSource dataSource;

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
                throw new SQLException("Saving input failed, no rows affected.");
            }
    
            // Retrieve the auto-generated ID for new inputs
            if (input.getId() == null) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        input.setId(generatedKeys.getLong(1)); // Set the generated key as the input's ID
                    } else {
                        throw new SQLException("Creating input failed, no ID obtained.");
                    }
                }
            }
    
            return input; // Return the input, now with an ID set if it was a new insertion
    
        } catch (SQLException e) {
            throw new RuntimeException("Error saving input " + input.getId() + ": " + e.getMessage(), e);
        }
    }
    

    /**
     * Important note : Inputs returned has type with no subtype set or user, user
     * with only id, subtype with no type
     */
    @Override
    public List<Input> findByUserId(Long userId) {
        String sql = "SELECT i.id, i.amount, i.input_date, " +
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
                    Input input = new Input();
                    input.setId(rs.getLong("id"));
                    input.setAmount(new MonetaryAmount(rs.getBigDecimal("amount")));
                    input.setDate(SimpleDate.valueOf(rs.getDate("input_date")));

                    InputType type = new InputType();
                    type.setId(rs.getLong("type_id"));
                    type.setName(rs.getString("type_name"));
                    input.setType(type);

                    if (rs.getLong("subtype_id") > 0) { // Check if subtype exists
                        InputSubType subType = new InputSubType();
                        subType.setId(rs.getLong("subtype_id"));
                        subType.setName(rs.getString("subtype_name"));
                        input.setSubType(subType);
                    }

                    // Set a lightweight User object with only the userId
                    User user = new User();
                    user.setId(userId);
                    input.setUser(user);

                    inputs.add(input);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching inputs by user ID: " + userId, e);
        }

        return inputs;
    }

    /*
     * Important Note : The input returned has user with no passhash, type with no
     * user/subtype set
     * subtype with no type (lightweight versions, all have id and name)
     */
    public Optional<Input> findById(Long id) {
        String sql = "SELECT i.id, i.amount, i.input_date, " +
                "it.id AS type_id, it.type_name, " +
                "ist.id AS subtype_id, ist.subtype_name, " +
                "u.id AS user_id, u.user_name, u.email " +
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
                    Input input = new Input();
                    input.setId(rs.getLong("id"));
                    input.setAmount(new MonetaryAmount(rs.getBigDecimal("amount")));
                    input.setDate(new SimpleDate(rs.getDate("input_date")));

                    User user = new User();
                    user.setId(rs.getLong("user_id"));
                    user.setUsername(rs.getString("user_name"));
                    user.setEmail(rs.getString("email"));
                    input.setUser(user);

                    InputType type = new InputType();
                    type.setId(rs.getLong("type_id"));
                    type.setName(rs.getString("type_name"));
                    input.setType(type);

                    if (rs.getLong("subtype_id") > 0) { // Check if subtype exists : can be null
                        InputSubType subType = new InputSubType();
                        subType.setId(rs.getLong("subtype_id"));
                        subType.setName(rs.getString("subtype_name"));
                        input.setSubType(subType);
                    }

                    return Optional.of(input);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching input by ID: " + id, e);
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
                throw new RuntimeException("Deleting input failed, no rows affected.");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error delete input with id: " + inputId, e);
        }
    }

}
