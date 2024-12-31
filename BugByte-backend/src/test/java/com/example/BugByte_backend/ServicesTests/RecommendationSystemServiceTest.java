package com.example.BugByte_backend.ServicesTests;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.BugByte_backend.models.Community;
import com.example.BugByte_backend.models.Post;
import com.example.BugByte_backend.models.Question;
import com.example.BugByte_backend.models.User;
import com.example.BugByte_backend.repositories.*;
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
    @Mock
    private UserRepositoryImp userRepositoryImp;

    @Mock
    private PostingRepository postingRepository;

    @Spy
    @InjectMocks
    private RecommendationSystemService recommendationSystemService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

//    @Test
//    void testGenerateFeedForUser_CacheEmpty() throws Exception {
//        String jwt = "Bearer validToken";
//        Long userId = 1L;
//        String userName = "userName";
//        int pageSize = 2;
//        long communityId = 1L;
//        Community mockCommunity = Community.builder()
//                .id(communityId)
//                .name("community 1")
//                .build();
//
//        when(communityRepository.findCommunityById(communityId)).thenReturn(mockCommunity);
//
//        // Mock the `getUserIdFromToken` method to immediately return `userId`
//        doReturn(userId).when(recommendationSystemService).getUserIdFromToken(jwt);
//        doReturn(userName).when(recommendationSystemService).getUserNameFromToken(jwt);
//
//        List<Question> mockQuestions = List.of(
//                Question.builder()
//                        .id(1L)
//                        .title("Question 1")
//                        .communityId(1L)
//                        .build(),
//                Question.builder()
//                        .id(2L)
//                        .title("Question 2")
//                        .communityId(1L)
//                        .build()
//        );
//        String cacheKey = "feed:" + userId;
//
//        long postId = 1L;
//
//        User mockUser = User.builder()
//                .userName("testUser")
//                .build();
//
//        Post mockPost = Post.builder()
//                .id(1L)
//                .build();
//
//        when(userRepositoryImp.findByIdentity(userName)).thenReturn(mockUser);
//        when(postingRepository.getPostByID(anyLong())).thenReturn(mockPost);
//        when(postingRepository.is_UpVoted(anyString(), anyLong())).thenReturn(true);
//        when(postingRepository.is_DownVoted(anyString(), anyLong())).thenReturn(false);
//
//
//        when(redisTemplate.opsForList()).thenReturn(listOperations);
//        when(redisTemplate.opsForList().size(cacheKey)).thenReturn(0L);
//        when(recommendationSystemRepository.generateFeedForUser(userId)).thenReturn(mockQuestions);
//        when(tagsRepository.findTagsByQuestion(anyLong())).thenReturn(List.of("java", "python"));
//        // when(recommendationSystemService.getQuestionCommunity(anyLong())).thenReturn("community 1");
////        when(recommendationSystemService.isUpVoted(anyString(), anyLong())).thenReturn(true);
////        when(recommendationSystemService.isDownVoted(anyString(), anyLong())).thenReturn(false);
//
//        List<Question> result = recommendationSystemService.generateFeedForUser(jwt, pageSize);
//
//        assertNotNull(result);
//        assertEquals(pageSize, result.size());
//    }

    @Test
    void testGenerateFeedForUser_CacheNotEmpty() throws Exception {
        String token = "Bearer mock-token";
        Long userId = 1L;
        String userName = "userName";
        int pageSize = 2;

        when(redisTemplate.opsForList()).thenReturn(listOperations);
        // Mocking
        when(listOperations.size("feed:" + userId)).thenReturn(3L);
        Question question = Question.builder()
                .id(1L)
                .title("Cached Question")
                .build();
        when(listOperations.leftPop("feed:" + userId)).thenReturn(question, question);
        doReturn(userId).when(recommendationSystemService).getUserIdFromToken(token);
        doReturn(userName).when(recommendationSystemService).getUserNameFromToken(token);

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

        when(redisTemplate.opsForList()).thenReturn(listOperations);
        when(redisTemplate.opsForList().leftPush(anyString(), eq(question))).thenReturn(0L);
//        when(redisTemplate.opsForList().trim(anyString(), 0, 99));
        // Test
        recommendationSystemService.updateUsersFeed(users, question);

        // Verify
        for (User user : users) {
            verify(listOperations).leftPush("feed:" + user.getId(), question);
            verify(listOperations).trim("feed:" + user.getId(), 0, 99);
        }
    }

    @Test
    void testUpdateUsersFeed2() {
        // Arrange
        List<User> users = List.of(
                User.builder().id(1L).build(),
                User.builder().id(2L).build()
        );

        Question question = Question.builder()
                .id(1L)
                .title("Test Question")
                .build();

        when(redisTemplate.opsForList()).thenReturn(listOperations);

        // Act
        recommendationSystemService.updateUsersFeed(users, question);

        // Assert
        for (User user : users) {
            String cacheKey = "feed:" + user.getId();
            verify(listOperations).leftPush(cacheKey, question);
            verify(listOperations).trim(cacheKey, 0, 99);
        }
        verifyNoMoreInteractions(listOperations);
    }

    @Test
    void testUpdateUsersFeed_NoUsers() {
        // Arrange
        List<User> users = List.of();
        Question question = Question.builder()
                .id(1L)
                .title("Test Question")
                .build();

        // Act
        recommendationSystemService.updateUsersFeed(users, question);

        // Assert
        verifyNoInteractions(redisTemplate);
    }

    @Test
    void testUpdateUsersFeed_NullQuestion() {
        // Arrange
        List<User> users = List.of(
                User.builder().id(1L).build(),
                User.builder().id(2L).build()
        );

        // Act & Assert
        assertThrows(NullPointerException.class, () -> recommendationSystemService.updateUsersFeed(users, null));
    }

    @Test
    void testGenerateFeedForUser_InvalidToken() {
        String token = "Bearer invalidToken";

        // Mocking
        doThrow(new IllegalArgumentException("Invalid token")).when(recommendationSystemService).getUserIdFromToken(token);

        // Test & Verify
        Exception exception = assertThrows(Exception.class, () -> recommendationSystemService.generateFeedForUser(token, 2));
        assertEquals("Invalid token", exception.getMessage());
    }

    @Test
    void testGetUserIdFromToken_NullToken() {
        assertThrows(NullPointerException.class, () -> recommendationSystemService.getUserIdFromToken(null));
    }

    @Test
    void testGetCommunityRecommendations_ValidToken() {
        String token = "Bearer validToken";
        Long userId = 1L;

        // Mocking
        doReturn(userId).when(recommendationSystemService).getUserIdFromToken(token);

        List<Community> mockCommunities = List.of(
                Community.builder().id(1L).name("Community 1").build(),
                Community.builder().id(2L).name("Community 2").build()
        );

        when(recommendationSystemRepository.generateRecommendedCommunitiesForUser(userId)).thenReturn(mockCommunities);
        when(tagsRepository.findTagsByCommunity(1L)).thenReturn(List.of("java", "python"));
        when(tagsRepository.findTagsByCommunity(2L)).thenReturn(List.of("machine learning", "data science"));

        // Test
        List<Community> result = recommendationSystemService.getCommunityRecommendations(token);

        // Verify
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Community 1", result.get(0).getName());
        assertEquals(List.of("java", "python"), result.get(0).getTags());
        assertEquals("Community 2", result.get(1).getName());
        assertEquals(List.of("machine learning", "data science"), result.get(1).getTags());
    }

    @Test
    void testGetCommunityRecommendations_NullUserId() {
        String token = "Bearer invalidToken";

        // Mocking
        doReturn(null).when(recommendationSystemService).getUserIdFromToken(token);

        // Test & Verify
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> recommendationSystemService.getCommunityRecommendations(token)
        );
        assertEquals("User id can't be null!", exception.getMessage());
    }

    @Test
    void testGetCommunityRecommendations_EmptyRecommendations() {
        String token = "Bearer validToken";
        Long userId = 2L;

        // Mocking
        doReturn(userId).when(recommendationSystemService).getUserIdFromToken(token);
        when(recommendationSystemRepository.generateRecommendedCommunitiesForUser(userId)).thenReturn(List.of());

        // Test
        List<Community> result = recommendationSystemService.getCommunityRecommendations(token);

        // Verify
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetCommunityRecommendations_NoTagsForCommunity() {
        String token = "Bearer validToken";
        Long userId = 3L;

        // Mocking
        doReturn(userId).when(recommendationSystemService).getUserIdFromToken(token);

        List<Community> mockCommunities = List.of(
                Community.builder().id(1L).name("Community 1").build()
        );

        when(recommendationSystemRepository.generateRecommendedCommunitiesForUser(userId)).thenReturn(mockCommunities);
        when(tagsRepository.findTagsByCommunity(1L)).thenReturn(List.of());

        // Test
        List<Community> result = recommendationSystemService.getCommunityRecommendations(token);

        // Verify
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Community 1", result.get(0).getName());
        assertTrue(result.get(0).getTags().isEmpty());
    }

    @Mock
    private CommunityRepository communityRepository;

    @Test
    void testGetQuestionCommunity() throws Exception {
        // Arrange
        long communityId = 1L;
        Community mockCommunity = Community.builder()
                .id(communityId)
                .name("community 1")
                .build();

        when(communityRepository.findCommunityById(communityId)).thenReturn(mockCommunity);

        // Act
        String communityName = recommendationSystemService.getQuestionCommunity(communityId);

        // Assert
        assertEquals("community 1", communityName);
        verify(communityRepository).findCommunityById(communityId);
    }

    @Test
    void testGetQuestionCommunity_ThrowsException() {
        // Arrange
        long communityId = 1L;

        when(communityRepository.findCommunityById(communityId)).thenReturn(null);

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> {
            recommendationSystemService.getQuestionCommunity(communityId);
        });
        assertEquals("community is null", exception.getMessage());
        verify(communityRepository).findCommunityById(communityId);
    }

    @Test
    void testIsUpVoted_UserAndPostExist() throws Exception {
        // Arrange
        String userName = "testUser";
        long postId = 1L;

        User mockUser = User.builder()
                .userName("testUser")
                .build();

        Post mockPost = Post.builder()
                .id(1L)
                .build();

        when(userRepositoryImp.findByIdentity(userName)).thenReturn(mockUser);
        when(postingRepository.getPostByID(postId)).thenReturn(mockPost);
        when(postingRepository.is_UpVoted(userName, postId)).thenReturn(true);

        // Act
        boolean result = recommendationSystemService.isUpVoted(userName, postId);

        // Assert
        assertTrue(result);
        verify(userRepositoryImp).findByIdentity(userName);
        verify(postingRepository).getPostByID(postId);
        verify(postingRepository).is_UpVoted(userName, postId);
    }

    @Test
    void testIsUpVoted_UserDoesNotExist() {
        // Arrange
        String userName = "nonExistentUser";
        long postId = 1L;

        when(userRepositoryImp.findByIdentity(userName)).thenReturn(null);

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> {
            recommendationSystemService.isUpVoted(userName, postId);
        });
        assertEquals("user is null", exception.getMessage());
        verify(userRepositoryImp).findByIdentity(userName);
        verifyNoInteractions(postingRepository);
    }

    @Test
    void testIsUpVoted_PostDoesNotExist() {
        // Arrange
        String userName = "testUser";
        long postId = 2L;
        User mockUser = User.builder()
                .userName("testUser")
                .build();

        Post mockPost = Post.builder()
                .id(1L)
                .build();

        when(userRepositoryImp.findByIdentity(userName)).thenReturn(mockUser);
        when(postingRepository.getPostByID(postId)).thenReturn(null);

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> {
            recommendationSystemService.isUpVoted(userName, postId);
        });
        assertEquals("post is null", exception.getMessage());
        verify(userRepositoryImp).findByIdentity(userName);
        verify(postingRepository).getPostByID(postId);
    }

    @Test
    void testIsDownVoted_UserAndPostExist() throws Exception {
        // Arrange
        String userName = "testUser";
        long postId = 1L;
        User mockUser = User.builder()
                .userName("testUser")
                .build();

        Post mockPost = Post.builder()
                .id(1L)
                .build();

        when(userRepositoryImp.findByIdentity(userName)).thenReturn(mockUser);
        when(postingRepository.getPostByID(postId)).thenReturn(mockPost);
        when(postingRepository.is_DownVoted(userName, postId)).thenReturn(false);

        // Act
        boolean result = recommendationSystemService.isDownVoted(userName, postId);

        // Assert
        assertFalse(result);
        verify(userRepositoryImp).findByIdentity(userName);
        verify(postingRepository).getPostByID(postId);
        verify(postingRepository).is_DownVoted(userName, postId);
    }

    @Test
    void testIsDownVoted_UserDoesNotExist() {
        // Arrange
        String userName = "nonExistentUser";
        long postId = 1L;
        User mockUser = User.builder()
                .userName("testUser")
                .build();

        Post mockPost = Post.builder()
                .id(1L)
                .build();

        when(userRepositoryImp.findByIdentity(userName)).thenReturn(null);

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> {
            recommendationSystemService.isDownVoted(userName, postId);
        });
        assertEquals("user is null", exception.getMessage());
        verify(userRepositoryImp).findByIdentity(userName);
        verifyNoInteractions(postingRepository);
    }

    @Test
    void testIsDownVoted_PostDoesNotExist() {
        // Arrange
        String userName = "testUser";
        long postId = 2L;
        User mockUser = User.builder()
                .userName("testUser")
                .build();

        Post mockPost = Post.builder()
                .id(1L)
                .build();

        when(userRepositoryImp.findByIdentity(userName)).thenReturn(mockUser);
        when(postingRepository.getPostByID(postId)).thenReturn(null);

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> {
            recommendationSystemService.isDownVoted(userName, postId);
        });
        assertEquals("post is null", exception.getMessage());
        verify(userRepositoryImp).findByIdentity(userName);
        verify(postingRepository).getPostByID(postId);
    }
}
