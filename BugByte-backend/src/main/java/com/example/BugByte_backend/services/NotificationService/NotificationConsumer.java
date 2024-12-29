package com.example.BugByte_backend.services.NotificationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@Service
public class NotificationConsumer {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @KafkaListener(topics = "BugByte_notifications", groupId = "notification_group")
    public void consume(String message) {
        messagingTemplate.convertAndSend("/topic/notifications", message);
    }
}
