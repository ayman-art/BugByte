package com.example.BugByte_backend.facades;

import com.example.BugByte_backend.Adapters.AnswerAdapter;
import com.example.BugByte_backend.Adapters.QuestionAdapter;
import com.example.BugByte_backend.Adapters.ReplyAdapter;
import com.example.BugByte_backend.models.Answer;
import com.example.BugByte_backend.models.Question;
import com.example.BugByte_backend.models.Reply;
import com.example.BugByte_backend.services.PostingService;
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
    void testPostQuestion_Success() throws Exception {
        Map<String, Object> postData = new HashMap<>();
        postData.put("opName", "user1");
        postData.put("communityId", 101L);
        postData.put("mdContent", "Sample question content");

        long mockQuestionId = 1L;
        when(postingServiceMock.postQuestion(any(Question.class))).thenReturn(mockQuestionId);

        Map<String, Object> result = interactionFacade.postQuestion(postData);

        assertNotNull(result);
        assertEquals(mockQuestionId, result.get("questionId"));
    }

    @Test
    void testPostQuestion_Failure() throws Exception {
        Map<String, Object> postData = new HashMap<>();
        postData.put("opName", "user1");
        postData.put("communityId", 101L);
        postData.put("mdContent", "Sample question content");

        when(postingServiceMock.postQuestion(any(Question.class)))
                .thenThrow(new RuntimeException("Failed to post question"));

        Exception exception = assertThrows(Exception.class, () -> interactionFacade.postQuestion(postData));
        assertEquals("Failed to post question", exception.getMessage());
    }
    @Test
    void testPostAnswer_Success() throws Exception {
        Map<String, Object> postData = new HashMap<>();
        postData.put("opName", "user1");
        postData.put("questionId", 101L);
        postData.put("mdContent", "Sample answer content");

        long mockAnswerId = 1L;
        when(postingServiceMock.postAnswer(any(Answer.class))).thenReturn(mockAnswerId);

        Map<String, Object> result = interactionFacade.postAnswer(postData);

        assertNotNull(result);
        assertEquals(mockAnswerId, result.get("answerId"));
    }

    @Test
    void testPostAnswer_Failure() throws Exception {
        Map<String, Object> postData = new HashMap<>();
        postData.put("opName", "user1");
        postData.put("questionId", 101L);
        postData.put("mdContent", "Sample answer content");

        when(postingServiceMock.postAnswer(any(Answer.class)))
                .thenThrow(new RuntimeException("Failed to post answer"));

        Exception exception = assertThrows(Exception.class, () -> interactionFacade.postAnswer(postData));
        assertEquals("Failed to post answer", exception.getMessage());
    }
    @Test
    void testPostReply_Success() throws Exception {
        Map<String, Object> postData = new HashMap<>();
        postData.put("opName", "user1");
        postData.put("answerId", 101L);
        postData.put("mdContent", "Sample reply content");

        long mockReplyId = 1L;
        when(postingServiceMock.postReply(any(Reply.class))).thenReturn(mockReplyId);

        Map<String, Object> result = interactionFacade.postReply(postData);

        assertNotNull(result);
        assertEquals(mockReplyId, result.get("replyId"));
    }

    @Test
    void testPostReply_Failure() throws Exception {
        Map<String, Object> postData = new HashMap<>();
        postData.put("opName", "user1");
        postData.put("answerId", 101L);
        postData.put("mdContent", "Sample reply content");

        when(postingServiceMock.postReply(any(Reply.class)))
                .thenThrow(new RuntimeException("Failed to post reply"));

        Exception exception = assertThrows(Exception.class, () -> interactionFacade.postReply(postData));
        assertEquals("Failed to post reply", exception.getMessage());
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
    void testUpVoteQuestion_Success() throws Exception {
        Map<String, Object> postData = new HashMap<>();
        postData.put("questionId", 101L);

        when(postingServiceMock.upVoteQuestion(anyLong())).thenReturn(true);

        boolean result = interactionFacade.upVoteQuestion(postData);

        assertTrue(result);
    }

    @Test
    void testUpVoteQuestion_Failure() throws Exception {
        Map<String, Object> postData = new HashMap<>();
        postData.put("questionId", 101L);

        when(postingServiceMock.upVoteQuestion(anyLong())).thenReturn(false);

        boolean result = false;
        try {
            result = interactionFacade.upVoteQuestion(postData);
        } catch (Exception e) {
            fail("Exception should not have been thrown");
        }

        assertFalse(result);
    }
    @Test
    void testRemoveUpVoteQuestion_Success() throws Exception {
        Map<String, Object> postData = new HashMap<>();
        postData.put("questionId", 101L);

        when(postingServiceMock.removeUpVoteFromQuestion(anyLong())).thenReturn(true);

        boolean result = interactionFacade.removeUpVoteQuestion(postData);

        assertTrue(result);
    }

    @Test
    void testRemoveUpVoteQuestion_Failure() throws Exception {
        Map<String, Object> postData = new HashMap<>();
        postData.put("questionId", 101L);

        when(postingServiceMock.removeUpVoteFromQuestion(anyLong())).thenReturn(false);

        boolean result = false;
        try {
            result = interactionFacade.removeUpVoteQuestion(postData);
        } catch (Exception e) {
            fail("Exception should not have been thrown");
        }

        assertFalse(result);
    }
    @Test
    void testDownVoteQuestion_Success() throws Exception {
        Map<String, Object> postData = new HashMap<>();
        postData.put("questionId", 101L);

        when(postingServiceMock.downVoteQuestion(anyLong())).thenReturn(true);

        boolean result = interactionFacade.downVoteQuestion(postData);

        assertTrue(result);
    }

    @Test
    void testDownVoteQuestion_Failure() throws Exception {
        Map<String, Object> postData = new HashMap<>();
        postData.put("questionId", 101L);

        when(postingServiceMock.downVoteQuestion(anyLong())).thenReturn(false);

        boolean result = false;
        try {
            result = interactionFacade.downVoteQuestion(postData);
        } catch (Exception e) {
            fail("Exception should not have been thrown");
        }

        assertFalse(result);
    }
    @Test
    void testRemoveDownVoteQuestion_Success() throws Exception {
        Map<String, Object> postData = new HashMap<>();
        postData.put("questionId", 101L);

        when(postingServiceMock.removeDownVoteFromQuestion(anyLong())).thenReturn(true);

        boolean result = interactionFacade.removeDownVoteQuestion(postData);

        assertTrue(result);
    }

    @Test
    void testRemoveDownVoteQuestion_Failure() throws Exception {
        Map<String, Object> postData = new HashMap<>();
        postData.put("questionId", 101L);

        when(postingServiceMock.removeDownVoteFromQuestion(anyLong())).thenReturn(false);

        boolean result = false;
        try {
            result = interactionFacade.removeDownVoteQuestion(postData);
        } catch (Exception e) {
            fail("Exception should not have been thrown");
        }

        assertFalse(result);
    }
    @Test
    void testUpVoteAnswer_Success() throws Exception {
        Map<String, Object> postData = new HashMap<>();
        postData.put("answerId", 201L);

        when(postingServiceMock.upVoteAnswer(anyLong())).thenReturn(true);

        boolean result = interactionFacade.upVoteAnswer(postData);

        assertTrue(result);
    }

    @Test
    void testUpVoteAnswer_Failure() throws Exception {
        Map<String, Object> postData = new HashMap<>();
        postData.put("answerId", 201L);

        when(postingServiceMock.upVoteAnswer(anyLong())).thenReturn(false);

        boolean result = false;
        try {
            result = interactionFacade.upVoteAnswer(postData);
        } catch (Exception e) {
            fail("Exception should not have been thrown");
        }

        assertFalse(result);
    }
    @Test
    void testRemoveUpVoteAnswer_Success() throws Exception {
        Map<String, Object> postData = new HashMap<>();
        postData.put("answerId", 202L);

        when(postingServiceMock.removeUpFromVoteAnswer(anyLong())).thenReturn(true);

        boolean result = interactionFacade.removeUpVoteAnswer(postData);

        assertTrue(result);
    }

    @Test
    void testRemoveUpVoteAnswer_Failure() throws Exception {
        Map<String, Object> postData = new HashMap<>();
        postData.put("answerId", 202L);

        when(postingServiceMock.removeUpFromVoteAnswer(anyLong())).thenReturn(false);

        boolean result = false;
        try {
            result = interactionFacade.removeUpVoteAnswer(postData);
        } catch (Exception e) {
            fail("Exception should not have been thrown");
        }

        assertFalse(result);
    }

    @Test
    void testDownVoteAnswer_Success() throws Exception {
        Map<String, Object> postData = new HashMap<>();
        postData.put("answerId", 303L);

        when(postingServiceMock.downVoteAnswer(anyLong())).thenReturn(true);

        boolean result = interactionFacade.downVoteAnswer(postData);

        assertTrue(result);
    }

    @Test
    void testDownVoteAnswer_Failure() throws Exception {
        Map<String, Object> postData = new HashMap<>();
        postData.put("answerId", 303L);

        when(postingServiceMock.downVoteAnswer(anyLong())).thenReturn(false);

        boolean result = false;
        try {
            result = interactionFacade.downVoteAnswer(postData);
        } catch (Exception e) {
            fail("Exception should not have been thrown");
        }

        assertFalse(result);
    }
    @Test
    void testRemoveDownVoteAnswer_Success() throws Exception {
        Map<String, Object> postData = new HashMap<>();
        postData.put("answerId", 404L);

        when(postingServiceMock.removeDownVoteAnswer(anyLong())).thenReturn(true);

        boolean result = interactionFacade.removeDownVoteAnswer(postData);

        assertTrue(result);
    }

    @Test
    void testRemoveDownVoteAnswer_Failure() throws Exception {
        Map<String, Object> postData = new HashMap<>();
        postData.put("answerId", 404L);

        when(postingServiceMock.removeDownVoteAnswer(anyLong())).thenReturn(false);

        boolean result = false;
        try {
            result = interactionFacade.removeDownVoteAnswer(postData);
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
