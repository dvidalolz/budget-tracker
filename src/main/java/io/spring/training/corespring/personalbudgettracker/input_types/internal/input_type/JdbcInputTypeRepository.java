package io.spring.training.corespring.personalbudgettracker.input_types.internal.input_type;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import io.spring.training.corespring.personalbudgettracker.exceptions.InputTypeExceptions;
import io.spring.training.corespring.personalbudgettracker.users.internal.user.User;


public class JdbcInputTypeRepository implements InputTypeRepository {

    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Returns a fully fleshed, saved inputtype object
     */
    @Override
    public InputType save(InputType inputType) {

        String insertSql = "INSERT INTO T_InputType (type_name, user_id) VALUES (?, ?)";

        String updateSql = "UPDATE T_InputType SET type_name = ?, user_id = ? WHERE id = ?";

        // If inputtype.id not exist do insert, else do update
        try (Connection conn = dataSource.getConnection();
                PreparedStatement ps = (inputType.getId() == null)
                        ? conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)
                        : conn.prepareStatement(updateSql)) {

            ps.setString(1, inputType.getName());
            ps.setLong(2, inputType.getUser().getId());

            // If inputtype.id does exist, do update - include input id
            if (inputType.getId() != null) {
                ps.setLong(3, inputType.getId());
            }

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new InputTypeExceptions.InputTypeSaveException("Creating/updating input type failed, no rows affected.");
            }

            // if insert, check auto-generated key (id) and set return input type object
            if (inputType.getId() == null) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        inputType.setId(generatedKeys.getLong(1));
                    } else {
                        throw new InputTypeExceptions.InputTypeSaveException("Creating input type failed, no id obtained.");
                    }
                }
            }

            return inputType;
        } catch (SQLException e) {
            throw new InputTypeExceptions.InputTypeSaveException("Error saving input type" + inputType.getName(), e);
        }

    }

    /**
     * Important Note: this method returns a list of types whose user has only id
     */
    @Override
    public List<InputType> findAllByUserId(Long userId) {
        // Updated SQL query to include a JOIN with the User table and fetch required
        // User fields
        String sql = "SELECT it.id AS input_type_id, it.type_name, u.id AS user_id " +
                "FROM T_InputType it " +
                "JOIN T_User u ON it.user_id = u.id " +
                "WHERE it.user_id = ?";

        List<InputType> inputTypes = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    inputTypes.add(mapInputType(rs));
                }
            }
        } catch (SQLException e) {
            throw new InputTypeExceptions.InputTypeNotFoundException("Error fetching input types by user ID: " + userId, e);
        }

        return inputTypes;
    }

    /**
     * Important Note : Important Note: this method returns a type whose user has only id
     */
    @Override
    public Optional<InputType> findById(Long typeId) {
        // SQL to include a JOIN with the User table and fetch the required User fields
        String sql = "SELECT it.id AS input_type_id, it.type_name, u.id AS user_id " +
                "FROM T_InputType it " +
                "JOIN T_User u ON it.user_id = u.id " +
                "WHERE it.id = ?";

        try (Connection conn = dataSource.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, typeId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapInputType(rs));
                }
            }
        } catch (SQLException e) {
            throw new InputTypeExceptions.InputTypeNotFoundException("Error fetching input type by ID: " + typeId, e);
        }

        return Optional.empty();
    }

    @Override
    public void deleteById(Long typeId) {
        String sql = "DELETE FROM T_InputType WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, typeId);

            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                throw new InputTypeExceptions.InputTypeDeletionException("Deleting input type failed, no rows affected");
            }

        } catch (SQLException e) {
            throw new InputTypeExceptions.InputTypeDeletionException("Error delete inputtype with id: " + typeId, e);
        }
    }

    private InputType mapInputType(ResultSet rs) throws SQLException {
        InputType inputType = new InputType();
        inputType.setId(rs.getLong("input_type_id"));
        inputType.setName(rs.getString("type_name"));
        inputType.setUser(mapUser(rs));
        return inputType;
    }

    private User mapUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("user_id"));
        // Note: Password hash is intentionally omitted for security reasons
        return user;
    }

}
