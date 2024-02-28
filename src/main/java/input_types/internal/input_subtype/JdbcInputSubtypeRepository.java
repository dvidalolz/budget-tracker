package input_types.internal.input_subtype;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import input_types.internal.input_type.InputType;
import users.internal.user.User;

public class JdbcInputSubtypeRepository implements InputSubtypeRepository {

    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public InputSubType save(InputSubType inputSubType) {
        String insertSql = "INSERT INTO T_InputSubType (subtype_name, input_type_id) VALUES (?, ?)";

        String updateSql = "UPDATE T_InputSubType SET subtype_name = ?, input_type_id = ? WHERE id = ?";

        // If subtype.id not exist do insert, else do update
        try (Connection conn = dataSource.getConnection();
                PreparedStatement ps = (inputSubType.getId() == null)
                        ? conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)
                        : conn.prepareStatement(updateSql)) {

            ps.setString(1, inputSubType.getName());
            ps.setLong(2, inputSubType.getType().getId());

            // If subtype.id does exist, do update - include input id
            if (inputSubType.getId() != null) {
                ps.setLong(3, inputSubType.getId());
            }

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating/updating input subtype failed, no rows affected.");
            }

            // if insert, check auto-generated key (id) and set return input type object
            if (inputSubType.getId() == null) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        inputSubType.setId(generatedKeys.getLong(1));
                    } else {
                        throw new SQLException("Creating input subtype failed, no id obtained");
                    }
                }
            }

            return inputSubType;
        } catch (SQLException e) {
            throw new RuntimeException("Error saving input type" + inputSubType.getName(), e);
        }
    }

    @Override
    public List<InputSubType> findByTypeId(Long typeId) {
        String sql = "SELECT id, subtype_name, input_type_id FROM T_InputSubType WHERE user_id = ?";

        List<InputSubType> inputSubTypes = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, typeId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    InputSubType inputSubType = new InputSubType();
                    inputSubType.setId(rs.getLong("id"));
                    inputSubType.setName(rs.getString("type_name"));
                    
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching input types by type ID: " + typeId, e);
        }

        return inputSubTypes;
    }

    @Override
    public void deleteById(Long subTypeId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteById'");
    }

}
