package com.example.BugByte_backend.facades;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import com.example.BugByte_backend.models.Answer;
import com.example.BugByte_backend.models.Question;
import com.example.BugByte_backend.models.Reply;
import com.example.BugByte_backend.services.AuthenticationService;
import com.example.BugByte_backend.services.NotificationService.NotificationProducer;
import com.example.BugByte_backend.services.PostingService;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

public class InteractionFacadeTest {

    @InjectMocks
    InteractionFacade interactionFacade;

    @Mock
    PostingService postingService;

    @Mock
    AuthenticationService authenticationService;

    @Mock
    NotificationProducer notifier;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testPostQuestion_Exception() throws Exception {
        // Arrange
        Map<String, Object> postData = new HashMap<>();
        postData.put("jwt", "mock.Jwt.Token");
        postData.put("communityId", 1);
        postData.put("mdContent", "hello");
        postData.put("title" , "hi");
        List<String> tags = new ArrayList<>();
        postData.put("tags" , tags);
        when(authenticationService.getUserNameFromJwt((String) postData.get("jwt"))).thenReturn("user1");
        when(postingService.postQuestion(any())).thenReturn(2L);
        Map<String, Object> res = interactionFacade.postQuestion(postData);
        assertEquals(res.get("questionId") , 2L);
    }
    @Test
    void testPostQuestionThrowsException() {
        // Arrange
        Map<String, Object> postData = new HashMap<>();
        postData.put("jwt", "invalid_jwt_token");
        postData.put("communityId", 1);
        postData.put("mdContent", "This is the question content");
        postData.put("title", "Test Question");
        postData.put("tags", Arrays.asList("tag1", "tag2"));

        String exceptionMessage = "Invalid JWT token";

        // Mock the behavior to throw an exception
        when(authenticationService.getUserNameFromJwt("invalid_jwt_token")).thenThrow(new RuntimeException(exceptionMessage));

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> interactionFacade.postQuestion(postData));

