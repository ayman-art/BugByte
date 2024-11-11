package com.example.BugByte_backend.repositories;

import com.example.BugByte_backend.models.UserModel;
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
    private static final String SQL_COUNT_BY_EMAIL = "SELECT COUNT(*) FROM users WHERE email = ?;";
    private static final String SQL_COUNT_BY_USERNAME = "SELECT COUNT(*) FROM users WHERE user_name = ?;";
    private static final String SQL_FIND_BY_ID = "SELECT * FROM users WHERE id = ?;";
    private static final String SQL_FIND_BY_IDENTITY = "SELECT * FROM users WHERE email = ? OR user_name = ?;";
    private static final String SQL_CHANGE_PASSWORD = "UPDATE users SET password = ? WHERE id = ?";
    private static final String SQL_DELETE_USER_BY_ID = "DELETE FROM users WHERE id = ?;";
    private static final String SQL_MAKE_USER_ADMIN = "UPDATE users SET is_admin = true WHERE id = ?";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Integer insertUser(String userName, String email, String password) {
        String hashedPassword = passwordEncoder.encode(password);
        return jdbcTemplate.update(SQL_INSERT_USER, userName, email, hashedPassword);
    }

    @Override
    public UserModel findByIdentityAndPassword(String identity, String password) {
        UserModel user = jdbcTemplate.queryForObject(SQL_FIND_BY_IDENTITY, userRowMapper, identity, identity);
        if (user != null && passwordEncoder.matches(password, user.getPassword()))
            return user;
        return null;
    }

    @Override
    public Integer getCountByEmail(String email) {
        return jdbcTemplate.queryForObject(SQL_COUNT_BY_EMAIL, new Object[]{email}, Integer.class);
    }

    @Override
    public Integer getCountByUsername(String userName) {
        return jdbcTemplate.queryForObject(SQL_COUNT_BY_USERNAME, new Object[]{userName}, Integer.class);
    }

    @Override
    public UserModel findById(Long userId) {
        return jdbcTemplate.queryForObject(SQL_FIND_BY_ID, userRowMapper, userId);
    }

    @Override
    public Boolean changePassword(Long userId, String newPassword) {
        String hashedPassword = passwordEncoder.encode(newPassword);

        int rows = jdbcTemplate.update(SQL_CHANGE_PASSWORD, hashedPassword, userId);
        return rows == 1;
    }

    @Override
    public Boolean deleteUser(Long userId) {
        int rows = jdbcTemplate.update(SQL_DELETE_USER_BY_ID, userId);
        return rows == 1;
    }

    @Override
    public Boolean makeUserAdmin(Long userId) {
        int rows = jdbcTemplate.update(SQL_MAKE_USER_ADMIN, userId);
        return rows == 1;
    }

    private final RowMapper<UserModel> userRowMapper = ((rs, rowNum) -> new UserModel(
            rs.getLong("id"),
            rs.getString("user_name"),
            rs.getString("email"),
            rs.getString("password"),
            rs.getLong("reputation"),
            rs.getBoolean("is_admin")
    ));
}
