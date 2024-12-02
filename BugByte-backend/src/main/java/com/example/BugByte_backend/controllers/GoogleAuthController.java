package com.example.BugByte_backend.controllers;

import com.example.BugByte_backend.facades.AdministrativeFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class GoogleAuthController {
    @Autowired
    AdministrativeFacade administrativeFacade;

    @PostMapping("/google")
    public ResponseEntity<?> verifyGoogleToken(@RequestBody Map<String, String> requestBody) {
            try {
                Map<String, Object> response = administrativeFacade.googleOAuthSignUp(requestBody);
                return new ResponseEntity<>(response, HttpStatus.CREATED);
            } catch (Exception e) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            }
    }
}

