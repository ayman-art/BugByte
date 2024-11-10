package com.example.BugByte_backend.repositories;


import com.example.BugByte_backend.models.User;

public class UserRepository {
    public long insertUser(String userName , String email,String password){
        return 0;
    }

    public User findByIdentityAndPassword(String identity , String password){
        return new User("user1" , "user@gmail.com" , "12345678");
    }

    public int getCountByEmail(String Email){
        return 0;
    }

    public int getCountByUsername(String uaerName){
        return 0;
    }

    public User findById(long userId){
        return new User("user1" , "user@gmail.com" , "12345678");
    }

    public Boolean changePassword(long userId , String newPassword){
        return true;
    }

    public Boolean deleteUser(long userId){
        return true;
    }

}
