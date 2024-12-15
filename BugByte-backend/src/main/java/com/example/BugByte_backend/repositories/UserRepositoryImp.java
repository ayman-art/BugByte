package com.example.BugByte_backend.repositories;

import com.example.BugByte_backend.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class UserRepositoryImp implements UserRepository {
    private static final String SQL_INSERT_USER = """
                INSERT INTO users
                    (user_name, email, password, bio, reputation, is_admin, picture)
                VALUES
                    (?, ?, ?, "", 0, false, "");
            """;
    private static final String SQL_FIND_BY_ID = "SELECT * FROM users WHERE id = ?;";
    private static final String SQL_FIND_ID_BY_EMAIL = "SELECT id FROM users WHERE email = ?;";
    private static final String SQL_FIND_BY_IDENTITY = "SELECT * FROM users WHERE email = ? OR user_name = ?;";
    private static final String SQL_CHANGE_PASSWORD = "UPDATE users SET password = ? WHERE id = ?";
    private static final String SQL_DELETE_USER_BY_ID = "DELETE FROM users WHERE id = ?;";
    private static final String SQL_MAKE_USER_ADMIN = "UPDATE users SET is_admin = true WHERE id = ?";
    private static final String SQL_UPDATE_PICTURE = "UPDATE users SET picture = ? WHERE id = ?";
    private static final String SQL_INSERT_VALIDATION_CODE = """
                INSERT INTO validation_code
                    (id, code)
                VALUES
                    (?, ?);
            """;
    private static final String SQL_DELETE_VALIDATION_CODE = "DELETE FROM validation_code WHERE code = ?;";
    private static final String SQL_COUNT_VALIDATION_CODE = "SELECT COUNT(*) AS count FROM validation_code WHERE code = ?";
    private static final String SQL_COUNT_USER_IN_VALIDATION_CODE = "SELECT COUNT(*) AS count FROM validation_code WHERE id = ?";
    private static final String SQL_FIND_VALIDATION_CODE_BY_ID = "SELECT code FROM validation_code WHERE id = ?;";
    private static final String SQL_UPDATE_VALIDATION_CODE = "UPDATE validation_code SET code = ? WHERE id = ?;";

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
    public User findByIdentity(String identity) {
        if (identity == null)
            throw new NullPointerException("Identity is Null");
       return jdbcTemplate.queryForObject(SQL_FIND_BY_IDENTITY, userRowMapper, identity, identity);
    }

    @Override
    public User findById(Long userId) {
        if (userId == null)
            throw new NullPointerException("UserId is Null");

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
            throw new NullPointerException("UserId or password is Null");

        String hashedPassword = passwordEncoder.encode(newPassword);

        int rows = jdbcTemplate.update(SQL_CHANGE_PASSWORD, hashedPassword, userId);
        return rows == 1;
    }

    @Override
    public Boolean deleteUser(Long userId) {
        if (userId == null)
            throw new NullPointerException("UserId is Null");

        int rows = jdbcTemplate.update(SQL_DELETE_USER_BY_ID, userId);
        return rows == 1;
    }

    @Override
    public Boolean makeUserAdmin(Long userId) {
        if (userId == null)
            throw new NullPointerException("UserId is Null");

        int rows = jdbcTemplate.update(SQL_MAKE_USER_ADMIN, userId);
        return rows == 1;
    }

    @Override
    public Boolean addResetCode(Long userId, String code) {
        if (userId == null || code == null)
            throw new NullPointerException("UserId or code is null");

        int rows = 0;
        if (userExists(userId))
            rows = jdbcTemplate.update(SQL_UPDATE_VALIDATION_CODE, code, userId);
        else
            rows = jdbcTemplate.update(SQL_INSERT_VALIDATION_CODE, userId, code);

        return rows == 1;
    }

    @Override
    public Boolean deleteResetCode(String code) {
        if (code == null)
            throw new NullPointerException("Code is Null");

        int rows = jdbcTemplate.update(SQL_DELETE_VALIDATION_CODE, code);
        return rows == 1;
    }

    @Override
    public Boolean codeExists(String code) {
        Integer count = jdbcTemplate.queryForObject(SQL_COUNT_VALIDATION_CODE, new Object[]{code}, Integer.class);
        if (count == null)
            throw new RuntimeException("Invalid Input");

        return count == 1;
    }

    @Override
    public Boolean userExists(Long userId) {
        Integer count = jdbcTemplate.queryForObject(SQL_COUNT_USER_IN_VALIDATION_CODE, new Object[]{ userId }, Integer.class);
        if (count == null)
            throw new RuntimeException("Invalid Input");

        return count == 1;
    }

    @Override
    public String getCodeById(Long id) {
        if (id == null)
            throw new NullPointerException("UserId is Null");

        return jdbcTemplate.queryForObject(SQL_FIND_VALIDATION_CODE_BY_ID, new Object[]{ id }, String.class);
    }

    @Override
    public void updateProfilePicture(Long userId, String URL) throws Exception {
        if (userId == null)
            throw new NullPointerException("UserId is Null");
        int rows = jdbcTemplate.update(SQL_UPDATE_PICTURE, URL, userId);
        if (rows != 1) throw new Exception("Picture update failed");
    }

    private final RowMapper<User> userRowMapper = ((rs, rowNum) -> User.builder()
            .id(rs.getLong("id"))
            .userName(rs.getString("user_name"))
            .email(rs.getString("email"))
            .password(rs.getString("password"))
            .bio(rs.getString("bio"))
            .reputation(rs.getLong("reputation"))
            .isAdmin(rs.getBoolean("is_admin"))
            .picture("picture")
            .build()
    );
}
