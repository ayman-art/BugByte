package com.example.BugByte_backend.controllers;

import com.example.BugByte_backend.facades.AdministrativeFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/users")
public class RegistrationController {

    @Autowired
    private AdministrativeFacade administrativeFacade;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody Map<String, Object> userdata) {
        try {
            Map<String, Object> response = administrativeFacade.registerUser(userdata);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, Object> userdata) {
        try {
            String jwt = administrativeFacade.loginUser(userdata);
            Map<String, Object> response = Map.of("jwt", jwt);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @DeleteMapping("/delete-user")
    public ResponseEntity<?> deleteUser(@RequestHeader("Authorization") String token){
        token = token.replace("Bearer ", "");
        Map<String, Object> userdata = Map.of("jwt", token);
        try {
            administrativeFacade.deleteUser(userdata);
            return new ResponseEntity<>("User Deleted Successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, Object> userdata) {
        try {
            String email = administrativeFacade.resetPassword(userdata);
            Map<String, Object> response = Map.of("email", email);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/validate-email-code")
    public ResponseEntity<?> validateEmailCode(@RequestBody Map<String, Object> userdata) {
        try {
            String token = administrativeFacade.validateEmailedCode(userdata);
            Map<String, Object> response = Map.of("jwt", token);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody Map<String, Object> userdata, @RequestHeader("Authorization") String token) {
        token = token.replace("Bearer ", "");
        userdata.put("jwt", token);
        try {
            administrativeFacade.changePassword(userdata);
            return new ResponseEntity<>("Password Changed Successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
}



