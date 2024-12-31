package com.example.BugByte_backend.Controllers;

import com.example.BugByte_backend.controllers.RecommendationSystemController;
import com.example.BugByte_backend.models.Community;
import com.example.BugByte_backend.models.Question;
import com.example.BugByte_backend.services.RecommendationSystemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.mockito.Mockito.*;

@SpringBootTest
public class RecommendationSystemControllerTest {
    @Mock
    private RecommendationSystemService recommendationSystemService;

    @InjectMocks
    private RecommendationSystemController recommendationSystemController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetFeed_Success() throws Exception {
        String token = "valid-token";
        int size = 10;
        int page = 1;
        List<String> tags = List.of("java", "python");
        Question q1 = Question.builder()
                .id(1L)
                .title("title")
                .mdContent("hello world")
                .tags(tags)
                .upVotes(0L)
                .downVotes(0L)
                .communityId(1L)
                .validatedAnswerId(1L)
                .creatorUserName("Ashraf")
                .build();
        Question q2 = Question.builder()
                .id(2L)
                .title("title2")
                .mdContent("hello world2")
                .tags(tags)
                .upVotes(0L)
                .downVotes(0L)
                .communityId(2L)
                .validatedAnswerId(2L)
                .creatorUserName("Ashraf")
                .build();
        List<Question> mockFeed = List.of(q1, q2);
        when(recommendationSystemService.generateFeedForUser(token, size)).thenReturn(mockFeed);

        ResponseEntity<?> response = recommendationSystemController.getFeed(token,page, size);

        verify(recommendationSystemService, times(1)).generateFeedForUser(token, size);
        assert response.getStatusCode() == HttpStatus.OK;
        assert response.getBody() instanceof Map;
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assert body.get("feed").equals(mockFeed);
    }

    @Test
    void testGetFeed_Unauthorized() throws Exception {
        String token = "invalid-token";
        int size = 5;
        int page = 1;
        when(recommendationSystemService.generateFeedForUser(token, size))
                .thenThrow(new RuntimeException("Unauthorized"));

        ResponseEntity<?> response = recommendationSystemController.getFeed(token,page, size);

        verify(recommendationSystemService, times(1)).generateFeedForUser(token, size);
        assert response.getStatusCode() == HttpStatus.UNAUTHORIZED;
        assert Objects.equals(response.getBody(), "Unauthorized");
    }

    @Test
    void testGetCommunities_Success() throws Exception {
        String token = "valid-token";
        List<String> tags = List.of("java", "python");
        Community c1 = Community.builder()
                .id(1L)
                .name("Community 1")
                .description("Description 1")
                .adminId(2L)
                .tags(tags)
                .build();
        Community c2 = Community.builder()
                .id(2L)
                .name("Community 2")
                .description("Description 2")
                .adminId(3L)
                .tags(tags)
                .build();
        List<Community> mockCommunities = List.of(c1, c2);

        when(recommendationSystemService.getCommunityRecommendations(token)).thenReturn(mockCommunities);

        ResponseEntity<?> response = recommendationSystemController.getCommunities(token);

        verify(recommendationSystemService, times(1)).getCommunityRecommendations(token);
        assert response.getStatusCode() == HttpStatus.OK;
        assert response.getBody() instanceof Map;
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assert body.get("communities").equals(mockCommunities);
    }

    @Test
    void testGetCommunities_Unauthorized() throws Exception {
        String token = "invalid-token";
        int size = 5;
        int page = 1;
        when(recommendationSystemService.getCommunityRecommendations(token))
                .thenThrow(new RuntimeException("Unauthorized"));

        ResponseEntity<?> response = recommendationSystemController.getCommunities(token);

        verify(recommendationSystemService, times(1)).getCommunityRecommendations(token);
        assert response.getStatusCode() == HttpStatus.UNAUTHORIZED;
        assert Objects.equals(response.getBody(), "Unauthorized");
    }
}
