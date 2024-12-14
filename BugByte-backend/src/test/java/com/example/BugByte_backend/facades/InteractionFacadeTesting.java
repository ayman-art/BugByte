package com.example.BugByte_backend.facades;

import com.example.BugByte_backend.Adapters.AnswerAdapter;
import com.example.BugByte_backend.Adapters.QuestionAdapter;
import com.example.BugByte_backend.Adapters.ReplyAdapter;
import com.example.BugByte_backend.models.Answer;
import com.example.BugByte_backend.models.Question;
import com.example.BugByte_backend.models.Reply;
import com.example.BugByte_backend.services.PostingService;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class InteractionFacadeTesting {
    @Mock
    PostingService postingServiceMock;

    @Mock
    QuestionAdapter questionAdapterMock;

    @Mock
    AnswerAdapter answerAdapterMock;

    @Mock
    ReplyAdapter replyAdapterMock;


    @InjectMocks
    InteractionFacade interactionFacade;
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testDeleteQuestion_Success() throws Exception {
        Map<String, Object> postData = new HashMap<>();
        postData.put("questionId", 101L);

        when(postingServiceMock.deleteQuestion(anyLong())).thenReturn(true);

        boolean result = interactionFacade.deleteQuestion(postData);

        assertTrue(result);
    }

    @Test
    void testDeleteQuestion_Failure() throws Exception {
        Map<String, Object> postData = new HashMap<>();
        postData.put("questionId", 101L);

        when(postingServiceMock.deleteQuestion(anyLong())).thenReturn(false);

        boolean result = false;
        try {
            result = interactionFacade.deleteQuestion(postData);
        } catch (Exception e) {
            fail("Exception should not have been thrown");
        }

        assertFalse(result);
    }
    @Test
    void testDeleteAnswer_Success() throws Exception {
        Map<String, Object> postData = new HashMap<>();
        postData.put("answerId", 101L);

        when(postingServiceMock.deleteAnswer(anyLong())).thenReturn(true);

        boolean result = interactionFacade.deleteAnswer(postData);

        assertTrue(result);
    }

    @Test
    void testDeleteAnswer_Failure() throws Exception {
        Map<String, Object> postData = new HashMap<>();
        postData.put("answerId", 101L);

        when(postingServiceMock.deleteAnswer(anyLong())).thenReturn(false);

        boolean result = false;
        try {
            result = interactionFacade.deleteAnswer(postData);
        } catch (Exception e) {
            fail("Exception should not have been thrown");
        }

        assertFalse(result);
    }

    @Test
    void testDeleteReply_Success() throws Exception {
        Map<String, Object> postData = new HashMap<>();
        postData.put("replyId", 101L);

        when(postingServiceMock.deleteReply(anyLong())).thenReturn(true);

        boolean result = interactionFacade.deleteReply(postData);

        assertTrue(result);
    }

    @Test
    void testDeleteReply_Failure() throws Exception {
        Map<String, Object> postData = new HashMap<>();
        postData.put("replyId", 101L);

        when(postingServiceMock.deleteReply(anyLong())).thenReturn(false);

        boolean result = false;
        try {
            result = interactionFacade.deleteReply(postData);
        } catch (Exception e) {
            fail("Exception should not have been thrown");
        }

        assertFalse(result);
    }
    @Test
    void testVerifyAnswer_Success() throws Exception {
        Map<String, Object> postData = new HashMap<>();
        postData.put("answerId", 505L);

        when(postingServiceMock.verifyAnswer(anyLong())).thenReturn(true);

        boolean result = interactionFacade.verifyAnswer(postData);

        assertTrue(result);
    }

    @Test
    void testVerifyAnswer_Failure() throws Exception {
        Map<String, Object> postData = new HashMap<>();
        postData.put("answerId", 505L);

        when(postingServiceMock.verifyAnswer(anyLong())).thenReturn(false);

        boolean result = false;
        try {
            result = interactionFacade.verifyAnswer(postData);
        } catch (Exception e) {
            fail("Exception should not have been thrown");
        }

        assertFalse(result);
    }
    @Test
    void testEditPost_Success() throws Exception {
        Map<String, Object> postData = new HashMap<>();
        postData.put("postId", 101L);
        postData.put("mdContent", "Updated content");

        when(postingServiceMock.editPost(anyLong(), anyString())).thenReturn(true);

        boolean result = interactionFacade.editPost(postData);

        assertTrue(result);
    }

    @Test
    void testEditPost_Failure() throws Exception {
        Map<String, Object> postData = new HashMap<>();
        postData.put("postId", 101L);
        postData.put("mdContent", "Updated content");

        when(postingServiceMock.editPost(anyLong(), anyString())).thenReturn(false);

        boolean result = false;
        try {
            result = interactionFacade.editPost(postData);
        } catch (Exception e) {
            fail("Exception should not have been thrown");
        }

        assertFalse(result);
    }
}
