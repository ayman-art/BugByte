package com.example.BugByte_backend.services.NotificationService;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class SocketEventListener {
    private final ConsumerManager consumerManager;
    private final ConcurrentHashMap<String, String> sessionUserMap = new ConcurrentHashMap<>(); // Maps sessionId to userId

    public SocketEventListener(ConsumerManager consumerManager) {
        this.consumerManager = consumerManager;
    }

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String userId = headerAccessor.getNativeHeader("userId").get(0);
        String sessionId = headerAccessor.getSessionId();
        assert sessionId != null;
        sessionUserMap.put(sessionId, userId);
        System.out.println("connected to ws");
        consumerManager.startConsumer(userId); // Start Kafka consumer
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();
        assert sessionId != null;
        String userId = sessionUserMap.get(sessionId);
        consumerManager.stopConsumer(userId); // Stop Kafka consumer
    }
}
