//package com.example.BugByte_backend.services.NotificationService;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.messaging.handler.annotation.Header;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.stereotype.Service;
//
//@Service
//public class NotificationConsumer {
//    private final SimpMessagingTemplate messagingTemplate;  // For sending messages to WebSocket
//
//    public NotificationConsumer(SimpMessagingTemplate messagingTemplate) {
//        this.messagingTemplate = messagingTemplate;
//    }
//
//    /**
//     * Consumes Kafka messages when a user is subscribed.
//     * @param payload Kafka message payload.
//     * @param topic The Kafka topic.
//     * @throws Exception If parsing the message fails.
//     */
//    @KafkaListener(topics = "notifications", groupId = "notification_group")
//    public void consume(String payload, @Header("kafka_receivedTopic") String topic) throws Exception {
//        // Extract userId from topic name (notifications_<userId>)
//        String userId = topic.replace("notifications_", "");
//
//        messagingTemplate.convertAndSend("/topic/notifications/" + userId, payload);
//    }
//}
