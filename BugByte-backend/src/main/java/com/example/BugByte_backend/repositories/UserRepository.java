package com.example.BugByte_backend.repositories;


public class UserRepository {
    long insertUser(String userName , String email,String password){
        return 0;
    }

    long getCountByEmail(String Email){
        return 0;
    }

    long getCountByUsername(String uaerName){
        return 0;
    }

    Boolean changePassword(long userId , String newPassword){
        return true;
    }

    Boolean deleteUser(long userId){
        return true;
    }

}
