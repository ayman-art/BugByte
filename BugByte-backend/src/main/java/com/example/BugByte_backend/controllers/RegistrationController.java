package com.example.BugByte_backend.controllers;

import com.example.BugByte_backend.facades.AdminstrativeFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/users")
public class RegistrationController {

    @Autowired
    private AdminstrativeFacade adminstrativeFacade;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody Map<String, Object> userdata) {
        try {
            System.out.println("registerUser from RegistrationController");
            Map<String, Object> response = adminstrativeFacade.registerUser(userdata);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}



