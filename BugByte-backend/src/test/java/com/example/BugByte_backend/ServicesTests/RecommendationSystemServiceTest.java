package com.example.BugByte_backend.ServicesTests;

import com.example.BugByte_backend.models.Question;
import com.example.BugByte_backend.models.User;
import com.example.BugByte_backend.repositories.RecommendationSystemRepository;
import com.example.BugByte_backend.repositories.TagsRepository;
import com.example.BugByte_backend.services.AuthenticationService;
import com.example.BugByte_backend.services.RecommendationSystemService;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RecommendationSystemServiceTest {

    @Mock
    private RecommendationSystemRepository recommendationSystemRepository;

    @Mock
    private TagsRepository tagsRepository;

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private ListOperations<String, Object> listOperations;

    @Mock
    private Claims claims;

    @InjectMocks
    private RecommendationSystemService recommendationSystemService;

    private static final Long USER_ID = 1L;
    private static final String TOKEN = "Bearer testToken";

    @BeforeEach
    void setUp() {
        // Setup common mocking behavior
        when(redisTemplate.opsForList()).thenReturn(listOperations);
        when(AuthenticationService.parseToken(anyString())).thenReturn(claims);
        when(claims.getId()).thenReturn(String.valueOf(USER_ID));
    }

    @Test
    void generateFeedForUser_EmptyCache_ShouldGenerateNewFeed() {
        // Arrange
        List<Question> mockQuestions = createMockQuestions();
        when(listOperations.size(anyString())).thenReturn(0L);
        when(recommendationSystemRepository.generateFeedForUser(USER_ID)).thenReturn(mockQuestions);
        when(tagsRepository.findTagsByQuestion(anyLong())).thenReturn(new ArrayList<>());

        // Act
        List<Question> result = recommendationSystemService.generateFeedForUser(TOKEN, 10);

        // Assert
        assertNotNull(result);
        verify(recommendationSystemRepository).generateFeedForUser(USER_ID);
        verify(listOperations, times(mockQuestions.size())).rightPush(anyString(), any());
    }

    @Test
    void generateFeedForUser_WithExistingCache_ShouldReturnCachedFeed() {
        // Arrange
        when(listOperations.size(anyString())).thenReturn(5L);
        List<Question> mockQuestions = createMockQuestions();
        when(listOperations.leftPop(anyString())).thenReturn(
                mockQuestions.get(0), mockQuestions.get(1), null
        );

        // Act
        List<Question> result = recommendationSystemService.generateFeedForUser(TOKEN, 10);

        // Assert
        assertEquals(2, result.size());
    }

    @Test
    void updateUsersFeed_ShouldUpdateMultipleUserFeeds() {
        // Arrange
        Question newQuestion = new Question();
        List<User> users = Arrays.asList(
                createUser(1L),
                createUser(2L),
                createUser(3L)
        );

        // Act
        recommendationSystemService.updateUsersFeed(users, newQuestion);

        // Assert
        verify(listOperations, times(users.size())).leftPush(anyString(), eq(newQuestion));
        verify(listOperations, times(users.size())).trim(anyString(), eq(0), eq(99));
    }

    @Test
    void getUserIdFromToken_ValidToken_ShouldReturnUserId() {
        // Arrange
        String validToken = "Bearer validToken";

        // Act
        List<Question> userId = recommendationSystemService.generateFeedForUser(validToken, 10);

        // Assert
        assertEquals(USER_ID, userId);
    }

    @Test
    void getUserIdFromToken_NullToken_ShouldThrowNullPointerException() {
        // Assert
        assertThrows(NullPointerException.class, () -> {
            recommendationSystemService.generateFeedForUser(null, 10);
        });
    }

    private List<Question> createMockQuestions() {
        List<Question> questions = new ArrayList<>();
        Question q1 = new Question();
        q1.setId(1L);
        Question q2 = new Question();
        q2.setId(2L);
        questions.add(q1);
        questions.add(q2);
        return questions;
    }

    private User createUser(Long id) {
        User user = new User();
        user.setId(id);
        return user;
    }
}