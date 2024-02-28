package input_types.internal.input_subtype;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import input_types.internal.input_type.InputType;

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
            throw new RuntimeException("Error saving input subtype " + inputSubType.getName() + ": " + e.getMessage(),
                    e);
        }
    }

    /**
     * Important Note: this method returns a list of subtypes which has
     * "lightweight" version of input_type (Only typeId)
     */
    @Override
    public List<InputSubType> findAllByTypeId(Long typeId) {
        String sql = "SELECT id, subtype_name, input_type_id FROM T_InputSubType WHERE input_type_id = ?";

        List<InputSubType> inputSubTypes = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, typeId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    InputSubType inputSubType = new InputSubType();
                    inputSubType.setId(rs.getLong("id"));
                    inputSubType.setName(rs.getString("subtype_name"));
                    inputSubType.setType(createLightTypeWithId(rs.getLong("input_type_id")));
                    inputSubTypes.add(inputSubType);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching input types by type ID: " + typeId, e);
        }

        return inputSubTypes;
    }

    /**
     * Important Note: this method returns optional of subtype which has
     * "lightweight" version of input_type (Only typeId)
     */
    @Override
    public Optional<InputSubType> findBySubTypeId(Long subTypeId) {
        String sql = "SELECT id, subtype_name, input_type_id FROM T_InputSubType WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, subTypeId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    InputSubType inputSubType = new InputSubType();
                    inputSubType.setId(rs.getLong("id"));
                    inputSubType.setName(rs.getString("subtype_name"));
                    inputSubType.setType(createLightTypeWithId(rs.getLong("input_type_id")));
                    return Optional.of(inputSubType);
                }

            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching input sub types by sub type ID: " + subTypeId, e);
        }

        return Optional.empty();
    }

    @Override
    public void deleteById(Long subTypeId) {
        String sql = "DELETE FROM T_InputSubType WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, subTypeId);

            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Deleting input subtype failed, no rows affected");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error delete input subtype with id: " + subTypeId, e);
        }
    }

    /**
     * Create lightweight type object with id only
     * 
     * Used for light association between subtype and type
     */
    private InputType createLightTypeWithId(Long typeId) {
        InputType inputType = new InputType();
        inputType.setId(typeId);
        return inputType;
    }

}
