package com.example.BugByte_backend.ServicesTests.NotificationService;

import com.example.BugByte_backend.models.Notification;
import com.example.BugByte_backend.services.AuthenticationService;
import com.example.BugByte_backend.services.NotificationService.NotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceTests {

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private ListOperations<String, Object> listOperations;

    private NotificationService notificationService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        notificationService = new NotificationService(messagingTemplate);
        notificationService.setRedisTemplate(redisTemplate);
        notificationService.setAuthenticationService(authenticationService);
    }

    @Test
    void notifyUser_ShouldSendNotificationToCorrectTopic() {
        // Arrange
        Long userId = 123L;
        String payload = "Test notification";
        String expectedDestination = "/topic/notifications/" + userId;

        // Act
        notificationService.notifyUser(payload, userId);

        // Assert
        verify(messagingTemplate).convertAndSend(expectedDestination, payload);
    }

    @Test
    void fetchNotifications_ShouldReturnNotificationsFromCache() throws Exception {
        // Arrange
        String token = "Bearer jwt-token";
        Long userId = 123L;
        Notification notification1 = new Notification(); // Set necessary fields
        Notification notification2 = new Notification(); // Set necessary fields

        String notificationJson1 = objectMapper.writeValueAsString(notification1);
        String notificationJson2 = objectMapper.writeValueAsString(notification2);

        when(redisTemplate.opsForList()).thenReturn(listOperations);
        when(authenticationService.getIdFromJwt("jwt-token")).thenReturn(userId);
        when(listOperations.size("notifications:" + userId)).thenReturn(2L);
        when(listOperations.range(eq("notifications:" + userId), eq(0L), eq(1L)))
                .thenReturn(Arrays.asList(notificationJson1, notificationJson2));

        // Act
        List<Notification> result = notificationService.fetchNotifications(token);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(authenticationService).getIdFromJwt("jwt-token");
    }

    @Test
    void fetchNotifications_ShouldReturnEmptyListWhenNoCachedNotifications() throws Exception {
        // Arrange
        String token = "Bearer jwt-token";
        Long userId = 123L;

        when(redisTemplate.opsForList()).thenReturn(listOperations);
        when(authenticationService.getIdFromJwt("jwt-token")).thenReturn(userId);
        when(listOperations.size("notifications:" + userId)).thenReturn(0L);

        // Act
        List<Notification> result = notificationService.fetchNotifications(token);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void cacheNotification_ShouldStoreNotificationInRedis() {
        // Arrange
        Long userId = 123L;
        String notification = "Test notification";
        String key = "notifications:" + userId;

        when(redisTemplate.opsForList()).thenReturn(listOperations);

        // Act
        notificationService.cacheNotification(userId, notification);

        // Assert
        verify(listOperations).rightPush(key, notification);
        verify(redisTemplate).expire(key, 1, TimeUnit.DAYS);
    }

    @Test
    void clearCache_ShouldDeleteNotificationsFromRedis() {
        // Arrange
        String userId = "123";
        String key = "notifications:" + userId;

        // Act
        notificationService.clearCache(userId);

        // Assert
        verify(redisTemplate).delete(key);
    }

    @Test
    void fetchNotifications_ShouldHandleInvalidJson() throws Exception {
        // Arrange
        String token = "Bearer jwt-token";
        Long userId = 123L;
        String invalidJson = "invalid-json";

        when(redisTemplate.opsForList()).thenReturn(listOperations);
        when(authenticationService.getIdFromJwt("jwt-token")).thenReturn(userId);
        when(listOperations.size("notifications:" + userId)).thenReturn(1L);
        when(listOperations.range(eq("notifications:" + userId), eq(0L), eq(0L)))
                .thenReturn(List.of(invalidJson));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            notificationService.fetchNotifications(token);
        });
    }
}