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
     * Important Note: this method returns a list of types which has no set of subtypes and whose user has no password_hash
     */
    @Override
    public List<InputType> findAllByUserId(Long userId) {
        // Updated SQL query to include a JOIN with the User table and fetch required User fields
        String sql = "SELECT it.id AS input_type_id, it.type_name, u.id AS user_id, u.user_name, u.email " +
                     "FROM T_InputType it " +
                     "JOIN T_User u ON it.user_id = u.id " +
                     "WHERE it.user_id = ?";
    
        List<InputType> inputTypes = new ArrayList<>();
    
        try (Connection conn = dataSource.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
    
            ps.setLong(1, userId);
            
            // Create list of input types
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    InputType inputType = new InputType();
                    inputType.setId(rs.getLong("input_type_id"));
                    inputType.setName(rs.getString("type_name"));
    
                    // Fully fleshed User object : no password_hash
                    User user = new User();
                    user.setId(rs.getLong("user_id"));
                    user.setUsername(rs.getString("user_name"));
                    user.setEmail(rs.getString("email"));
                    
                    inputType.setUser(user);
                    inputTypes.add(inputType);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching input types by user ID: " + userId, e);
        }
    
        return inputTypes; // no set of types in these inputtypes
    }
    

    /**
     * Important Note : Important Note: this method returns a type which has no set of subtypes and whose user has no password_hash
     */
    @Override
    public Optional<InputType> findById(Long typeId) {
        // SQL to include a JOIN with the User table and fetch the required User fields
        String sql = "SELECT it.id AS input_type_id, it.type_name, u.id AS user_id, u.user_name, u.email " +
                     "FROM T_InputType it " +
                     "JOIN T_User u ON it.user_id = u.id " +
                     "WHERE it.id = ?";
    
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
    
            ps.setLong(1, typeId);
    
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    InputType inputType = new InputType();
                    inputType.setId(rs.getLong("input_type_id"));
                    inputType.setName(rs.getString("type_name"));
    
                    // Flesh Out user object : no password hash
                    User user = new User();
                    user.setId(rs.getLong("user_id"));
                    user.setUsername(rs.getString("user_name"));
                    user.setEmail(rs.getString("email"));
    
                    inputType.setUser(user); 
    
                    return Optional.of(inputType); // no set of subtypes in this input type
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching input type by ID: " + typeId, e);
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

}
