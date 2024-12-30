package com.example.BugByte_backend.services.NotificationService;

import com.example.BugByte_backend.models.Answer;
import com.example.BugByte_backend.models.Question;
import com.example.BugByte_backend.models.Reply;
import com.example.BugByte_backend.models.User;
import com.example.BugByte_backend.services.PostingService;
import com.example.BugByte_backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class NotificationProducer {
    private static final String TOPIC = "notifications_";
    @Autowired
    PostingService postingService;
    @Autowired
    UserService userService;
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendNotification(String message, Long userId, String link) {
        String date = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        String customTopic = TOPIC+String.valueOf(userId);
        String notificationPayload = String.format("{\"message\": \"%s\", \"date\": \"%s\", \"link\": \"%s\"}",
                message, date, link);
        kafkaTemplate.send(customTopic, notificationPayload);
    }

    public void sendAnswerNotification(Answer answer){
        try{
            Question q = postingService.getQuestion(answer.getQuestionId());
            String opUserName = q.getCreatorUserName();
            User op = userService.getUser(opUserName);
            Long opId = op.getId();
            String answerOpUserName = answer.getCreatorUserName();
            String message = answerOpUserName + " has posted an answer to your question!";
            String link = "/posts/"+q.getId();
            sendNotification(message, opId, link);
        }catch (Exception e){
            System.out.println("Log: failed notification!"+ e.getMessage());
        }
    }
    public void sendReplyNotification(Reply reply){
        try{
            Long ansId = reply.getAnswerId();
            Answer answer = postingService.getAnswer(ansId);
            Question q = postingService.getQuestion(answer.getQuestionId());
            String opUserName = answer.getCreatorUserName();
            User op = userService.getUser(opUserName);
            Long opId = op.getId();
            String replyOpUserName = reply.getCreatorUserName();
            String message = replyOpUserName + " has replied to your question!";
            String link = "/posts/"+q.getId();
            sendNotification(message, opId, link);
        }catch (Exception e){
            System.out.println("Log: failed notification!"+ e.getMessage());
        }
    }
    public void sendFollowNotification(User user){

    }
}
