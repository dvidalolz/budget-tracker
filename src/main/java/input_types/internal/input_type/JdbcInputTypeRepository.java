package input_types.internal.input_type;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    /**
     * Important Note: this method returns a list of types which has "lightweight"
     * version of user (Only userid)
     */
    @Override
    public List<InputType> findAllByUserId(Long userId) {

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
                    inputType.setUser(createLightUserWithId(rs.getLong("user_id")));
                    inputTypes.add(inputType);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching input types by user ID: " + userId, e);
        }

        return inputTypes;

    }

    /**
     * Important Note: this method returns optional of type which has
     * "lightweight" version of user (Only userid)
     */
    @Override
    public Optional<InputType> findByInputTypeId(Long typeId) {
        String sql = "SELECT id, type_name, user_id FROM T_InputType WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, typeId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    InputType inputType = new InputType();
                    inputType.setId(rs.getLong("id"));
                    inputType.setName(rs.getString("type_name"));
                    inputType.setUser(createLightUserWithId(rs.getLong("user_id")));

                    return Optional.of(inputType);
                }

            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching input types by type ID: " + typeId, e);
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
                throw new SQLException("Deleting input type failed, no rows affected");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error delete inputtype with id: " + typeId, e);
        }
    }

    /**
     * Create lightweight user object with id only
     * 
     * Used for light association between user and type
     */
    private User createLightUserWithId(Long userId) {
        User user = new User();
        user.setId(userId);
        return user;
    }

}
