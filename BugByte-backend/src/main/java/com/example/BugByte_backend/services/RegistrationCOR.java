package com.example.BugByte_backend.services;

public class RegistrationCOR {

    //check if the email is valid
    public boolean validateEmail(String email){
        return email.contains("@") && email.endsWith(".com") &&
                !email.startsWith("@") && email.indexOf("@") == email.lastIndexOf("@");
    }

    //check if the username is valid
    public boolean validateUserName(String userName){
        return userName.length() > 3;
    }

    //check if the password is valid
    public  boolean validatePassword(String password){
        return password.length() > 8 && password.matches(".*[!@#$%^&*(),.?\":{}|<>].*");
    }

}
