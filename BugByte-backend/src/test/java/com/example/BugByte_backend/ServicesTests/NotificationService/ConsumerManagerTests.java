package com.example.BugByte_backend.ServicesTests.NotificationService;


import com.example.BugByte_backend.services.NotificationService.ConsumerManager;
import com.example.BugByte_backend.services.NotificationService.NotificationService;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ConsumerManagerTests {

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private ConsumerManager consumerManager;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }


    @Test
    void testStopConsumer() {
        String userId = "123";

        KafkaConsumer<String, String> mockConsumer = mock(KafkaConsumer.class);
        ConcurrentHashMap<String, KafkaConsumer<String, String>> consumerMap = new ConcurrentHashMap<>();
        consumerMap.put(userId, mockConsumer);

        consumerManager = new ConsumerManager();
        consumerManager.consumerMap = consumerMap;

        consumerManager.stopConsumer(userId);

        // Verify the consumer was removed from the map
        assertNull(consumerMap.get(userId));

        // Verify the wakeup method was called on the consumer
        verify(mockConsumer, times(1)).wakeup();
    }

    @Test
    void testStopConsumerNonExistent() {
        String userId = "456";

        KafkaConsumer<String, String> mockConsumer = mock(KafkaConsumer.class);
        ConcurrentHashMap<String, KafkaConsumer<String, String>> consumerMap = new ConcurrentHashMap<>();

        consumerManager = new ConsumerManager();
        consumerManager.consumerMap = consumerMap;

        consumerManager.stopConsumer(userId);

        // Verify that nothing happens if the consumer doesn't exist
        verify(mockConsumer, never()).wakeup();
        assertTrue(consumerMap.isEmpty());
    }

    @Test
    void testMultipleConsumers() {
        String userId1 = "123";
        String userId2 = "456";

        KafkaConsumer<String, String> mockConsumer1 = mock(KafkaConsumer.class);
        KafkaConsumer<String, String> mockConsumer2 = mock(KafkaConsumer.class);

        ConcurrentHashMap<String, KafkaConsumer<String, String>> consumerMap = new ConcurrentHashMap<>();
        consumerMap.put(userId1, mockConsumer1);
        consumerMap.put(userId2, mockConsumer2);

        consumerManager = new ConsumerManager();
        consumerManager.consumerMap = consumerMap;

        consumerManager.stopConsumer(userId1);
        consumerManager.stopConsumer(userId2);

        // Verify both consumers were removed from the map
        assertNull(consumerMap.get(userId1));
        assertNull(consumerMap.get(userId2));

        // Verify wakeup was called on both consumers
        verify(mockConsumer1, times(1)).wakeup();
        verify(mockConsumer2, times(1)).wakeup();
    }
}
