package com.example.BugByte_backend.services;
import java.security.SecureRandom;


public class RegistrationCOR {
    SecureRandom RANDOM = new SecureRandom();

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

    //function to generate reset password code
    String generateCode(){
        String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder(8);
        for (int i = 0; i < 8; i++) {
            int randomIndex = RANDOM.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(randomIndex));
        }
        return sb.toString();
    }

    public String generateRandomPassword() {
        String upperCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";
        String specialCharacters = "!@#$%^&*()-_=+[]{}|;:,.<>?";
        String allCharacters = upperCaseLetters + lowerCaseLetters + numbers + specialCharacters;
        StringBuilder password = new StringBuilder();
        password.append(specialCharacters.charAt(RANDOM.nextInt(specialCharacters.length())));
        for (int i = 1; i < 8; i++) {
            password.append(allCharacters.charAt(RANDOM.nextInt(allCharacters.length())));
        }
        char[] passwordArray = password.toString().toCharArray();
        for (int i = 0; i < passwordArray.length; i++) {
            int randomIndex = RANDOM.nextInt(passwordArray.length);
            char temp = passwordArray[i];
            passwordArray[i] = passwordArray[randomIndex];
            passwordArray[randomIndex] = temp;
        }
        return new String(passwordArray);
    }
}
