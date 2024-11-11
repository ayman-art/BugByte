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
            //check if the user already exists in the database
            int userByEmail = userRepository.getCountByEmail(email);
            int userByUserName = userRepository.getCountByUsername(userName);
            if (userByEmail > 0 || userByUserName > 0) {
                throw new Exception("UserName or Email already exists");
            }
            else {
                //insert new user in the database
                return userRepository.insertUser(userName , email , password);
            }
        }
        catch (Exception e){
            throw new Exception("Error registering user: " + e.getMessage());
        }
    }

    //login user
    public User loginUser(String identity , String password) throws Exception{
        try {
            //check if the user exists in the database
            int userByEmail = userRepository.getCountByEmail(identity);
            int userByUserName = userRepository.getCountByUsername(identity);
            if (userByEmail == 0 && userByUserName == 0) {
                throw new Exception("User doesn't exist");
            }
            else {
                //get the user from the database
                return userRepository.findByIdentityAndPassword(identity , password);
            }
        }
        catch (Exception e){
            throw new Exception("Error occurred while logging in user: " + e.getMessage());
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
