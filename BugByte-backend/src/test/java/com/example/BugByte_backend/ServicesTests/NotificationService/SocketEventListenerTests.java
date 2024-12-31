package com.example.BugByte_backend.ServicesTests.NotificationService;

import com.example.BugByte_backend.services.NotificationService.ConsumerManager;
import com.example.BugByte_backend.services.NotificationService.SocketEventListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class SocketEventListenerTests {

    @Mock
    private ConsumerManager consumerManager;

    @InjectMocks
    private SocketEventListener socketEventListener;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testHandleWebSocketConnectListener() {
        String userId = "123";
        String sessionId = "session-456";

        // Create headers to simulate a WebSocket message
        Map<String, Object> headers = new HashMap<>();
        headers.put(StompHeaderAccessor.NATIVE_HEADERS, Collections.singletonMap("userId", Collections.singletonList(userId)));
        headers.put(StompHeaderAccessor.SESSION_ID_HEADER, sessionId);

        Message<byte[]> message = new GenericMessage<>(new byte[0], headers);
        SessionConnectEvent event = new SessionConnectEvent(this, message);

        // Invoke the method
        socketEventListener.handleWebSocketConnectListener(event);

        // Validate sessionUserMap and ConsumerManager interactions
        assertEquals(userId, socketEventListener.getSessionUserMap().get(sessionId));
        verify(consumerManager, times(1)).startConsumer(userId);
    }

    @Test
    void testHandleWebSocketDisconnectListener() {
        String userId = "123";
        String sessionId = "session-456";

        // Pre-populate sessionUserMap
        socketEventListener.getSessionUserMap().put(sessionId, userId);

        // Create headers to simulate a WebSocket message
        Map<String, Object> headers = new HashMap<>();
        headers.put(StompHeaderAccessor.SESSION_ID_HEADER, sessionId);

        Message<byte[]> message = new GenericMessage<>(new byte[0], headers);
        SessionDisconnectEvent event = mock(SessionDisconnectEvent.class);
        when(event.getMessage()).thenReturn(message);

        // Invoke the method
        socketEventListener.handleWebSocketDisconnectListener(event);

        // Validate sessionUserMap and ConsumerManager interactions
        assertTrue(socketEventListener.getSessionUserMap().isEmpty());
        verify(consumerManager, times(1)).stopConsumer(userId);
    }

    @Test
    void testHandleWebSocketDisconnectListenerNonExistentSession() {
        String sessionId = "session-456";

        // Create headers to simulate a WebSocket message
        Map<String, Object> headers = new HashMap<>();
        headers.put(StompHeaderAccessor.SESSION_ID_HEADER, sessionId);

        Message<byte[]> message = new GenericMessage<>(new byte[0], headers);
        SessionDisconnectEvent event = mock(SessionDisconnectEvent.class);
        when(event.getMessage()).thenReturn(message);

        // Invoke the method
        socketEventListener.handleWebSocketDisconnectListener(event);

        // Validate that stopConsumer was never called (because session does not exist)
        verify(consumerManager, never()).stopConsumer(any());
    }

}
