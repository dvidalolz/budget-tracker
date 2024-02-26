package users.internal.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Optional;

import javax.sql.DataSource;

public class JdbcUserRepository implements UserRepository {

    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /*
     * Save user to repository
     */
    @Override
    public User save(User user) {

        String insertSql = "INSERT INTO T_user (user_name, password_hash, email) VALUES (?, ?, ?)";

        String updateSql = "UPDATE T_user SET user_name = ?, password_hash = ?, email = ? WHERE id = ?";

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet generatedKeys = null;

        try {
            conn = dataSource.getConnection();

            if (user.getId() != null && user.getId() > 0) {
                ps = conn.prepareStatement(updateSql);
                ps.setString(1, user.getUsername());
                ps.setString(2, user.getPasswordHash());
                ps.setString(3, user.getEmail());
                ps.setLong(4, user.getId());
            } else {
                ps = conn.preparedStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
            }
        }
    }

    @Override
    public Optional<User> findById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findById'");
    }

    @Override
    public Optional<User> findByUsername(String username) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByUsername'");
    }

    @Override
    public void deleteById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteById'");
    }
    
}
