package com.example.BugByte_backend.repositories;

import com.example.BugByte_backend.models.User;

public interface UserRepository {
    Long insertUser(String userName, String email, String password);

    User findByIdentityAndPassword(String identity, String password);

    User findByIdentity(String identity);

    User findById(Long userId);

    Long findIdByEmail(String email);

    Boolean changePassword(Long userId, String newPassword);

    Boolean deleteUser(Long userId);

    Boolean makeUserAdmin(Long userId);

    Boolean addResetCode(Long userId, String code);

    Boolean deleteResetCode(String code);

    Boolean codeExists(String code);

    Boolean userExists(Long userId);

    String getCodeById(Long id);
}
