package com.example.BugByte_backend.Controllers;

import com.example.BugByte_backend.controllers.PostingController;
import com.example.BugByte_backend.facades.InteractionFacade;
import org.apache.kafka.common.protocol.types.Field;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class PostingControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Mock
    private InteractionFacade interactionFacade;

    @InjectMocks
    private PostingController postingController;

    @Test
    void testPostQuestion_Success() throws Exception {
        // Arrange
        String token = "Bearer testToken";
        Long communityId = 123L;
        Map<String, Object> question = new HashMap<>(
                Map.of("jwt", "testToken", "communityId", "123",
                        "title", "Test question", "mdContent", "Test content",
"tags", new String[]{"tag1", "tag2"})
        );

        Map<String, Object> expectedResponse = Map.of("questionId", "456");
        when(interactionFacade.postQuestion(any())).thenReturn(expectedResponse);

        // Act
        ResponseEntity<?> result = postingController.postQuestion(token, communityId, question);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(expectedResponse, result.getBody());
        verify(interactionFacade, times(1)).postQuestion(question);

    }

    @Test
    void testPostQuestion_Failure() throws Exception {
        // Arrange
        String token = "Bearer testToken";
        Long communityId = 123L;
        Map<String, Object> question = new HashMap<>(
                Map.of("jwt", "testToken", "communityId", "123",
                        "title", "Test question", "mdContent", "Test content",
                        "tags", new String[]{"tag1", "tag2"})
        );

        when(interactionFacade.postQuestion(any())).thenThrow(new Exception("Test exception"));

        // Act
        ResponseEntity<?> result = postingController.postQuestion(token, communityId, question);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        assertEquals("Test exception", result.getBody());
    }

    @Test
    void testPostAnswer_Success() throws Exception {
        // Arrange
        String token = "Bearer testToken";
        Long questionId = 123L;
        Map<String, Object> answer = new HashMap<>();
        answer.put("mdContent", "Test answer");

        Map<String, Object> expectedResponse = Map.of("answerId", "456");
        when(interactionFacade.postAnswer(any())).thenReturn(expectedResponse);

        // Act
        ResponseEntity<?> result = postingController.postAnswer(token, questionId, answer);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(expectedResponse, result.getBody());
        verify(interactionFacade, times(1)).postAnswer(answer);
    }


    @Test
    void testPostAnswer_Failure() throws Exception {
        // Arrange
        String token = "Bearer testToken";
        Long questionId = 123L;
        Map<String, Object> answer = new HashMap<>();
        answer.put("mdContent", "Test answer");

        Map<String, Object> expectedResponse = Map.of("answerId", "456");
        when(interactionFacade.postAnswer(any())).thenThrow(new Exception("Test exception"));

        // Act
        ResponseEntity<?> result = postingController.postAnswer(token, questionId, answer);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        assertEquals("Test exception", result.getBody());
    }

    @Test
    void testPostReply_Success() throws Exception {
        // Arrange
        String token = "Bearer testToken";
        Long answerId = 123L;
        Map<String, Object> reply = new HashMap<>();
        reply.put("mdContent", "Test reply");

        Map<String, Object> expectedResponse = Map.of("replyId", "456");
        when(interactionFacade.postReply(any())).thenReturn(expectedResponse);

        // Act
        ResponseEntity<?> result = postingController.postReply(token, answerId, reply);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(expectedResponse, result.getBody());
        verify(interactionFacade, times(1)).postReply(reply);
    }

    @Test
    void testPostReply_Failure() throws Exception {
        // Arrange
        String token = "Bearer testToken";
        Long answerId = 123L;
        Map<String, Object> reply = new HashMap<>();
        reply.put("mdContent", "Test reply");

        when(interactionFacade.postReply(any())).thenThrow(new Exception("Test exception"));

        // Act
        ResponseEntity<?> result = postingController.postReply(token, answerId, reply);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        assertEquals("Test exception", result.getBody());
    }


    @Test
    void getQuestionSuccess() throws Exception {
        String token = "Bearer validToken"; // Replace with a valid token
        Long questionId = 1L;

        mockMvc.perform(MockMvcRequestBuilders.get("/questions")
                        .header("Authorization", token)
                        .param("questionId", questionId.toString())
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$.status").value("OK"))
                .andExpect((ResultMatcher) jsonPath("$.data").isNotEmpty()); // Adjust as needed for the response structure
    }

    @Test
    void getQuestionFailure() throws Exception {
        String token = "Bearer invalidToken"; // Invalid token
        Long questionId = 1L;

        mockMvc.perform(MockMvcRequestBuilders.get("/questions")
                        .header("Authorization", token)
                        .param("questionId", questionId.toString())
                        .contentType("application/json"))
                .andExpect(status().isUnauthorized())
                .andExpect((ResultMatcher) jsonPath("$.message").value("Unauthorized"));
    }

    @Test
    void testGetQuestion_Failure() throws Exception {
        // Arrange
        String token = "Bearer testToken";
        Long questionId = 123L;
        Map<String, Object> request = new HashMap<>();
        request.put("jwt", token.replace("Bearer ", ""));
        request.put("questionId", questionId);
        when(interactionFacade.getQuestion(request)).thenThrow(new Exception("Test exception"));

        // Act
        ResponseEntity<?> result = postingController.getQuestion(token, questionId);

        // Assert
        verify(interactionFacade, times(1)).getQuestion(request);
        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        assertEquals("Test exception", result.getBody());
    }

    @Test
    void testGetAnswer() throws Exception {
        // Arrange
        String token = "Bearer testToken";
        Long answerId = 123L;
        Map<String, Object> request = new HashMap<>();
        request.put("jwt", token.replace("Bearer ", ""));
        request.put("answerId", answerId);
        Map<String, Object> expectedResponse = Map.of(
                "answerId", "123",
                "mdContent", "Test content",
                "questionId", "123"
        );
        when(interactionFacade.getAnswer(request)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<?> result = postingController.getAnswer(token, answerId);

        // Assert
        verify(interactionFacade, times(1)).getAnswer(request);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(expectedResponse, result.getBody());

    }

    @Test
    void testGetAnswer_Failure() throws Exception {
        // Arrange
        String token = "Bearer testToken";
        Long answerId = 123L;
        Map<String, Object> request = new HashMap<>();
        request.put("jwt", token.replace("Bearer ", ""));
        request.put("answerId", answerId);
        when(interactionFacade.getAnswer(request)).thenThrow(new Exception("Test exception"));

        // Act
        ResponseEntity<?> result = postingController.getAnswer(token, answerId);

        // Assert
        verify(interactionFacade, times(1)).getAnswer(request);
        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        assertEquals("Test exception", result.getBody());
    }

    @Test
    void testGetReply_Success() throws Exception {
        // Arrange
        String token = "Bearer testToken";
        Long replyId = 123L;
        Map<String, Object> request = new HashMap<>();
        request.put("jwt", token.replace("Bearer ", ""));
        request.put("replyId", replyId);
        Map<String, Object> expectedResponse = Map.of(
                "replyId", "123",
                "mdContent", "Test content",
                "answerId", "123"
        );
        when(interactionFacade.getReply(request)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<?> result = postingController.getReply(token, replyId);

        // Assert
        verify(interactionFacade, times(1)).getReply(request);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(expectedResponse, result.getBody());
    }

    @Test
    void testGetReply_Failure() throws Exception {
        // Arrange
        String token = "Bearer testToken";
        Long replyId = 123L;
        Map<String, Object> request = new HashMap<>();
        request.put("jwt", token.replace("Bearer ", ""));
        request.put("replyId", replyId);
        when(interactionFacade.getReply(request)).thenThrow(new Exception("Test exception"));

        // Act
        ResponseEntity<?> result = postingController.getReply(token, replyId);

        // Assert
        verify(interactionFacade, times(1)).getReply(request);
        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        assertEquals("Test exception", result.getBody());
    }

    @Test
    void testGetAnswers_Success() throws Exception {
        // Arrange
        String token = "Bearer testToken";
        Long questionId = 123L;
        int offset = 0;
        int limit = 10;
        Map<String, Object> request = new HashMap<>();
        request.put("jwt", token.replace("Bearer ", ""));
        request.put("questionId", questionId);
        request.put("offset", offset);
        request.put("limit", limit);

        ArrayList<Map<String, Object>> answers = new ArrayList<>();

        Map<String, Object> answer1 = Map.of(
                "answerId", "123",
                "mdContent", "Test content",
                "questionId", "123",
                "isUpvoted", false,
                "isDownvoted", false,
                "upvotes", 0,
                "downvotes", 0
        );

        Map<String, Object> answer2 = Map.of(
                "answerId", "456",
                "mdContent", "Test content",
                "questionId", "123",
                "isUpvoted", false,
                "isDownvoted", false,
                "upvotes", 0,
                "downvotes", 0
        );

        answers.add(answer1);
        answers.add(answer2);

        when(interactionFacade.getAnswersForQuestion(request)).thenReturn(answers);

        // Act
        ResponseEntity<?> result = postingController.getAnswers(token, questionId, offset, limit);

        // Assert
        verify(interactionFacade, times(1)).getAnswersForQuestion(request);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(answers, result.getBody());
    }

//    @Test
//    void testGetAnswers_Failure() throws Exception {
//        // Arrange
//        String token = "Bearer testToken";
//        Long questionId = 123L;
//        int offset = 0;
//        int limit = 10;
//        Map<String, Object> request = new HashMap<>();
//
//        request.put("jwt", token.replace("Bearer ", ""));
//        request.put("questionId", questionId);
//        request.put("offset", offset);
//        request.put("limit", limit);
//
//        when(interactionFacade.getAnswersForQuestion(request)).thenThrow(new Exception("Test exception"));
//
//        // Act
//        ResponseEntity<?> result = postingController.getAnswers(token, questionId, offset, limit);
//
//        // Assert
//        verify(interactionFacade, times(1)).getAnswersForQuestion(request);
//        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
//        assertEquals("Test exception", result.getBody());
//    }
//
//    @Test
//    void testGetReplies_Success() throws Exception {
//        // Arrange
//        String token = "Bearer testToken";
//        Long answerId = 123L;
//        int offset = 0;
//        int limit = 10;
//        Map<String, Object> request = new HashMap<>();
//        request.put("jwt", token.replace("Bearer ", ""));
//        request.put("answerId", answerId);
//        request.put("offset", offset);
//        request.put("limit", limit);
//
//        ArrayList<Map<String, Object>> replies = new ArrayList<>();
//
//        Map<String, Object> reply1 = Map.of(
//                "replyId", "123",
//                "mdContent", "Test content",
//                "answerId", "123",
//                "isUpvoted", false,
//                "isDownvoted", false,
//                "upvotes", 0,
//                "downvotes", 0
//        );
//
//        Map<String, Object> reply2 = Map.of(
//                "replyId", "456",
//                "mdContent", "Test content",
//                "answerId", "123",
//                "isUpvoted", false,
//                "isDownvoted", false,
//                "upvotes", 0,
//                "downvotes", 0
//        );
//
//        replies.add(reply1);
//        replies.add(reply2);
//
//        when(interactionFacade.getRepliesForAnswer(request)).thenReturn(replies);
//
//        // Act
//        ResponseEntity<?> result = postingController.getReplies(token, answerId, offset, limit);
//
//        // Assert
//        verify(interactionFacade, times(1)).getRepliesForAnswer(request);
//        assertEquals(HttpStatus.OK, result.getStatusCode());
//        assertEquals(replies, result.getBody());
//    }


    @Test
    void testUpdateQuestion_Success() throws Exception {
        // Arrange
        String token = "Bearer testToken";
        Long questionId = 123L;
        Map<String, Object> question = new HashMap<>();
        question.put("jwt", "testToken");
        question.put("mdContent", "Test content");
        question.put("title", "Test title");
        question.put("tags", new String[]{"tag1", "tag2"});


        boolean expectedResponse = true;
        when(interactionFacade.editPost(any())).thenReturn(true);

        // Act
        ResponseEntity<?> result = postingController.updateQuestion(token, questionId, question);

        // Assert
        verify(interactionFacade, times(1)).editPost(question);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(expectedResponse, result.getBody());
    }

    @Test
    void testUpdateQuestion_Failure() throws Exception {
        // Arrange
        String token = "Bearer testToken";
        Long questionId = 123L;
        Map<String, Object> question = new HashMap<>();
        question.put("jwt", "testToken");
        question.put("mdContent", "Test content");
        question.put("title", "Test title");

        when(interactionFacade.editPost(any())).thenThrow(new Exception("Test exception"));

        // Act
        ResponseEntity<?> result = postingController.updateQuestion(token, questionId, question);

        // Assert
        verify(interactionFacade, times(1)).editPost(question);
        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        assertEquals("Test exception", result.getBody());
    }

    @Test
    void testUpdateAnswer_Success() throws Exception {
        // Arrange
        String token = "Bearer testToken";
        Long answerId = 456L;
        Map<String, Object> answer = new HashMap<>();
        answer.put("jwt", "testToken");
        answer.put("content", "Answer content");

        boolean expectedResponse = true;
        when(interactionFacade.editPost(any())).thenReturn(true);

        // Act
        ResponseEntity<?> result = postingController.updateAnswer(token, answerId, answer);

        // Assert
        verify(interactionFacade, times(1)).editPost(answer);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(expectedResponse, result.getBody());
    }

    @Test
    void testUpdateAnswer_Failure() throws Exception {
        // Arrange
        String token = "Bearer testToken";
        Long answerId = 456L;
        Map<String, Object> answer = new HashMap<>();
        answer.put("jwt", "testToken");
        answer.put("content", "Answer content");

        when(interactionFacade.editPost(any())).thenThrow(new Exception("Test exception"));

        // Act
        ResponseEntity<?> result = postingController.updateAnswer(token, answerId, answer);

        // Assert
        verify(interactionFacade, times(1)).editPost(answer);
        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        assertEquals("Test exception", result.getBody());
    }

    @Test
    void testUpdateReply_Success() throws Exception {
        // Arrange
        String token = "Bearer testToken";
        Long replyId = 789L;
        Map<String, Object> reply = new HashMap<>();
        reply.put("jwt", "testToken");
        reply.put("content", "Reply content");

        boolean expectedResponse = true;
        when(interactionFacade.editPost(any())).thenReturn(true);

        // Act
        ResponseEntity<?> result = postingController.updateReply(token, replyId, reply);

        // Assert
        verify(interactionFacade, times(1)).editPost(reply);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(expectedResponse, result.getBody());
    }

    @Test
    void testUpdateReply_Failure() throws Exception {
        // Arrange
        String token = "Bearer testToken";
        Long replyId = 789L;
        Map<String, Object> reply = new HashMap<>();
        reply.put("jwt", "testToken");
        reply.put("content", "Reply content");

        when(interactionFacade.editPost(any())).thenThrow(new Exception("Test exception"));

        // Act
        ResponseEntity<?> result = postingController.updateReply(token, replyId, reply);

        // Assert
        verify(interactionFacade, times(1)).editPost(reply);
        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        assertEquals("Test exception", result.getBody());
    }

    @Test
    void testDeleteQuestion_Success() throws Exception {
        // Arrange
        String token = "Bearer testToken";
        Long questionId = 123L;

        boolean expectedResponse = true;
        when(interactionFacade.deleteQuestion(any())).thenReturn(true);

        // Act
        ResponseEntity<?> result = postingController.deleteQuestion(token, questionId);

        // Assert
        verify(interactionFacade, times(1)).deleteQuestion(any());
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(expectedResponse, result.getBody());
    }

    @Test
    void testDeleteQuestion_Failure() throws Exception {
        // Arrange
        String token = "Bearer testToken";
        Long questionId = 123L;

        when(interactionFacade.deleteQuestion(any())).thenThrow(new Exception("Test exception"));

        // Act
        ResponseEntity<?> result = postingController.deleteQuestion(token, questionId);

        // Assert
        verify(interactionFacade, times(1)).deleteQuestion(any());
        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        assertEquals("Test exception", result.getBody());
    }

    @Test
    void testDeleteAnswer_Success() throws Exception {
        // Arrange
        String token = "Bearer testToken";
        Long answerId = 456L;

        boolean expectedResponse = true;
        when(interactionFacade.deleteAnswer(any())).thenReturn(true);

        // Act
        ResponseEntity<?> result = postingController.deleteAnswer(token, answerId);

        // Assert
        verify(interactionFacade, times(1)).deleteAnswer(any());
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(expectedResponse, result.getBody());
    }

    @Test
    void testDeleteAnswer_Failure() throws Exception {
        // Arrange
        String token = "Bearer testToken";
        Long answerId = 456L;

        when(interactionFacade.deleteAnswer(any())).thenThrow(new Exception("Test exception"));

        // Act
        ResponseEntity<?> result = postingController.deleteAnswer(token, answerId);

        // Assert
        verify(interactionFacade, times(1)).deleteAnswer(any());
        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        assertEquals("Test exception", result.getBody());
    }

    @Test
    void testDeleteReply_Success() throws Exception {
        // Arrange
        String token = "Bearer testToken";
        Long replyId = 789L;

        boolean expectedResponse = true;
        when(interactionFacade.deleteReply(any())).thenReturn(true);

        // Act
        ResponseEntity<?> result = postingController.deleteReply(token, replyId);

        // Assert
        verify(interactionFacade, times(1)).deleteReply(any());
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(expectedResponse, result.getBody());
    }

    @Test
    void testDeleteReply_Failure() throws Exception {
        // Arrange
        String token = "Bearer testToken";
        Long replyId = 789L;

        when(interactionFacade.deleteReply(any())).thenThrow(new Exception("Test exception"));

        // Act
        ResponseEntity<?> result = postingController.deleteReply(token, replyId);

        // Assert
        verify(interactionFacade, times(1)).deleteReply(any());
        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        assertEquals("Test exception", result.getBody());
    }
//    @Test
//    void testGetUserQuestions_Success() throws Exception {
//        // Arrange
//        String token = "Bearer testToken";
//        int limit = 10;
//        int offset = 0;
//        Map<String, Object> userData = Map.of("jwt", "testToken", "limit", limit, "offset", offset);
//        List<Map<String, Object>> mockQuestions = new ArrayList<>();
//        mockQuestions.add(Map.of("questionId", 123L, "questionText", "Test Question"));
//        when(interactionFacade.getUserQuestions(userData)).thenReturn(mockQuestions);
//        String userName = "user1";
//        // Act
//        ResponseEntity<?> result = postingController.getUserQuestions(token, limit, offset , userName);
//
//        // Assert
//        verify(interactionFacade, times(1)).getUserQuestions(userData);
//        assertEquals(HttpStatus.OK, result.getStatusCode());
//        assertEquals(mockQuestions, result.getBody());
//    }
//
//
//
//    @Test
//    void testGetCommunityQuestions_Success() throws Exception {
//        // Arrange
//        String token = "Bearer testToken";
//        Long communityId = 123L;
//        int limit = 10;
//        int offset = 0;
//        Map<String, Object> communityData = Map.of("jwt", "testToken", "communityId", communityId, "limit", limit, "offset", offset);
//        List<Map<String, Object>> mockQuestions = new ArrayList<>();
//        mockQuestions.add(Map.of("questionId", 456L, "questionText", "Community Question"));
//        when(interactionFacade.getCommunityQuestions(communityData)).thenReturn(mockQuestions);
//
//        // Act
//        ResponseEntity<?> result = postingController.getCommunityQuestions(token, communityId, limit, offset);
//
//        // Assert
//        verify(interactionFacade, times(1)).getCommunityQuestions(communityData);
//        assertEquals(HttpStatus.OK, result.getStatusCode());
//        assertEquals(mockQuestions, result.getBody());
//    }
//
//
//    @Test
//    void testGetCommunityQuestions_Failure() throws Exception {
//        // Arrange
//        String token = "Bearer testToken";
//        Long communityId = 123L;
//        int limit = 10;
//        int offset = 0;
//        Map<String, Object> communityData = Map.of("jwt", "testToken", "communityId", communityId, "limit", limit, "offset", offset);
//        when(interactionFacade.getCommunityQuestions(communityData)).thenThrow(new Exception("Test exception"));
//
//        // Act
//        ResponseEntity<?> result = postingController.getCommunityQuestions(token, communityId, limit, offset);
//
//        // Assert
//        verify(interactionFacade, times(1)).getCommunityQuestions(communityData);
//        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
//        assertEquals(Map.of("message", "Error fetching community questions"), result.getBody());
//    }
//
//    @Test
//    void updateQuestionSuccess() throws Exception {
//        String token = "Bearer validToken"; // Replace with a valid token
//        Long questionId = 1L;
//        Map<String, Object> question = Map.of(
//                "title", "Updated Title",
//                "content", "Updated Content"
//        );
//
//        mockMvc.perform(MockMvcRequestBuilders.put("/questions")
//                        .header("Authorization", token)
//                        .param("questionId", questionId.toString())
//                        .contentType("application/json")
//                        .content("{\"title\": \"Updated Title\", \"content\": \"Updated Content\"}"))
//                .andExpect(status().isOk())
//                .andExpect((ResultMatcher) jsonPath("$.status").value("OK"))
//                .andExpect((ResultMatcher) jsonPath("$.data").isNotEmpty()); // Adjust as needed for the response structure
//    }
//
//    @Test
//    void updateQuestionFailure() throws Exception {
//        String token = "Bearer invalidToken"; // Invalid token
//        Long questionId = 1L;
//        Map<String, Object> question = Map.of(); // Empty question data
//
//        mockMvc.perform(MockMvcRequestBuilders.put("/questions")
//                        .header("Authorization", token)
//                        .param("questionId", questionId.toString())
//                        .contentType("application/json")
//                        .content("{}")) // Empty content
//                .andExpect(status().isUnauthorized())
//                .andExpect((ResultMatcher) jsonPath("$.message").value("Unauthorized"));
//
//        // Another failure case: valid token, but no question data
//        mockMvc.perform(MockMvcRequestBuilders.put("/questions")
//                        .header("Authorization", token)
//                        .param("questionId", questionId.toString())
//                        .contentType("application/json"))
//                .andExpect(status().isBadRequest()) // Assuming bad request for missing data
//                .andExpect((ResultMatcher) jsonPath("$.message").value("Request body is empty or invalid"));
//    }
//    @Test
//    void testUpvoteQuestion_Success() throws Exception {
//        String token = "Bearer testToken";
//        Long postId = 123L;
//        Map<String, Object> postData = Map.of("jwt", "testToken", "questionId", postId);
//        when(interactionFacade.upVoteQuestion(postData)).thenReturn(true);
//
//        ResponseEntity<?> result = postingController.upvoteQuestion(token, postId);
//
//        assertEquals(HttpStatus.OK, result.getStatusCode());
//    }
//
//    @Test
//    void testUpvoteQuestion_Failure() throws Exception {
//        String token = "Bearer testToken";
//        Long postId = 123L;
//        Map<String, Object> postData = Map.of("jwt", "testToken", "questionId", postId);
//        when(interactionFacade.upVoteQuestion(postData)).thenThrow(new Exception("Test exception"));
//
//        ResponseEntity<?> result = postingController.upvoteQuestion(token, postId);
//
//        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
//        assertEquals("Test exception", result.getBody());
//    }
//
//    @Test
//    void testRemoveUpvoteQuestion_Success() throws Exception {
//        String token = "Bearer testToken";
//        Long postId = 123L;
//        Map<String, Object> postData = Map.of("jwt", "testToken", "questionId", postId);
//        when(interactionFacade.removeUpVoteQuestion(postData)).thenReturn(true);
//
//        ResponseEntity<?> result = postingController.removeUpvoteQuestion(token, postId);
//
//        assertEquals(HttpStatus.OK, result.getStatusCode());
//    }
//
//    @Test
//    void testRemoveUpvoteQuestion_Failure() throws Exception {
//        String token = "Bearer testToken";
//        Long postId = 123L;
//        Map<String, Object> postData = Map.of("jwt", "testToken", "questionId", postId);
//        when(interactionFacade.removeUpVoteQuestion(postData)).thenThrow(new Exception("Test exception"));
//
//        ResponseEntity<?> result = postingController.removeUpvoteQuestion(token, postId);
//
//        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
//        assertEquals("Test exception", result.getBody());
//    }
//
//    @Test
//    void testRemoveUpvoteAnswer_Success() throws Exception {
//        String token = "Bearer testToken";
//        Long postId = 456L;
//        Map<String, Object> postData = Map.of("jwt", "testToken", "answerId", postId);
//        when(interactionFacade.removeUpVoteAnswer(postData)).thenReturn(true);
//
//        ResponseEntity<?> result = postingController.removeUpvoteAnswer(token, postId);
//
//        assertEquals(HttpStatus.OK, result.getStatusCode());
//    }
//
//    @Test
//    void testRemoveUpvoteAnswer_Failure() throws Exception {
//        String token = "Bearer testToken";
//        Long postId = 456L;
//        Map<String, Object> postData = Map.of("jwt", "testToken", "answerId", postId);
//        when(interactionFacade.removeUpVoteAnswer(postData)).thenThrow(new Exception("Test exception"));
//
//        ResponseEntity<?> result = postingController.removeUpvoteAnswer(token, postId);
//
//        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
//        assertEquals("Test exception", result.getBody());
//    }
//
//    @Test
//    void testDownvoteAnswer_Success() throws Exception {
//        String token = "Bearer testToken";
//        Long answerId = 456L;
//        Map<String, Object> postData = Map.of("jwt", "testToken", "answerId", answerId);
//        when(interactionFacade.downVoteAnswer(postData)).thenReturn(true);
//
//        ResponseEntity<?> result = postingController.downvoteAnswer(token, answerId);
//
//        assertEquals(HttpStatus.OK, result.getStatusCode());
//    }
//
//    @Test
//    void testDownvoteAnswer_Failure() throws Exception {
//        String token = "Bearer testToken";
//        Long answerId = 456L;
//        Map<String, Object> postData = Map.of("jwt", "testToken", "answerId", answerId);
//        when(interactionFacade.downVoteAnswer(postData)).thenThrow(new Exception("Test exception"));
//
//        ResponseEntity<?> result = postingController.downvoteAnswer(token, answerId);
//
//        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
//        assertEquals("Test exception", result.getBody());
//    }
//
//    @Test
//    void testRemoveDownvoteAnswer_Success() throws Exception {
//        String token = "Bearer testToken";
//        Long answerId = 456L;
//        Map<String, Object> postData = Map.of("jwt", "testToken", "answerId", answerId);
//        when(interactionFacade.removeDownVoteAnswer(postData)).thenReturn(true);
//
//        ResponseEntity<?> result = postingController.removeDownvoteAnswer(token, answerId);
//
//        assertEquals(HttpStatus.OK, result.getStatusCode());
//    }
//
//    @Test
//    void testRemoveDownvoteAnswer_Failure() throws Exception {
//        String token = "Bearer testToken";
//        Long answerId = 456L;
//        Map<String, Object> postData = Map.of("jwt", "testToken", "answerId", answerId);
//        when(interactionFacade.removeDownVoteAnswer(postData)).thenThrow(new Exception("Test exception"));
//
//        ResponseEntity<?> result = postingController.removeDownvoteAnswer(token, answerId);
//
//        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
//        assertEquals("Test exception", result.getBody());
//    }
//
//    @Test
//    void testUpvoteAnswer_Success() throws Exception {
//        String token = "Bearer testToken";
//        Long answerId = 456L;
//        Map<String, Object> postData = Map.of("jwt", "testToken", "answerId", answerId);
//        when(interactionFacade.upVoteAnswer(postData)).thenReturn(true);
//
//        ResponseEntity<?> result = postingController.upvoteAnswer(token, answerId);
//
//        assertEquals(HttpStatus.OK, result.getStatusCode());
//    }
//
//    @Test
//    void testUpvoteAnswer_Failure() throws Exception {
//        String token = "Bearer testToken";
//        Long answerId = 456L;
//        Map<String, Object> postData = Map.of("jwt", "testToken", "answerId", answerId);
//        when(interactionFacade.upVoteAnswer(postData)).thenThrow(new Exception("Test exception"));
//
//        ResponseEntity<?> result = postingController.upvoteAnswer(token, answerId);
//
//        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
//        assertEquals("Test exception", result.getBody());
//    }
//
//    @Test
//    void testDownvoteQuestion_Success() throws Exception {
//        String token = "Bearer testToken";
//        Long postId = 123L;
//        Map<String, Object> postData = Map.of("jwt", "testToken", "questionId", postId);
//        when(interactionFacade.downVoteQuestion(postData)).thenReturn(true);
//
//        ResponseEntity<?> result = postingController.downvoteQuestion(token, postId);
//
//        verify(interactionFacade, times(1)).downVoteQuestion(postData);
//        assertEquals(HttpStatus.OK, result.getStatusCode());
//        assertEquals(Map.of("message", "Downvoted question successfully"), result.getBody());
//    }
//
//    @Test
//    void testDownvoteQuestion_Failure() throws Exception {
//        String token = "Bearer testToken";
//        Long postId = 123L;
//        Map<String, Object> postData = Map.of("jwt", "testToken", "questionId", postId);
//        when(interactionFacade.downVoteQuestion(postData)).thenThrow(new Exception("Test exception"));
//
//        ResponseEntity<?> result = postingController.downvoteQuestion(token, postId);
//
//        verify(interactionFacade, times(1)).downVoteQuestion(postData);
//        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
//        assertEquals("Test exception", result.getBody());
//    }
//
//    @Test
//    void testRemoveDownvoteQuestion_Success() throws Exception {
//        String token = "Bearer testToken";
//        Long postId = 123L;
//        Map<String, Object> postData = Map.of("jwt", "testToken", "questionId", postId);
//        when(interactionFacade.removeDownVoteQuestion(postData)).thenReturn(true);
//
//        ResponseEntity<?> result = postingController.removeDownvoteQuestion(token, postId);
//
//        verify(interactionFacade, times(1)).removeDownVoteQuestion(postData);
//        assertEquals(HttpStatus.OK, result.getStatusCode());
//        assertEquals(Map.of("message", "Downvote removed from question successfully"), result.getBody());
//    }
//
//    @Test
//    void testRemoveDownvoteQuestion_Failure() throws Exception {
//        String token = "Bearer testToken";
//        String postId = "123";
//        Map<String, Object> postData = Map.of("jwt", "testToken", "questionId", postId);
//        when(interactionFacade.removeDownVoteQuestion(postData)).thenThrow(new Exception("Test exception"));
//
//        ResponseEntity<?> result = postingController.removeDownvoteQuestion(token, (long) 123L);
//
//        verify(interactionFacade, times(1)).removeDownVoteQuestion(postData);
//        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
//        assertEquals("Test exception", result.getBody());
//    }

}