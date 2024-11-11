package com.example.BugByte_backend.services;

import com.example.BugByte_backend.models.User;
import com.example.BugByte_backend.repositories.UserRepositoryImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {

    @Autowired
    private UserRepositoryImp userRepository;

    public long registerUser(String email , String userName , String password) throws Exception{
        RegistrationCOR registrationCOR = new RegistrationCOR();
        try {
             if(!registrationCOR.validateEmail(email)){
                 throw new Exception("email is not valid");
             }
             if(!registrationCOR.validateUserName(userName)){
                 throw new Exception("userName is too short");
             }
             if(!registrationCOR.validatePassword(password)){
                 throw new Exception("week password");
             }
            //insert user in the database
            return userRepository.insertUser(userName , email , password);
        }
        catch (Exception e){
            throw new Exception("Error registering user , user Already exists: " + e.getMessage());
        }
    }

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
            throw new Exception("Error occurred while logging in user: " + e.getMessage());
        }
    }


    public User logoutUser(long userId) throws Exception{
        try {
            //check if the user exists in the database
            User user = userRepository.findById(userId);
            if (user == null) {
                throw new Exception("User doesn't exist");
            }
                return user;
        }
        catch (Exception e){
            throw new Exception("Error occurred while logging out user:  " + e.getMessage());
        }
    }


    public boolean deleteUser(long userId) throws Exception{
        try {
            //check if the user exists in the database
            User user = userRepository.findById(userId);
            if (user == null) {
                throw new Exception("User doesn't exist");
            }
                //delete the user
                return userRepository.deleteUser(userId);
        }
        catch (Exception e){
            throw new Exception("Error occurred while deleting user:  " + e.getMessage());
        }
    }


    public boolean changePassword(long userId , String newPassword) throws Exception{
        try {
            //check if the user exists in the database
            User user = userRepository.findById(userId);
            if (user == null) {
                throw new Exception("User doesn't exist");
            }
                //change the password
                return userRepository.changePassword(userId , newPassword);
        }
        catch (Exception e){
            throw new Exception("Error occurred while changing password:  " + e.getMessage());
        }
    }
}
