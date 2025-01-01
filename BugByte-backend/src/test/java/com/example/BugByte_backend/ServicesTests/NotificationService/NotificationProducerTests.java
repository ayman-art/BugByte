package com.example.BugByte_backend.ServicesTests.NotificationService;

import com.example.BugByte_backend.models.Answer;
import com.example.BugByte_backend.models.Question;
import com.example.BugByte_backend.models.Reply;
import com.example.BugByte_backend.models.User;
import com.example.BugByte_backend.services.NotificationService.NotificationProducer;
import com.example.BugByte_backend.services.PostingService;
import com.example.BugByte_backend.services.UserService;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NotificationProducerTests {

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @Mock
    private PostingService postingService;

    @Mock
    private UserService userService;

    @InjectMocks
    private NotificationProducer notificationProducer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendNotification() throws JSONException {
        String message = "Test Message";
        Long userId = 123L;
        String link = "/test/link";

        notificationProducer.sendNotification(message, userId, link);

        ArgumentCaptor<String> topicCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> payloadCaptor = ArgumentCaptor.forClass(String.class);

        verify(kafkaTemplate, times(1)).send(topicCaptor.capture(), payloadCaptor.capture());

        String expectedTopic = "notifications_123";
        String actualPayload = payloadCaptor.getValue();

        // Parse the actual payload
        JSONObject payloadJson = new JSONObject(actualPayload);
        assertEquals(message, payloadJson.getString("message"));
        assertEquals(link, payloadJson.getString("link"));

        // Validate the timestamp separately
        String actualDate = payloadJson.getString("date");
        LocalDateTime parsedDate = LocalDateTime.parse(actualDate, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        assertNotNull(parsedDate); // Ensure date parsing succeeds

        assertEquals(expectedTopic, topicCaptor.getValue());
    }


    @Test
    void testSendAnswerNotification() throws Exception {
        Question question = new Question();
        question.setId(1L);
        question.setCreatorUserName("opUser");

        Answer answer = new Answer();
        answer.setQuestionId(1L);
        answer.setCreatorUserName("answerUser");

        User opUser = new User();
        opUser.setId(123L);

        when(postingService.getQuestion(1L)).thenReturn(question);
        when(userService.getUser("opUser")).thenReturn(opUser);

        notificationProducer.sendAnswerNotification(answer);

        verify(kafkaTemplate, times(1)).send(eq("notifications_123"), anyString());
    }

    @Test
    void testSendReplyNotification() throws Exception {
        Question question = new Question();
        question.setId(1L);

        Answer answer = new Answer();
        answer.setQuestionId(1L);
        answer.setCreatorUserName("answerUser");

        Reply reply = new Reply();
        reply.setAnswerId(1L);
        reply.setCreatorUserName("replyUser");

        User opUser = new User();
        opUser.setId(123L);

        when(postingService.getAnswer(1L)).thenReturn(answer);
        when(postingService.getQuestion(1L)).thenReturn(question);
        when(userService.getUser("answerUser")).thenReturn(opUser);

        notificationProducer.sendReplyNotification(reply);

        verify(kafkaTemplate, times(1)).send(eq("notifications_123"), anyString());
    }

    @Test
    void testSendFollowNotification() {
        User user = new User();
        user.setId(123L);

        when(userService.getUser("followedUser")).thenReturn(user);

        notificationProducer.sendFollowNotification("followerUser", "followedUser");

        verify(kafkaTemplate, times(1)).send(eq("notifications_123"), anyString());
    }
}
