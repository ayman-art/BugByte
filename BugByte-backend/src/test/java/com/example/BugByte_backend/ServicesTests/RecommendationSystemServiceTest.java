package com.example.BugByte_backend.ServicesTests;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.BugByte_backend.models.Question;
import com.example.BugByte_backend.models.User;
import com.example.BugByte_backend.repositories.RecommendationSystemRepository;
import com.example.BugByte_backend.repositories.TagsRepository;
import com.example.BugByte_backend.services.RecommendationSystemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ListOperations;

import java.util.*;

class RecommendationSystemServiceTest {

    @Mock
    private RecommendationSystemRepository recommendationSystemRepository;

    @Mock
    private TagsRepository tagsRepository;

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private ListOperations<String, Object> listOperations;

    @Spy
    @InjectMocks
    private RecommendationSystemService recommendationSystemService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGenerateFeedForUser_CacheEmpty() throws Exception {
        String jwt = "Bearer validToken";
        Long userId = 1L;
        int pageSize = 2;

        // Mock the `getUserIdFromToken` method to immediately return `userId`
        doReturn(userId).when(recommendationSystemService).getUserIdFromToken(jwt);

        List<Question> mockQuestions = List.of(
                Question.builder().id(1L).title("Question 1").build(),
                Question.builder().id(2L).title("Question 2").build()
        );
        String cacheKey = "feed:" + userId;

        when(redisTemplate.opsForList().size(cacheKey)).thenReturn(0L);
        when(recommendationSystemRepository.generateFeedForUser(userId)).thenReturn(mockQuestions);
        when(tagsRepository.findTagsByQuestion(anyLong())).thenReturn(List.of("java", "python"));

        List<Question> result = recommendationSystemService.generateFeedForUser(jwt, pageSize);

        assertNotNull(result);
        assertEquals(pageSize, result.size());
    }

    @Test
    void testGenerateFeedForUser_CacheNotEmpty() throws Exception {
        String token = "Bearer mock-token";
        Long userId = 1L;
        int pageSize = 2;

        // Mocking
        when(listOperations.size("feed:" + userId)).thenReturn(3L);
        Question question = Question.builder()
                .id(1L)
                .title("Cached Question")
                .build();
        when(listOperations.leftPop("feed:" + userId)).thenReturn(question, question);
        doReturn(userId).when(recommendationSystemService).getUserIdFromToken(token);

        // Test
        List<Question> result = recommendationSystemService.generateFeedForUser(token, pageSize);

        // Verify
        verify(recommendationSystemRepository, never()).generateFeedForUser(userId);
        assertEquals(2, result.size());
        assertEquals("Cached Question", result.getFirst().getTitle());
    }

    @Test
    void testUpdateUsersFeed() {
        List<User> users = Arrays.asList(User.builder()
                        .id(1L)
                        .build(),
                User.builder()
                        .id(2L)
                        .build());
        Question question = Question
                .builder()
                .id(1L)
                .title("New Question")
                .build();

        // Test
        recommendationSystemService.updateUsersFeed(users, question);

        // Verify
        for (User user : users) {
            verify(listOperations).leftPush("feed:" + user.getId(), question);
            verify(listOperations).trim("feed:" + user.getId(), 0, 99);
        }
    }

    @Test
    void testGetUserIdFromToken_NullToken() {
        assertThrows(NullPointerException.class, () -> recommendationSystemService.getUserIdFromToken(null));
    }
}
