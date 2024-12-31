package com.example.BugByte_backend.Controllers;

import com.example.BugByte_backend.controllers.NotificationController;
import com.example.BugByte_backend.models.Notification;
import com.example.BugByte_backend.services.AuthenticationService;
import com.example.BugByte_backend.services.NotificationService.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NotificationControllerTests {

    @Mock
    private NotificationService notificationService;

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private NotificationController notificationController;

    private static final String VALID_TOKEN = "Bearer valid-token";
    private static final String INVALID_TOKEN = "Bearer invalid-token";
    private static final Long USER_ID = 123L;

    @BeforeEach
    void setUp() {
        // No setup needed as we're using @InjectMocks
    }

    @Test
    void fetchNotifications_Success() throws Exception {
        // Arrange
        List<Notification> expectedNotifications = Arrays.asList(
                new Notification(),
                new Notification()
        );
        when(notificationService.fetchNotifications(VALID_TOKEN))
                .thenReturn(expectedNotifications);

        // Act
        ResponseEntity<?> response = notificationController.fetchNotifications(VALID_TOKEN);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedNotifications, response.getBody());
        verify(notificationService).fetchNotifications(VALID_TOKEN);
    }

    @Test
    void fetchNotifications_UnauthorizedAccess() throws Exception {
        // Arrange
        when(notificationService.fetchNotifications(INVALID_TOKEN))
                .thenThrow(new RuntimeException("Unauthorized"));

        // Act
        ResponseEntity<?> response = notificationController.fetchNotifications(INVALID_TOKEN);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        @SuppressWarnings("unchecked")
        Map<String, String> responseBody = (Map<String, String>) response.getBody();
        assertNotNull(responseBody);
        assertEquals("Action Unauthorized", responseBody.get("message"));
        verify(notificationService).fetchNotifications(INVALID_TOKEN);
    }

    @Test
    void clearCache_Success() {
        // Arrange
        String token = VALID_TOKEN.replace("Bearer ", "");
        when(authenticationService.getIdFromJwt(token)).thenReturn(USER_ID);

        // Act
        ResponseEntity<?> response = notificationController.clearKafka(VALID_TOKEN);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        @SuppressWarnings("unchecked")
        Map<String, String> responseBody = (Map<String, String>) response.getBody();
        assertNotNull(responseBody);
        assertEquals("ok", responseBody.get("message"));
        verify(notificationService).clearCache(USER_ID.toString());
        verify(authenticationService).getIdFromJwt(token);
    }

    @Test
    void clearCache_WithEmptyToken() {
        // Arrange
        String emptyToken = "Bearer ";

        // Act
        ResponseEntity<?> response = notificationController.clearKafka(emptyToken);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(authenticationService).getIdFromJwt("");
    }

    @Test
    void clearCache_WithNullToken() {
        // Act
        ResponseEntity<?> response = notificationController.clearKafka(null);

        // Assert
        assertEquals(HttpStatus.BAD_GATEWAY, response.getStatusCode());
        @SuppressWarnings("unchecked")
        Map<String, String> responseBody = (Map<String, String>) response.getBody();
        assertNotNull(responseBody);
        assertEquals("operation failed", responseBody.get("message"));
    }

    @Test
    void fetchNotifications_WithNullToken() {
        // Act
        ResponseEntity<?> response = notificationController.fetchNotifications(null);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        @SuppressWarnings("unchecked")
        List<Notification> responseBody = (List<Notification>) response.getBody();
        assertNotNull(responseBody);
        assertTrue(responseBody.isEmpty());
    }
}