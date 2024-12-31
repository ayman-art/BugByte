package com.example.BugByte_backend.controllers;

import com.example.BugByte_backend.services.AuthenticationService;
import com.example.BugByte_backend.services.NotificationService.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.RecursiveTask;

@RestController
@RequestMapping("/notifications")
public class NotificationController {
    @Autowired
    NotificationService notificationService;
    @Autowired
    AuthenticationService authenticationService;
    @GetMapping("/fetch-notifications")
    public ResponseEntity<?> fetchNotifications(@RequestHeader("Authorization") String token){
        try{
            return new ResponseEntity<>(notificationService.fetchNotifications(token), HttpStatus.OK);
        }catch(Exception e){
            System.out.println(e.getMessage());
            return new ResponseEntity<>(Map.of("message", "Action Unauthorized"), HttpStatus.UNAUTHORIZED);
        }

    }
    @GetMapping("/clear")
    public ResponseEntity<?> clearKafka(@RequestHeader("Authorization") String token){
        try {
            notificationService.clearCache(
                    String.valueOf(authenticationService.getIdFromJwt(
                            token.replace("Bearer ", ""))));
            return new ResponseEntity<>(Map.of("message", "ok"), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(Map.of("message", "operation failed"), HttpStatus.BAD_GATEWAY);
        }
    }

}
