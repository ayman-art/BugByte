package com.example.BugByte_backend.services;

import com.example.BugByte_backend.models.User;
import com.example.BugByte_backend.repositories.UserRepository;
import com.example.BugByte_backend.repositories.UserRepositoryImp;

public class RegistrationService {

    private final UserRepositoryImp userRepository;

    public RegistrationService(UserRepositoryImp userRepository) {
        this.userRepository = userRepository;
    }

    //register new user
    public long registerUser(String email , String userName , String password) throws Exception{
        try {
            //insert user in the database
            return userRepository.insertUser(userName , email , password);
        }
        catch (Exception e){
            throw new Exception("Error registering user , user Already exists: " + e.getMessage());
        }
    }

    //login user
    public User loginUser(String identity , String password) throws Exception{
        try {

            //get the user from the database
                User user = userRepository.findByIdentityAndPassword(identity , password);
                if(user == null){
                    throw new NullPointerException("User doesn't exist");
                }
                return user;
        }
        catch (Exception e){
            throw new Exception("Error occurred while logging in user , user doesn't exist: " + e.getMessage());
        }
    }

    //logout user
    public User logoutUser(long userId) throws Exception{
        try {
            //check if the user exists in the database
            User user = userRepository.findById(userId);
            if (user == null) {
                throw new Exception("User doesn't exist");
            }
            else {
                return user;
            }
        }
        catch (Exception e){
            throw new Exception("Error occurred while logging out user:  " + e.getMessage());
        }
    }

    //delete user
    public boolean deleteUser(long userId) throws Exception{
        try {
            //check if the user exists in the database
            User user = userRepository.findById(userId);
            if (user == null) {
                throw new Exception("User doesn't exist");
            }
            else {
                //delete the user
                return userRepository.deleteUser(userId);
            }
        }
        catch (Exception e){
            throw new Exception("Error occurred while deleting user:  " + e.getMessage());
        }
    }

    //change password
    public boolean changePassword(long userId , String newPassword) throws Exception{
        try {
            //check if the user exists in the database
            User user = userRepository.findById(userId);
            if (user == null) {
                throw new Exception("User doesn't exist");
            }
            else {
                //change the password
                return userRepository.changePassword(userId , newPassword);
            }
        }
        catch (Exception e){
            throw new Exception("Error occurred while changing password:  " + e.getMessage());
        }
    }
}
