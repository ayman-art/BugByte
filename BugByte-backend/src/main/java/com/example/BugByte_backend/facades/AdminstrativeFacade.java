package com.example.BugByte_backend.facades;

import com.example.BugByte_backend.services.RegistrationService;
import com.example.BugByte_backend.services.UserService;

import java.util.Map;

public class AdminstrativeFacade {
    // Applying singleton design pattern
    private static AdminstrativeFacade instance = null;

    public static AdminstrativeFacade getInstance(){
        if(instance==null) instance = new AdminstrativeFacade();
        return instance;
    }
    // protected constructor to avoid using it outside the class
    protected AdminstrativeFacade(){}


    private static UserService userService = UserService.getInstance();
    private static RegistrationService registrationService = new RegistrationService();


    public static void registerUser(Map<String, Object> userdata){

    }
}
