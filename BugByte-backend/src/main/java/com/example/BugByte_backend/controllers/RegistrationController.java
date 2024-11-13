package com.example.BugByte_backend.controllers;

import com.example.BugByte_backend.facades.AdminstrativeFacade;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/users")
public class RegistrationController {


    private AdminstrativeFacade adminstrativeFacade;

    public RegistrationController() {
        adminstrativeFacade = AdminstrativeFacade.getInstance();
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody Map<String, Object> userdata) {
        try {
            System.out.println("registerUser from RegistrationController");
            Map response = adminstrativeFacade.registerUser(userdata);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    static class test{
        public static void main(String[] args) {
            String endpoint = "http://localhost:8080/users/signup";
            Map<String, Object> userdata = Map.of(
                    "email", "aymanalgamal22@gmail.com",
                    "user_name", "ayman",
                    "password", "Ayman!@#Q!1"
            );
            ResponseEntity<?> response = new RegistrationController().registerUser(userdata);
            System.out.println(response);
        }
        }

}



