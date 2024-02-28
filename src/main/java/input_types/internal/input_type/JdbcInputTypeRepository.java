package input_types.internal.input_type;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import users.internal.user.User;


public class JdbcInputTypeRepository implements InputTypeRepository {

    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

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
                throw new SQLException("Creating/updating input type failed, no rows affected.");
            }

            // if insert, check auto-generated key (id) and set return input type object
            if (inputType.getId() == null) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        inputType.setId(generatedKeys.getLong(1));
                    } else {
                        throw new SQLException("Creating input type failed, no id obtained");
                    }
                }
            }

            return inputType;
        } catch (SQLException e) {
            throw new RuntimeException("Error saving input type" + inputType.getName(), e);
        }

    }

    // Finds all associated input types of user by id : "light" version of user (with id only) are input types' attribute
    @Override
    public List<InputType> findByUserId(Long userId) {

        String sql = "SELECT id, type_name, user_id FROM T_InputType WHERE user_id = ?";

        List<InputType> inputTypes = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, userId); 

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    InputType inputType = new InputType();
                    inputType.setId(rs.getLong("id"));
                    inputType.setName(rs.getString("type_name"));
                    User user = new User();
                    user.setId(rs.getLong("user_id"));  // "lightweight" user attribute for all input types (userId only)
                    inputType.setUser(user);
                    inputTypes.add(inputType);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching input types by user ID: " + userId, e);
        }

        return inputTypes;

    }

    @Override
    public void deleteById(Long typeId) {
        String sql = "DELETE FROM T_InputType WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, typeId);

            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Deleting input type failed, no rows affected");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error delete inputtype with id: " + typeId, e);
        }
    }

}
