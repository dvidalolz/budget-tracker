package users.internal.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

            // check if user alreaady has an id (existing user)
            if (user.getId() != null && user.getId() > 0) {
                // update existing user
                ps = conn.prepareStatement(updateSql);
                ps.setString(1, user.getUsername());
                ps.setString(2, user.getPasswordHash());
                ps.setString(3, user.getEmail());
                ps.setLong(4, user.getId());
            } else {
                // insert new user
                ps = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, user.getUsername());
                ps.setString(2, user.getPasswordHash());
                ps.setString(3, user.getEmail());
            }

            int affectedRows = ps.executeUpdate(); // execute insert/update

            // check if insert operation was succesful and retrieve the generated key (id)
            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }

            // if this was an insert, get the generated key and set it to returning user
            // object
            if (user.getId() == null) {
                generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Creating user failed, no id obtained.");
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error saving user", e);
        } finally {
            // clean
            if (generatedKeys != null) {
                try {
                    generatedKeys.close();
                } catch (SQLException e) {
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }

        return user;
    }

    @Override
    public Optional<User> findById(Long id) {
        // SQL query to fetch a user by ID
        String sql = "SELECT id, username, email, password_hash FROM users WHERE id = ?";
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
