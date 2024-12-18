package com.example.BugByte_backend.Controllers;

import com.example.BugByte_backend.controllers.SearchingFilteringController;
import com.example.BugByte_backend.models.Community;
import com.example.BugByte_backend.models.Question;
import com.example.BugByte_backend.services.AuthenticationService;
import com.example.BugByte_backend.services.SearchingFilteringCommunityService;
import com.example.BugByte_backend.services.SearchingFilteringQuestionService;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class SearchingFilteringControllerTest {
    @InjectMocks
    private SearchingFilteringController controller;

    @Mock
    private SearchingFilteringQuestionService questionService;

    @Mock
    private SearchingFilteringCommunityService communityService;

    @Mock
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void searchQuestionsByContent_ShouldReturnQuestions() throws Exception {
        String token = "Bearer validToken";
        String content = "question content";
        int page = 0, size = 10;

        // Mocking
        Claims claims = mock(Claims.class);
        when(AuthenticationService.parseToken("validToken")).thenReturn(claims);
        List<Question> questions = List.of(Question.builder().id(1L).title("Sample Question").build());
        Page<Question> questionPage = new PageImpl<>(questions);
        when(questionService.searchQuestions(content, page, size)).thenReturn(questionPage);

        // Call
        ResponseEntity<?> response = controller.searchQuestionsByContent(token, content, page, size);

        // Verify
        assertEquals(200, response.getStatusCodeValue());
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertNotNull(body);
        assertEquals(questions, body.get("questions"));
    }

    @Test
    void filterQuestionByTags_ShouldReturnFilteredQuestions() throws Exception {
        String token = "Bearer validToken";
        List<String> tags = List.of("tag1", "tag2");
        int page = 0, size = 10;

        // Mocking
        Claims claims = mock(Claims.class);
        when(AuthenticationService.parseToken("validToken")).thenReturn(claims);
        List<Question> questions = List.of(Question.builder().id(1L).title("Sample Question").build());
        Page<Question> questionPage = new PageImpl<>(questions);
        when(questionService.getQuestionsByTags(tags, page, size)).thenReturn(questionPage);

        // Call
        ResponseEntity<?> response = controller.filterQuestionByTags(token, tags, page, size);

        // Verify
        assertEquals(200, response.getStatusCodeValue());
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertNotNull(body);
        assertEquals(questions, body.get("questions"));
    }

    @Test
    void searchCommunityByContent_ShouldReturnCommunities() throws Exception {
        String token = "Bearer validToken";
        String content = "community content";
        int page = 0, size = 10;

        // Mocking
        Claims claims = mock(Claims.class);
        when(AuthenticationService.parseToken("validToken")).thenReturn(claims);
        List<Community> communities = List.of(Community.builder().id(1L).description("Simple community").build());
        Page<Community> communityPage = new PageImpl<>(communities);
        when(communityService.searchCommunity(content, page, size)).thenReturn(communityPage);

        // Call
        ResponseEntity<?> response = controller.searchCommunityByContent(token, content, page, size);

        // Verify
        assertEquals(200, response.getStatusCodeValue());
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertNotNull(body);
        assertEquals(communities, body.get("communities"));
    }

    @Test
    void filterCommunityByTags_ShouldReturnFilteredCommunities() throws Exception {
        String token = "Bearer validToken";
        List<String> tags = List.of("tag1", "tag2");
        int page = 0, size = 10;

        // Mocking
        Claims claims = mock(Claims.class);
        when(AuthenticationService.parseToken("validToken")).thenReturn(claims);
        List<Community> communities = List.of(Community.builder().id(1L).description("Simple community").build());
        Page<Community> communityPage = new PageImpl<>(communities);
        when(communityService.getCommunityByTags(tags, page, size)).thenReturn(communityPage);

        // Call
        ResponseEntity<?> response = controller.filterCommunityByTags(token, tags, page, size);

        // Verify
        assertEquals(200, response.getStatusCodeValue());
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertNotNull(body);
        assertEquals(communities, body.get("communities"));
    }

    @Test
    void searchQuestionsByContent_ShouldReturnUnauthorized_WhenTokenInvalid() {
        String token = "Bearer invalidToken";
        String content = "question content";
        int page = 0, size = 10;

        // Mocking
        when(AuthenticationService.parseToken("invalidToken")).thenThrow(new RuntimeException("Invalid Token"));

        // Call
        ResponseEntity<?> response = controller.searchQuestionsByContent(token, content, page, size);

        // Verify
        assertEquals(401, response.getStatusCodeValue());
        assertEquals("Invalid Token", response.getBody());
    }
}
