package com.example.BugByte_backend.repositories;

import com.example.BugByte_backend.models.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
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
        return null;
    }

    @Override
    public Integer getCountByEmail(String email) {
        return null;
    }

    @Override
    public Integer getCountByUsername(String userName) {
        return null;
    }

    @Override
    public UserModel findById(Integer userId) {
        return null;
    }

    @Override
    public Boolean changePassword(Integer userId, String newPassword) {
        return null;
    }

    @Override
    public Boolean deleteUser(Integer userId) {
        return null;
    }

    @Override
    public Boolean makeUserAdmin(Integer userId) {
        return null;
    }
}
