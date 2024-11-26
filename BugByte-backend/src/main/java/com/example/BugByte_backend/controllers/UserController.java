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
            // Administrative facade calling get profile method
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
            // Administrative facade calling update profile method
            return null;
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @PutMapping("/follow")
    public ResponseEntity<?> followUser(@RequestHeader("Authorization") String token, @RequestParam("userid") Long userid) {
        token = token.replace("Bearer ", "");
        Map<String, Object> userData = new HashMap<>();
        userData.put("jwt", token);
        userData.put("userid", userid);
        try {
            // Administrative facade calling follow method
            return null;
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @DeleteMapping("/unfollow")
    public ResponseEntity<?> unFollowUser(@RequestHeader("Authorization") String token, @RequestParam("userid") Long userid) {
        token = token.replace("Bearer ", "");
        Map<String, Object> userData = new HashMap<>();
        userData.put("jwt", token);
        userData.put("userid", userid);
        try {
            // Administrative facade calling unfollow method
            return null;
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/following")
    public ResponseEntity<?> getFollowings(@RequestHeader("Authorization") String token) {
        token = token.replace("Bearer ", "");
        Map<String, Object> userData = new HashMap<>();
        userData.put("jwt", token);
        try {
            // Administrative facade calling get followings method
            return null;
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @PutMapping("/make-admin")
    public ResponseEntity<?> makeAdmin(@RequestHeader("Authorization") String token, @RequestParam("userid") Long userid) {
        token = token.replace("Bearer ", "");
        Map<String, Object> userData = new HashMap<>();
        userData.put("jwt", token);
        userData.put("userid", userid);
        try {
            // Administrative facade calling make admin method
            return null;
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
}
