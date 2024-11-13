package com.example.BugByte_backend.repositories;

import com.example.BugByte_backend.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;


@Repository
public class UserRepositoryImp implements UserRepository {
    private static final String SQL_INSERT_USER = """
                INSERT INTO users
                    (user_name, email, password, reputation, is_admin)
                VALUES
                    (?, ?, ?, 0, false);
            """;
    private static final String SQL_FIND_BY_ID = "SELECT * FROM users WHERE id = ?;";
    private static final String SQL_FIND_ID_BY_EMAIL = "SELECT id FROM users WHERE email = ?;";
    private static final String SQL_FIND_BY_IDENTITY = "SELECT * FROM users WHERE email = ? OR user_name = ?;";
    private static final String SQL_CHANGE_PASSWORD = "UPDATE users SET password = ? WHERE id = ?";
    private static final String SQL_DELETE_USER_BY_ID = "DELETE FROM users WHERE id = ?;";
    private static final String SQL_MAKE_USER_ADMIN = "UPDATE users SET is_admin = true WHERE id = ?";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Long insertUser(String userName, String email, String password) {
        if (userName == null || email == null || password == null)
            throw new NullPointerException("username or email or password is null");

        String hashedPassword = passwordEncoder.encode(password);
        int rows = jdbcTemplate.update(SQL_INSERT_USER, userName, email, hashedPassword);

        if (rows == 0)
            throw new RuntimeException("Invalid input");

        return findIdByEmail(email);
    }

    @Override
    public User findByIdentityAndPassword(String identity, String password) {
        if (identity == null || password == null)
            throw new NullPointerException("Identity or password is Null");

        User user = jdbcTemplate.queryForObject(SQL_FIND_BY_IDENTITY, userRowMapper, identity, identity);
        if (user != null && passwordEncoder.matches(password, user.getPassword()))
            return user;
        return null;
    }

    @Override
    public User findById(Long userId) {
        if (userId == null)
            throw new NullPointerException("Null user id");

        return jdbcTemplate.queryForObject(SQL_FIND_BY_ID, userRowMapper, userId);
    }

    @Override
    public Long findIdByEmail(String email) {
        if (email == null)
            throw new NullPointerException("Email is Null");

        return jdbcTemplate.queryForObject(SQL_FIND_ID_BY_EMAIL, new Object[]{email}, Long.class);
    }

    @Override
    public Boolean changePassword(Long userId, String newPassword) {
        if (userId == null || newPassword == null)
            throw new NullPointerException("Null user id or password");

        String hashedPassword = passwordEncoder.encode(newPassword);

        int rows = jdbcTemplate.update(SQL_CHANGE_PASSWORD, hashedPassword, userId);
        return rows == 1;
    }

    @Override
    public Boolean deleteUser(Long userId) {
        if (userId == null)
            throw new NullPointerException("Null user id");

        int rows = jdbcTemplate.update(SQL_DELETE_USER_BY_ID, userId);
        return rows == 1;
    }

    @Override
    public Boolean makeUserAdmin(Long userId) {
        if (userId == null)
            throw new NullPointerException("Null user id");

        int rows = jdbcTemplate.update(SQL_MAKE_USER_ADMIN, userId);
        return rows == 1;
    }

    @Override
    public Boolean add_reset_code(Long userId, String code) {
        return null;
    }

    public User findByIdentity(String identity){
        // this function returns the user by identity to send code to the user's email
        return null;
    }

    public boolean codeExists(String code){
        // this function checks if the code already exist while generating a random code
        return false;
    }
    public boolean deleteCode(String code){
        return true;
    }

    public String getCodeById(long id){
        return null;
    }

    private final RowMapper<User> userRowMapper = ((rs, rowNum) -> new User(
            rs.getLong("id"),
            rs.getString("user_name"),
            rs.getString("email"),
            rs.getString("password"),
            rs.getLong("reputation"),
            rs.getBoolean("is_admin")
    ));
}
