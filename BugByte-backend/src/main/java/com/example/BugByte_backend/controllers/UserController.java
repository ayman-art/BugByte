package com.example.BugByte_backend.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    // Auto wiring the administrative facade

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(@RequestHeader("Authorization") String token, @RequestParam("userid") Long userid) {
        token = token.replace("Bearer ", "");
        Map<String, Object> userData = new HashMap<>();
        userData.put("jwt", token);
        userData.put("userid", userid);
        try {
            // Administrative facade calling getProfile method
            return null;
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @PutMapping("/update-profile")
    public ResponseEntity<?> updateProfile(@RequestHeader("Authorization") String token, @RequestBody Map<String, Object> userData) {
        token = token.replace("Bearer ", "");
        userData.put("jwt", token);
        try {
            // Administrative facade calling getProfile method
            return null;
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }



}
