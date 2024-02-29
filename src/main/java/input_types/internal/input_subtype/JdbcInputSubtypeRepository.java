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
            throw new RuntimeException("Error saving input subtype " + inputSubType.getName() + ": " + e.getMessage(), e);
        }
    }

    /**
     * Important note: Returns a list of inputsubtypes' which have a type which has no user or set of subtypes
     */
    @Override
    public List<InputSubType> findAllByTypeId(Long typeId) {
        String sql = "SELECT ist.id AS subtype_id, ist.subtype_name, it.id AS type_id, it.type_name " +
                     "FROM T_InputSubType ist " +
                     "JOIN T_InputType it ON ist.input_type_id = it.id " +
                     "WHERE ist.input_type_id = ?";
    
        List<InputSubType> inputSubTypes = new ArrayList<>();
    
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
    
            ps.setLong(1, typeId);
    
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    inputSubTypes.add(mapInputSubType(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching input subtypes by type ID: " + typeId, e);
        }
    
        return inputSubTypes;
    }
    

    /**
     * Important note: Returns an input subtype which has a type, has no user or set of subtypes
     */
    @Override
    public Optional<InputSubType> findById(Long subTypeId) {
        String sql = "SELECT ist.id AS subtype_id, ist.subtype_name, it.id AS type_id, it.type_name " +
                     "FROM T_InputSubType ist " +
                     "JOIN T_InputType it ON ist.input_type_id = it.id " +
                     "WHERE ist.id = ?";
    
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
    
            ps.setLong(1, subTypeId);
    
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapInputSubType(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching input subtype by ID: " + subTypeId, e);
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
                throw new RuntimeException("Deleting input subtype failed, no rows affected");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error delete input subtype with id: " + subTypeId, e);
        }
    }

    private InputSubType mapInputSubType(ResultSet rs) throws SQLException {
        InputSubType inputSubType = new InputSubType();
        inputSubType.setId(rs.getLong("subtype_id"));
        inputSubType.setName(rs.getString("subtype_name"));
        inputSubType.setType(mapLightInputType(rs));
        return inputSubType;
    }
    
    private InputType mapLightInputType(ResultSet rs) throws SQLException {
        InputType inputType = new InputType();
        inputType.setId(rs.getLong("type_id"));
        inputType.setName(rs.getString("type_name"));
        // Note: User and subtypes are not set to avoid unnecessary data loading and potential circular dependencies
        return inputType;
    }
    

}
