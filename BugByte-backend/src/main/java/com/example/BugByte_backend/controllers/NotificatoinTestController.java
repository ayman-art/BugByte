package com.example.BugByte_backend.controllers;

import com.example.BugByte_backend.services.NotificationService.NotificationProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notifications")
public class NotificatoinTestController {



    @Autowired
    private NotificationProducer producer;

    @PostMapping("/{userId}")
    public String sendNotification(@PathVariable String userId, @RequestBody String message) {
        producer.sendNotification(message, Long.valueOf(userId), "");

        return "Notification sent to user " + userId;
    }
}
