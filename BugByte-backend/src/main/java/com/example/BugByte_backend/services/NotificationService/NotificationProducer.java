package com.example.BugByte_backend.services.NotificationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class NotificationProducer {
    private static final String TOPIC = "notifications_";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendNotification(String message, Long userId, String link) {
        String date = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        String customTopic = TOPIC+String.valueOf(userId);
        String notificationPayload = String.format("{\"message\": \"%s\", \"date\": \"%s\", \"link\": \"%s\"}",
                message, date, link);
        kafkaTemplate.send(customTopic, notificationPayload);
    }
}
