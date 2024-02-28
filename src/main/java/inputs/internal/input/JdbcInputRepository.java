package inputs.internal.input;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;



public class JdbcInputRepository implements InputRepository {

    private DataSource dataSource;

    public void JdbcInputRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Input save(Input input) {

        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'save'");
    }

    @Override
    public List<Input> findByUserId(Long userId) {
         // TODO Auto-generated method stub
         throw new UnsupportedOperationException("Unimplemented method 'findByUserId'");
      
    }

    @Override
    public void deleteById(Long inputId) {

        String sql = "DELETE FROM T_Input WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setLong(1, inputId);

            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Deleting input failed, no rows affected.");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error delete input with id: " + inputId, e);
        }
    }

    
}
