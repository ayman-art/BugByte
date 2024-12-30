package com.example.BugByte_backend.services.NotificationService;
import com.example.BugByte_backend.models.Notification;
import com.example.BugByte_backend.services.AuthenticationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    public NotificationService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void notifyUser(String notificationPayload, Long userId) {
        messagingTemplate.convertAndSend("/topic/notifications/"+userId, notificationPayload);
    }
    public List<Notification> fetchNotifications(String token) throws Exception{
        String jwt = token.replace("Bearer ", "");
        Long id = authenticationService.getIdFromJwt(jwt);
        String[] jsons = getCachedNotificationsStrings(id);
        ObjectMapper objectMapper = new ObjectMapper();

        List<Notification> notifications = Arrays.stream(jsons).map(string -> {
            try {
                return objectMapper.readValue(string, Notification.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }).toList();
        return notifications;
    }

    private String[] getCachedNotificationsStrings(Long userId){
        String key = "notifications:" + userId;
        Long size = redisTemplate.opsForList().size(key);
        if (size == null || size == 0) {
            return new String[0];
        }
        return redisTemplate.opsForList().range(key, 0, size - 1).toArray(new String[0]);
    }
    public void cacheNotification(Long userId, String notification) {
        String key = "notifications:" + userId;
        redisTemplate.opsForList().rightPush(key, notification);
        redisTemplate.expire(key, 1, TimeUnit.DAYS); // Set expiration to 1 day
    }

    public void clearCache(String userId) {
        String key = "notifications:" + userId;
        redisTemplate.delete(key);
    }


}
