package com.example.BugByte_backend.services.NotificationService;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class ConsumerManager {
    private final ConcurrentHashMap<String, KafkaConsumer<String, String>> consumerMap = new ConcurrentHashMap<>();
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    @Autowired
    NotificationService notificationService;
    public void startConsumer(String userId) {
        executorService.submit(() -> {
            String topic = "notifications_" + userId;
            Properties properties = new Properties();
            properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092"); // Adjust to your Kafka broker
            properties.put(ConsumerConfig.GROUP_ID_CONFIG, "notification-group-" + userId);
            properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
            properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
            properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

            try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties)) {
                consumer.subscribe(Collections.singletonList(topic));
                consumerMap.put(userId, consumer);
                while (!Thread.currentThread().isInterrupted()) {
                    consumer.poll(Duration.ofMillis(100)).forEach(record -> {
                        // Process the message
                        System.out.println("Received message: " + record.value());
                        notificationService.notifyUser(record.value(), Long.valueOf(userId));
                        // Optionally forward this message to the connected WebSocket session
                    });
                }
            } catch (WakeupException e) {
                // Expected during consumer shutdown, handle gracefully
                System.out.println("Consumer for userId " + userId + " is shutting down.");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                stopConsumer(userId);
                System.out.println("Consumer for userId " + userId + " closed.");
            }
        });
    }

    public void stopConsumer(String userId) {
        KafkaConsumer<String, String> consumer = consumerMap.remove(userId);
        if (consumer != null) {
            consumer.wakeup(); // Stops the consumer thread
        }
    }
}
