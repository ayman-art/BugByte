package com.example.BugByte_backend.facades;
import com.example.BugByte_backend.models.Answer;
import com.example.BugByte_backend.models.Question;
import com.example.BugByte_backend.models.Reply;
import com.example.BugByte_backend.services.AuthenticationService;
import com.example.BugByte_backend.services.PostingService;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InteractionFacadeTest {

    @Mock
    private PostingService postingService;

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private InteractionFacade interactionFacade;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void postQuestion_shouldReturnQuestionData_whenValidInput() throws Exception {
        // Arrange
        Map<String, Object> postData = new HashMap<>();
        postData.put("jwt", "valid-token");
        postData.put("communityId", 1);
        postData.put("mdContent", "Question content");
        postData.put("title", "Question title");
        postData.put("tags", List.of("tag1", "tag2"));

        Claims mockClaims = mock(Claims.class);
        when(mockClaims.getSubject()).thenReturn("test-user");
        when(AuthenticationService.parseToken("valid-token")).thenReturn(mockClaims);

        when(postingService.postQuestion(any(Question.class))).thenReturn(1L);

        // Act
        Map<String, Object> result = interactionFacade.postQuestion(postData);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.get("questionId"));
        verify(postingService, times(1)).postQuestion(any(Question.class));
    }

    @Test
    void postAnswer_shouldReturnAnswerData_whenValidInput() throws Exception {
        // Arrange
        Map<String, Object> postData = new HashMap<>();
        postData.put("jwt", "valid-token");
        postData.put("questionId", 1);
        postData.put("mdContent", "Answer content");

        Claims mockClaims = mock(Claims.class);
        when(mockClaims.getSubject()).thenReturn("test-user");
        when(AuthenticationService.parseToken("valid-token")).thenReturn(mockClaims);

        when(postingService.postAnswer(any(Answer.class))).thenReturn(2L);

        // Act
        Map<String, Object> result = interactionFacade.postAnswer(postData);

        // Assert
        assertNotNull(result);
        assertEquals(2L, result.get("answerId"));
        verify(postingService, times(1)).postAnswer(any(Answer.class));
    }

    @Test
    void postReply_shouldReturnReplyData_whenValidInput() throws Exception {
        // Arrange
        Map<String, Object> postData = new HashMap<>();
        postData.put("jwt", "valid-token");
        postData.put("answerId", 1);
        postData.put("mdContent", "Reply content");

        Claims mockClaims = mock(Claims.class);
        when(mockClaims.getSubject()).thenReturn("test-user");
        when(AuthenticationService.parseToken("valid-token")).thenReturn(mockClaims);

        when(postingService.postReply(any(Reply.class))).thenReturn(3L);

        // Act
        Map<String, Object> result = interactionFacade.postReply(postData);

        // Assert
        assertNotNull(result);
        assertEquals(3L, result.get("replyId"));
        verify(postingService, times(1)).postReply(any(Reply.class));
    }

    @Test
    void deleteQuestion_shouldReturnTrue_whenDeletionIsSuccessful() throws Exception {
        // Arrange
        Map<String, Object> postData = new HashMap<>();
        postData.put("jwt", "valid-token");
        postData.put("questionId", 1);

        Claims mockClaims = mock(Claims.class);
        when(mockClaims.getSubject()).thenReturn("test-user");
        when(AuthenticationService.parseToken("valid-token")).thenReturn(mockClaims);

        when(postingService.deleteQuestion(1L, "test-user")).thenReturn(true);

        // Act
        boolean result = interactionFacade.deleteQuestion(postData);

        // Assert
        assertTrue(result);
        verify(postingService, times(1)).deleteQuestion(1L, "test-user");
    }

    @Test
    void upVoteQuestion_shouldReturnTrue_whenVoteIsSuccessful() throws Exception {
        // Arrange
        Map<String, Object> postData = new HashMap<>();
        postData.put("jwt", "valid-token");
        postData.put("questionId", 1);

        Claims mockClaims = mock(Claims.class);
        when(mockClaims.getSubject()).thenReturn("test-user");
        when(AuthenticationService.parseToken("valid-token")).thenReturn(mockClaims);

        when(postingService.upVoteQuestion(1L, "test-user")).thenReturn(true);

        // Act
        boolean result = interactionFacade.upVoteQuestion(postData);

        // Assert
        assertTrue(result);
        verify(postingService, times(1)).upVoteQuestion(1L, "test-user");
    }

    @Test
    void getUserQuestions_shouldReturnQuestionList_whenValidInput() throws Exception {
        // Arrange
        Map<String, Object> userdata = new HashMap<>();
        userdata.put("jwt", "valid-token");
        userdata.put("limit", 10);
        userdata.put("offset", 0);

        Claims mockClaims = mock(Claims.class);
        when(mockClaims.getSubject()).thenReturn("test-user");
        when(AuthenticationService.parseToken("valid-token")).thenReturn(mockClaims);

        Question mockQuestion = new Question();
        mockQuestion.setId(1L);
        mockQuestion.setTitle("Sample Question");
        when(postingService.getUserQuestions("test-user", 10, 0))
                .thenReturn(List.of(mockQuestion));

        // Act
        List<Map<String, Object>> result = interactionFacade.getUserQuestions(userdata);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Sample Question", result.get(0).get("title"));
        verify(postingService, times(1)).getUserQuestions("test-user", 10, 0);
    }
}

