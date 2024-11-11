package com.example.BugByte_backend.repositories;

import com.example.BugByte_backend.models.UserModel;

public interface UserRepository {
    Integer insertUser(String userName, String email, String password);

    UserModel findByIdentityAndPassword(String identity, String password);

    Integer getCountByEmail(String email);

    Integer getCountByUsername(String userName);

    UserModel findById(Integer userId);

    Boolean changePassword(Integer userId, String newPassword);

    Boolean deleteUser(Integer userId);

    Boolean makeUserAdmin(Integer userId);
}
