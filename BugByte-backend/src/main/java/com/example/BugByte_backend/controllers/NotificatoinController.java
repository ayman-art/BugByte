package com.example.BugByte_backend.controllers;

import com.example.BugByte_backend.services.NotificationService.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/notifications")
public class NotificatoinController {
    @Autowired
    NotificationService notificationService;

    @GetMapping("/fetch-notifications")
    ResponseEntity<?> fetchNotifications(@RequestHeader("Authorization") String token){
        try{
            return new ResponseEntity<>(notificationService.fetchNotifications(token), HttpStatus.OK);
        }catch(Exception e){
            System.out.println(e.getMessage());
            return new ResponseEntity<>(Map.of("message", "Action Unauthorized"), HttpStatus.UNAUTHORIZED);
        }

    }

}
