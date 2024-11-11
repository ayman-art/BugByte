package com.example.BugByte_backend.repositories;

import com.example.BugByte_backend.models.User;

public interface UserRepository {
    Integer insertUser(String userName, String email, String password);

    User findByIdentityAndPassword(String identity, String password);

    Integer getCountByEmail(String email);

    Integer getCountByUsername(String userName);

    User findById(Long userId);

    Boolean changePassword(Long userId, String newPassword);

    Boolean deleteUser(Long userId);

    Boolean makeUserAdmin(Long userId);

    Boolean add_reset_code(Long userId, String code);
}
