package com.example.BugByte_backend.controllers;

import com.example.BugByte_backend.facades.AdministrativeFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private AdministrativeFacade administrativeFacade;

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(@RequestHeader("Authorization") String token, @RequestParam("username") String username) {
        System.out.println("HERE");
        token = token.replace("Bearer ", "");

        Map<String, Object> userData = new HashMap<>();
        userData.put("jwt", token);
        userData.put("userName", username);

        try {
            Map<String, Object> response = administrativeFacade.getProfile(userData);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @PutMapping("/update-bio")
    public ResponseEntity<?> updateProfile(@RequestHeader("Authorization") String token, @RequestBody Map<String, Object> userData) {
        token = token.replace("Bearer ", "");
        userData.put("jwt", token);

        try {
            administrativeFacade.updateProfile(userData);
            return new ResponseEntity<>(Map.of("message", "Bio updated successfully"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @PutMapping("/follow")
    public ResponseEntity<?> followUser(@RequestHeader("Authorization") String token, @RequestParam("username") String username) {
        token = token.replace("Bearer ", "");

        Map<String, Object> userData = new HashMap<>();
        userData.put("jwt", token);
        userData.put("user-name", username);

        try {
            boolean res = administrativeFacade.followUser(userData);

            if (res)
                return new ResponseEntity<>("User followed successfully.", HttpStatus.OK);
            else
                return new ResponseEntity<>("Failed to follow the user.", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @DeleteMapping("/unfollow")
    public ResponseEntity<?> unFollowUser(@RequestHeader("Authorization") String token, @RequestParam("username") String username) {
        token = token.replace("Bearer ", "");

        Map<String, Object> userData = new HashMap<>();
        userData.put("jwt", token);
        userData.put("user-name", username);

        try {
            boolean res = administrativeFacade.unfollowUser(userData);

            if (res)
                return new ResponseEntity<>("User unfollowed successfully.", HttpStatus.OK);
            else
                return new ResponseEntity<>("Failed to unfollow the user.", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/following")
    public ResponseEntity<?> getFollowings(@RequestHeader("Authorization") String token, @RequestParam("username") String username) {
        token = token.replace("Bearer ", "");

        Map<String, Object> userData = new HashMap<>();
        userData.put("jwt", token);
        userData.put("user-name", username);

        try {
            List<Map<String, Object>> res = administrativeFacade.getFollowings(userData);
            return new ResponseEntity<>(res, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/followers")
    public ResponseEntity<?> getFollowers(@RequestHeader("Authorization") String token, @RequestParam("username") String username) {
        token = token.replace("Bearer ", "");

        Map<String, Object> userData = new HashMap<>();
        userData.put("jwt", token);
        userData.put("user-name", username);

        try {
            List<Map<String, Object>> res = administrativeFacade.getFollowers(userData);
            return new ResponseEntity<>(res, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @PutMapping("/make-admin")
    public ResponseEntity<?> makeAdmin(@RequestHeader("Authorization") String token, @RequestParam("username") String username) {
        token = token.replace("Bearer ", "");

        Map<String, Object> userData = new HashMap<>();
        userData.put("jwt", token);
        userData.put("user-name", username);

        try {
            boolean res = administrativeFacade.makeAdmin(userData);

            if (res)
                return new ResponseEntity<>("User promoted to admin successfully.", HttpStatus.OK);
            else
                return new ResponseEntity<>("Failed to promote user to admin.", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
}
