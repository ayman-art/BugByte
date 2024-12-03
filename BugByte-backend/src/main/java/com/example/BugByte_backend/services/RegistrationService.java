package com.example.BugByte_backend.services;

import com.example.BugByte_backend.models.User;
import com.example.BugByte_backend.repositories.UserRepositoryImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {

    @Autowired
    private UserRepositoryImp userRepository;
    RegistrationCOR registrationCOR = new RegistrationCOR();

    EmailService emailService = new EmailService();

    public User registerUser(String email , String userName , String password) throws Exception {
        try {
             if(!registrationCOR.validateEmail(email)) {
                 throw new Exception("email is not valid");
             }
             if(!registrationCOR.validateUserName(userName)) {
                 throw new Exception("userName is too short");
             }
             if(!registrationCOR.validatePassword(password)) {
                 throw new Exception("weak password");
             }
            System.out.println("userName " + userName + "email " + email);
             //insert user in the database
             long id  = userRepository.insertUser(userName , email , password);
             return new User(id, userName, email, password);
        } catch (Exception e) {
            throw new Exception("Error registering user , user Already exists: " + e.getMessage());
        }
    }

    public User loginUser(String identity , String password) throws Exception {
        try {
            //get the user from the database
            User user = userRepository.findByIdentityAndPassword(identity , password);
            if(user == null){
                throw new NullPointerException("User doesn't exist");
            }
            return user;
        }
        catch (Exception e) {
            throw new Exception("Error occurred while logging in user: " + e.getMessage());
        }
    }

    public User logoutUser(long userId) throws Exception {
        try {
            //check if the user exists in the database
            User user = userRepository.findById(userId);
            if (user == null) {
                throw new Exception("User doesn't exist");
            }
            return user;
        }
        catch (Exception e) {
            throw new Exception("Error occurred while logging out user:  " + e.getMessage());
        }
    }

    public boolean deleteUser(long userId) throws Exception {
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

    public boolean changePassword(long userId , String newPassword) throws Exception {
        try {
            //check if the user exists in the database
            User user = userRepository.findById(userId);
            if (user == null) {
                throw new Exception("User doesn't exist");
            }
            //change the password
            if (!userRepository.changePassword(userId , newPassword)) throw new Exception("Invalid Operation");
            return true;
        } catch (Exception e){
            throw new Exception("Error occurred while changing password:  " + e.getMessage());
        }
    }

    public String sendResetPasswordCode(String identity) throws Exception {
        try {
            User user = userRepository.findByIdentity(identity);
            if (user == null) {
                throw new Exception("User doesn't exist");
            }

            String code = registrationCOR.generateCode();
            while (userRepository.codeExists(code)) {
                code = registrationCOR.generateCode();
            }
            userRepository.addResetCode(user.getId() , code);
            if(emailService.sendCodeByEmail(user.getEmail(), code)){
                return user.getEmail();
            } else{
                throw new Exception("Error occurred while sending code");
            }
        } catch (Exception e) {
            throw new Exception("Error occurred while sending code:  " + e.getMessage());
        }
    }

    public User validateCode(String email , String code) throws Exception {
        try{
            User user = userRepository.findByIdentity(email);
            if(user == null){
                throw new Exception("User doesn't exist");
            }
            String userCode = userRepository.getCodeById(user.getId());
            if(userCode.equals(code)){
                userRepository.deleteResetCode(code);
                return user;
            }
            throw new Exception("wrong code");
        } catch (Exception e){
            throw new Exception("Error occurred while validation  " + e.getMessage());
        }
    }
    public User registerUsingGoogle(String name , String email) throws Exception{
        try{
            User user = userRepository.findByIdentity(email);
            return user;
        } catch (EmptyResultDataAccessException e){
            String password = registrationCOR.generateRandomPassword();
            long id = userRepository.insertUser(name , email , password);
            return new User(id, name, email, password);
        }
        catch (Exception e){
            throw new Exception("Failed to register user " + e.getMessage());
        }
    }
}