        // Assert exception message
        assertEquals(exceptionMessage, exception.getMessage());


    }
    @Test
    public void testPostAnswer_Success() throws Exception {
        // Arrange
        Map<String, Object> postData = new HashMap<>();
        postData.put("jwt", "validToken");
        postData.put("questionId", 123);
        postData.put("mdContent", "Sample answer content");

        String expectedUserName = "user1";
        long expectedAnswerId = 200L;

        when(authenticationService.getUserNameFromJwt("validToken")).thenReturn(expectedUserName);
        when(postingService.postAnswer(any(Answer.class))).thenReturn(expectedAnswerId);
        doNothing().when(notifier).sendAnswerNotification(any(Answer.class));

        // Act
        Map<String, Object> result = interactionFacade.postAnswer(postData);

        // Assert
        assertNotNull(result);
        assertEquals(expectedAnswerId, result.get("answerId"));

    }

    @Test
    void testPostAnswerThrowsException() {
        // Arrange
        Map<String, Object> postData = new HashMap<>();
        postData.put("jwt", "invalid_jwt_token");
        postData.put("questionId", 1);
        postData.put("mdContent", "This is the answer content");

        String exceptionMessage = "Invalid JWT token";

        // Mock the behavior to throw an exception
        when(authenticationService.getUserNameFromJwt("invalid_jwt_token")).thenThrow(new RuntimeException(exceptionMessage));

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> interactionFacade.postAnswer(postData));

        // Assert exception message
        assertEquals(exceptionMessage, exception.getMessage());
    }
    @Test
    public void testPostQuestion_Success() throws Exception {
        // Arrange
        Map<String, Object> postData = new HashMap<>();
        postData.put("jwt", "validToken");
        postData.put("communityId", 123);
        postData.put("mdContent", "Sample content");
        postData.put("title", "Sample title");
        postData.put("tags", List.of("tag1", "tag2"));

        String expectedUserName = "user1";
        long expectedQuestionId = 100L;

        when(authenticationService.getUserNameFromJwt("validToken")).thenReturn(expectedUserName);
        when(postingService.postQuestion(any(Question.class))).thenReturn(expectedQuestionId);

        // Act
        Map<String, Object> result = interactionFacade.postQuestion(postData);

        // Assert
        assertNotNull(result);
        assertEquals(expectedQuestionId, result.get("questionId"));
    }
    @Test
    void testPostReplySuccess() throws Exception {
        // Arrange
        Map<String, Object> postData = new HashMap<>();
        postData.put("jwt", "valid_jwt_token");
        postData.put("answerId", 1);
        postData.put("mdContent", "This is the reply content");

        String mockUserName = "testUser";
        long mockReplyId = 12345L;

        // Mock the behavior of dependencies
        when(authenticationService.getUserNameFromJwt("valid_jwt_token")).thenReturn(mockUserName);
        when(postingService.postReply(any(Reply.class))).thenReturn(mockReplyId);

        // Act
        Map<String, Object> result = interactionFacade.postReply(postData);

        // Assert
        assertNotNull(result);
        assertEquals(mockReplyId, result.get("replyId"));
    }
    @Test
    void testDeleteQuestionSuccess() throws Exception {
        // Arrange
        Map<String, Object> postData = new HashMap<>();
        postData.put("jwt", "valid_jwt_token");
        postData.put("questionId", 1);

        String mockUserName = "testUser";
        long mockQuestionId = 12345L;

        // Mock the behavior of dependencies
        when(authenticationService.getUserNameFromJwt("valid_jwt_token")).thenReturn(mockUserName);
        when(postingService.deleteQuestion(anyLong(), anyString())).thenReturn(true);

        // Act
        boolean result = interactionFacade.deleteQuestion(postData);

        // Assert
        assertTrue(result);
    }

    @Test
    void testDeleteQuestionThrowsException() {
        // Arrange
        Map<String, Object> postData = new HashMap<>();
        postData.put("jwt", "invalid_jwt_token");
        postData.put("questionId", 1);

        String exceptionMessage = "Invalid JWT token";

        // Mock the behavior to throw an exception
        when(authenticationService.getUserNameFromJwt("invalid_jwt_token")).thenThrow(new RuntimeException(exceptionMessage));

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> interactionFacade.deleteQuestion(postData));

        // Assert exception message
        assertEquals(exceptionMessage, exception.getMessage());
    }
    @Test
    void testDeleteAnswerSuccess() throws Exception {
        // Arrange
        Map<String, Object> postData = new HashMap<>();
        postData.put("jwt", "valid_jwt_token");
        postData.put("answerId", 1);

        String mockUserName = "testUser";
        long mockAnswerId = 12345L;

        // Mock the behavior of dependencies
        when(authenticationService.getUserNameFromJwt("valid_jwt_token")).thenReturn(mockUserName);
        when(postingService.deleteAnswer(anyLong(), anyString())).thenReturn(true);

        // Act
        boolean result = interactionFacade.deleteAnswer(postData);

        // Assert
        assertTrue(result);
    }

    @Test
    void testDeleteAnswerThrowsException() {
        // Arrange
        Map<String, Object> postData = new HashMap<>();
        postData.put("jwt", "invalid_jwt_token");
        postData.put("answerId", 1);

        String exceptionMessage = "Invalid JWT token";

        // Mock the behavior to throw an exception
        when(authenticationService.getUserNameFromJwt("invalid_jwt_token")).thenThrow(new RuntimeException(exceptionMessage));

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> interactionFacade.deleteAnswer(postData));

        // Assert exception message
        assertEquals(exceptionMessage, exception.getMessage());
    }
    @Test
    void testDeleteReplySuccess() throws Exception {
        // Arrange
        Map<String, Object> postData = new HashMap<>();
        postData.put("jwt", "valid_jwt_token");
        postData.put("replyId", 1);

        String mockUserName = "testUser";
        long mockReplyId = 12345L;

        // Mock the behavior of dependencies
        when(authenticationService.getUserNameFromJwt("valid_jwt_token")).thenReturn(mockUserName);
        when(postingService.deleteReply(anyLong(), anyString())).thenReturn(true);

        // Act
        boolean result = interactionFacade.deleteReply(postData);

        // Assert
        assertTrue(result);
    }

    @Test
    void testDeleteReplyThrowsException() {
        // Arrange
        Map<String, Object> postData = new HashMap<>();
        postData.put("jwt", "invalid_jwt_token");
        postData.put("replyId", 1);

        String exceptionMessage = "Invalid JWT token";

        // Mock the behavior to throw an exception
        when(authenticationService.getUserNameFromJwt("invalid_jwt_token")).thenThrow(new RuntimeException(exceptionMessage));

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> interactionFacade.deleteReply(postData));

        // Assert exception message
        assertEquals(exceptionMessage, exception.getMessage());
    }
    @Test
    void testUpVoteQuestionSuccess() throws Exception {
        // Arrange
        Map<String, Object> postData = new HashMap<>();
        postData.put("jwt", "valid_jwt_token");
        postData.put("questionId", 1);

        String mockUserName = "testUser";
        long mockQuestionId = 12345L;

        // Mock the behavior of dependencies
        when(authenticationService.getUserNameFromJwt("valid_jwt_token")).thenReturn(mockUserName);
        when(postingService.upVoteQuestion(anyLong(), anyString())).thenReturn(true);

        // Act
        boolean result = interactionFacade.upVoteQuestion(postData);

        // Assert
        assertTrue(result);
    }

    @Test
    void testUpVoteQuestionThrowsException() {
        // Arrange
        Map<String, Object> postData = new HashMap<>();
        postData.put("jwt", "invalid_jwt_token");
        postData.put("questionId", 1);

        String exceptionMessage = "Invalid JWT token";

        // Mock the behavior to throw an exception
        when(authenticationService.getUserNameFromJwt("invalid_jwt_token")).thenThrow(new RuntimeException(exceptionMessage));

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> interactionFacade.upVoteQuestion(postData));

        // Assert exception message
        assertEquals(exceptionMessage, exception.getMessage());
    }
    @Test
    void testRemoveUpVoteQuestionSuccess() throws Exception {
        // Arrange
        Map<String, Object> postData = new HashMap<>();
        postData.put("jwt", "valid_jwt_token");
        postData.put("questionId", 1);

        String mockUserName = "testUser";
        long mockQuestionId = 12345L;

        // Mock the behavior of dependencies
        when(authenticationService.getUserNameFromJwt("valid_jwt_token")).thenReturn(mockUserName);
        when(postingService.removeUpVoteFromQuestion(anyLong(), anyString())).thenReturn(true);

        // Act
        boolean result = interactionFacade.removeUpVoteQuestion(postData);

        // Assert
        assertTrue(result);
    }

    @Test
    void testRemoveUpVoteQuestionThrowsException() {
        // Arrange
        Map<String, Object> postData = new HashMap<>();
        postData.put("jwt", "invalid_jwt_token");
        postData.put("questionId", 1);

        String exceptionMessage = "Invalid JWT token";

        // Mock the behavior to throw an exception
        when(authenticationService.getUserNameFromJwt("invalid_jwt_token")).thenThrow(new RuntimeException(exceptionMessage));

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> interactionFacade.removeUpVoteQuestion(postData));

        // Assert exception message
        assertEquals(exceptionMessage, exception.getMessage());
    }

    @Test
    void testPostReplyThrowsException() {
        // Arrange
        Map<String, Object> postData = new HashMap<>();
        postData.put("jwt", "invalid_jwt_token");
        postData.put("answerId", 1);
        postData.put("mdContent", "This is the reply content");

        String exceptionMessage = "Invalid JWT token";

        // Mock the behavior to throw an exception
        when(authenticationService.getUserNameFromJwt("invalid_jwt_token")).thenThrow(new RuntimeException(exceptionMessage));

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> interactionFacade.postReply(postData));

        // Assert exception message
        assertEquals(exceptionMessage, exception.getMessage());
    }
    @Test
    void testDownVoteQuestionSuccess() throws Exception {
        // Arrange
        Map<String, Object> postData = new HashMap<>();
        postData.put("jwt", "valid_jwt_token");
        postData.put("questionId", 1);

        String mockUserName = "testUser";
        long mockQuestionId = 12345L;
        Claims mockClaims = Mockito.mock(Claims.class);
        when(mockClaims.getSubject()).thenReturn(mockUserName);

        // Mock the behavior of dependencies
        when(authenticationService.getUserNameFromJwt("valid_jwt_token")).thenReturn(mockUserName);
        when(postingService.downVoteQuestion(anyLong(), anyString())).thenReturn(true);

        // Act
        boolean result = interactionFacade.downVoteQuestion(postData);

        // Assert
        assertTrue(result);
    }

    @Test
    void testDownVoteQuestionThrowsException() {
        // Arrange
        Map<String, Object> postData = new HashMap<>();
        postData.put("jwt", "invalid_jwt_token");
        postData.put("questionId", 1);

        String exceptionMessage = "Invalid JWT token";

        // Mock the behavior to throw an exception
        when(authenticationService.getUserNameFromJwt("invalid_jwt_token")).thenThrow(new RuntimeException(exceptionMessage));

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> interactionFacade.downVoteQuestion(postData));

        // Assert exception message
        assertEquals(exceptionMessage, exception.getMessage());
    }
    @Test
    void testRemoveDownVoteQuestionSuccess() throws Exception {
        // Arrange
        Map<String, Object> postData = new HashMap<>();
        postData.put("jwt", "valid_jwt_token");
        postData.put("questionId", 1);

        String mockUserName = "testUser";
        long mockQuestionId = 12345L;

        // Mock the behavior of dependencies
        when(authenticationService.getUserNameFromJwt("valid_jwt_token")).thenReturn(mockUserName);
        when(postingService.removeDownVoteFromQuestion(anyLong(), anyString())).thenReturn(true);

        // Act
        boolean result = interactionFacade.removeDownVoteQuestion(postData);

        // Assert
        assertTrue(result);
    }

    @Test
    void testRemoveDownVoteQuestionThrowsException() {
        // Arrange
        Map<String, Object> postData = new HashMap<>();
        postData.put("jwt", "invalid_jwt_token");
        postData.put("questionId", 1);

        String exceptionMessage = "Invalid JWT token";

        // Mock the behavior to throw an exception
        when(authenticationService.getUserNameFromJwt("invalid_jwt_token")).thenThrow(new RuntimeException(exceptionMessage));

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> interactionFacade.removeDownVoteQuestion(postData));

        // Assert exception message
        assertEquals(exceptionMessage, exception.getMessage());
    }
    public boolean upVoteAnswer(Map<String , Object> postData) throws Exception {
        try {
            String token = (String) postData.get("jwt");
            String userName = authenticationService.getUserNameFromJwt(token);
            Long answerId = Long.valueOf((Integer) postData.get("answerId"));
            return postingService.upVoteAnswer(answerId , userName);
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    @Test
    void testRemoveUpVoteAnswerSuccess() throws Exception {
        // Arrange
        Map<String, Object> postData = new HashMap<>();
        postData.put("jwt", "valid_jwt_token");
        postData.put("answerId", 1);

        String mockUserName = "testUser";
        long mockAnswerId = 12345L;

        // Mock the behavior of dependencies
        when(authenticationService.getUserNameFromJwt("valid_jwt_token")).thenReturn(mockUserName);
        when(postingService.removeUpVoteFromAnswer(anyLong(), anyString())).thenReturn(true);

        // Act
        boolean result = interactionFacade.removeUpVoteAnswer(postData);

        // Assert
        assertTrue(result);
    }

    @Test
    void testRemoveUpVoteAnswerThrowsException() {
        // Arrange
        Map<String, Object> postData = new HashMap<>();
        postData.put("jwt", "invalid_jwt_token");
        postData.put("answerId", 1);

        String exceptionMessage = "Invalid JWT token";

        // Mock the behavior to throw an exception
        when(authenticationService.getUserNameFromJwt("invalid_jwt_token")).thenThrow(new RuntimeException(exceptionMessage));

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> interactionFacade.removeUpVoteAnswer(postData));

        // Assert exception message
        assertEquals(exceptionMessage, exception.getMessage());
    }
    @Test
    void testDownVoteAnswerSuccess() throws Exception {
        // Arrange
        Map<String, Object> postData = new HashMap<>();
        postData.put("jwt", "valid_jwt_token");
        postData.put("answerId", 1);

        String mockUserName = "testUser";
        long mockAnswerId = 12345L;

        // Mock the behavior of dependencies
        when(authenticationService.getUserNameFromJwt("valid_jwt_token")).thenReturn(mockUserName);
        when(postingService.downVoteAnswer(anyLong(), anyString())).thenReturn(true);

        // Act
        boolean result = interactionFacade.downVoteAnswer(postData);

        // Assert
        assertTrue(result);
    }

    @Test
    void testDownVoteAnswerThrowsException() {
        // Arrange
        Map<String, Object> postData = new HashMap<>();
        postData.put("jwt", "invalid_jwt_token");
        postData.put("answerId", 1);

        String exceptionMessage = "Invalid JWT token";

        // Mock the behavior to throw an exception
        when(authenticationService.getUserNameFromJwt("invalid_jwt_token")).thenThrow(new RuntimeException(exceptionMessage));

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> interactionFacade.downVoteAnswer(postData));

        // Assert exception message
        assertEquals(exceptionMessage, exception.getMessage());
    }
    @Test
    void testRemoveDownVoteAnswerSuccess() throws Exception {
        // Arrange
        Map<String, Object> postData = new HashMap<>();
        postData.put("jwt", "valid_jwt_token");
        postData.put("answerId", 1);

        String mockUserName = "testUser";
        long mockAnswerId = 12345L;

        // Mock the behavior of dependencies
        when(authenticationService.getUserNameFromJwt("valid_jwt_token")).thenReturn(mockUserName);
        when(postingService.removeDownVoteAnswer(anyLong(), anyString())).thenReturn(true);

        // Act
        boolean result = interactionFacade.removeDownVoteAnswer(postData);

        // Assert
        assertTrue(result);
    }

    @Test
    void testRemoveDownVoteAnswerThrowsException() {
        // Arrange
        Map<String, Object> postData = new HashMap<>();
        postData.put("jwt", "invalid_jwt_token");
        postData.put("answerId", 1);

        String exceptionMessage = "Invalid JWT token";

        // Mock the behavior to throw an exception
        when(authenticationService.getUserNameFromJwt("invalid_jwt_token")).thenThrow(new RuntimeException(exceptionMessage));

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> interactionFacade.removeDownVoteAnswer(postData));

        // Assert exception message
        assertEquals(exceptionMessage, exception.getMessage());
    }
    @Test
    void testVerifyAnswerSuccess() throws Exception {
        // Arrange
        Map<String, Object> postData = new HashMap<>();
        postData.put("jwt", "valid_jwt_token");
        postData.put("answerId", 1);

        String mockUserName = "testUser";
        long mockAnswerId = 12345L;

        // Mock the behavior of dependencies
        when(authenticationService.getUserNameFromJwt("valid_jwt_token")).thenReturn(mockUserName);
        when(postingService.verifyAnswer(anyLong(), anyString())).thenReturn(true);

        // Act
        boolean result = interactionFacade.verifyAnswer(postData);

        // Assert
        assertTrue(result);
    }

    @Test
    void testVerifyAnswerThrowsException() {
        // Arrange
        Map<String, Object> postData = new HashMap<>();
        postData.put("jwt", "invalid_jwt_token");
        postData.put("answerId", 1);

        String exceptionMessage = "Invalid JWT token";

        // Mock the behavior to throw an exception
        when(authenticationService.getUserNameFromJwt("invalid_jwt_token")).thenThrow(new RuntimeException(exceptionMessage));

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> interactionFacade.verifyAnswer(postData));

        // Assert exception message
        assertEquals(exceptionMessage, exception.getMessage());
    }
    @Test
    void testEditPostSuccess() throws Exception {
        // Arrange
        Map<String, Object> postData = new HashMap<>();
        postData.put("postId", 1);
        postData.put("mdContent", "Updated content");

        long mockPostId = 12345L;
        String mockContent = "Updated content";

        // Mock the behavior of the postingService
        when(postingService.editPost(anyLong(), anyString())).thenReturn(true);

        // Act
        boolean result = interactionFacade.editPost(postData);

        // Assert
        assertTrue(result);
    }

    @Test
    void testEditPostThrowsException() throws Exception {
        // Arrange
        Map<String, Object> postData = new HashMap<>();
        postData.put("postId", 1);
        postData.put("mdContent", "Updated content");

        String exceptionMessage = "Unable to edit post";

        // Mock the behavior to throw an exception
        when(postingService.editPost(anyLong(), anyString())).thenThrow(new RuntimeException(exceptionMessage));

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> interactionFacade.editPost(postData));

        // Assert exception message
        assertEquals(exceptionMessage, exception.getMessage());
    }
    @Test
    public void testUpVoteAnswer_Success() throws Exception {
        // Arrange
        Map<String, Object> postData = new HashMap<>();
        postData.put("jwt", "validToken");
        postData.put("answerId", 123);

        String expectedUserName = "user1";
        Long expectedAnswerId = 123L;

        when(authenticationService.getUserNameFromJwt("validToken")).thenReturn(expectedUserName);
        when(postingService.upVoteAnswer(expectedAnswerId, expectedUserName)).thenReturn(true);

        // Act
        boolean result = interactionFacade.upVoteAnswer(postData);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testUpVoteAnswer_InvalidToken() {
        // Arrange
        Map<String, Object> postData = new HashMap<>();
        postData.put("jwt", "invalidToken");
        postData.put("answerId", 123);

        when(authenticationService.getUserNameFromJwt("invalidToken")).thenThrow(new RuntimeException("Invalid token"));

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> {
            interactionFacade.upVoteAnswer(postData);
        });

    }

    @Test
    public void testUpVoteAnswer_PostingServiceThrowsException() throws Exception {
        // Arrange
        Map<String, Object> postData = new HashMap<>();
        postData.put("jwt", "validToken");
        postData.put("answerId", 123);

        String expectedUserName = "user1";
        Long expectedAnswerId = 123L;

        when(authenticationService.getUserNameFromJwt("validToken")).thenReturn(expectedUserName);
        when(postingService.upVoteAnswer(expectedAnswerId, expectedUserName)).thenThrow(new RuntimeException("Error upvoting answer"));

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> {
            interactionFacade.upVoteAnswer(postData);
        });

        assertEquals("Error upvoting answer", exception.getMessage());
    }
}

