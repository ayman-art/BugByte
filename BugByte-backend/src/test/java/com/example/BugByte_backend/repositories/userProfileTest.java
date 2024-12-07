package com.example.BugByte_backend.repositories;

import com.example.BugByte_backend.models.Follower;
import com.example.BugByte_backend.models.FollowerId;
import com.example.BugByte_backend.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class UserProfileRepositoryTest {

    private static final String SQL_FOLLOW_USER = """
                INSERT INTO followers
                    (follower_id, followed_id)
                VALUES
                    (?, ?);
            """;

    private static final String SQL_IS_FOLLOWING = """
                SELECT COUNT(*) FROM followers
                WHERE follower_id = ? AND followed_id = ?;
            """;

    private static final String SQL_DELETE_FOLLOWER = "DELETE FROM followers WHERE follower_id = ? AND followed_id = ?;";
    private static final String SQL_GET_FOLLOWINGS_COUNT = """
                SELECT COUNT(*)
                FROM followers f
                JOIN users u ON f.followed_id = u.id
                WHERE f.follower_id = ?;
            """;
    private static final String SQL_GET_FOLLOWERS_COUNT = """
                SELECT COUNT(* )
                FROM followers f
                JOIN users u ON f.follower_id = u.id
                WHERE f.followed_id = ?;
            """;
    private static final String SQL_GET_FOLLOWERS = """
                SELECT * 
                FROM followers f
                JOIN users u ON f.follower_id = u.id
                WHERE f.followed_id = ?;
            """;

    private static final String SQL_GET_FOLLOWINGS = """
                SELECT * 
                FROM followers f
                JOIN users u ON f.followed_id = u.id
                WHERE f.follower_id = ?;
            """;

    private static final String SQL_UPDATE_BIO = """
                UPDATE users 
                SET bio = ? 
                WHERE id = ?;
            """;


    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private userProfileRepository repository;

    private User testUser1;
    private User testUser2;

    private User testUser3;
    private User testUser4;

    private User testUser5;
    private User testUser6;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testUser1 = User.builder()
                .id(1L)
                .userName("user1")
                .email("user1@test.com")
                .password("password")
                .reputation(100L)
                .isAdmin(false)
                .build();

        testUser2 = User.builder()
                .id(2L)
                .userName("user2")
                .email("user2@test.com")
                .password("password")
                .reputation(200L)
                .isAdmin(false)
                .build();

        testUser3 = User.builder()
                .id(3L)
                .userName("user3")
                .email("user3@test.com")
                .password("password")
                .reputation(300L)
                .isAdmin(false)
                .build();

        testUser4 = User.builder()
                .id(4L)
                .userName("user4")
                .email("user4@test.com")
                .password("password")
                .reputation(400L)
                .isAdmin(false)
                .build();

        testUser5 = User.builder()
                .id(5L)
                .userName("user5")
                .email("user5@test.com")
                .password("password")
                .reputation(500L)
                .isAdmin(false)
                .build();

        testUser6 = User.builder()
                .id(6L)
                .userName("user6")
                .email("user6@test.com")
                .password("password")
                .reputation(600L)
                .isAdmin(false)
                .build();
    }

    @Test
    void followUser_WhenNotAlreadyFollowing_ShouldReturnTrue() {
        when(jdbcTemplate.queryForObject(
                eq(SQL_IS_FOLLOWING),
                eq(new Object[]{testUser1.getId(), testUser2.getId()}),
                eq(Integer.class)
        )).thenReturn(0);

        when(jdbcTemplate.update(
                eq(SQL_FOLLOW_USER),
                eq(testUser1.getId()),
                eq(testUser2.getId())
        )).thenReturn(1);

        Boolean result = repository.followUser(testUser1.getId(), testUser2.getId());
        assertTrue(result);

        verify(jdbcTemplate).update(
                eq(SQL_FOLLOW_USER),
                eq(testUser1.getId()),
                eq(testUser2.getId())
        );
    }

    @Test
    void unfollowUser_ShouldReturnTrue_WhenSuccessful() {
        when(jdbcTemplate.update(
                eq(SQL_DELETE_FOLLOWER),
                eq(testUser1.getId()), eq(testUser2.getId())
        )).thenReturn(1);

        Boolean result = repository.unfollowUser(testUser1.getId(), testUser2.getId());

        assertTrue(result);
    }
    @Test
    void testGetFollowersCount_Success() {
        // Mock JdbcTemplate

        // Sample data
        long userId = 1L;
        int followersCount = 5;

        // Mock behavior
        when(jdbcTemplate.queryForObject(SQL_GET_FOLLOWERS_COUNT, new Object[]{userId}, Integer.class))
                .thenReturn(followersCount);

        // Call the method
        int result = repository.getFollowersCount(userId);

        // Validate results
        assertEquals(followersCount, result);

        // Verify interaction
        verify(jdbcTemplate, times(1))
                .queryForObject(SQL_GET_FOLLOWERS_COUNT, new Object[]{userId}, Integer.class);
    }

    @Test
    void testGetFollowersCount_InvalidInput() {
        // Sample data
        long userId = 1L;

        // Mock behavior
        when(jdbcTemplate.queryForObject(SQL_GET_FOLLOWERS_COUNT, new Object[]{userId}, Integer.class))
                .thenReturn(null);


        Exception exception = assertThrows(RuntimeException.class, () -> {
            repository.getFollowersCount(userId);
        });

        assertEquals("Invalid Input", exception.getMessage());

        // Verify interaction
        verify(jdbcTemplate, times(1))
                .queryForObject(SQL_GET_FOLLOWERS_COUNT, new Object[]{userId}, Integer.class);
    }

    @Test
    void testGetFollowingsCount_Success() {
        // Sample data
        long userId = 1L;
        int followingsCount = 3;

        // Mock behavior
        when(jdbcTemplate.queryForObject(SQL_GET_FOLLOWINGS_COUNT, new Object[]{userId}, Integer.class))
                .thenReturn(followingsCount);

        int result = repository.getFollowingsCount(userId);

        assertEquals(followingsCount, result);

        verify(jdbcTemplate, times(1))
                .queryForObject(SQL_GET_FOLLOWINGS_COUNT, new Object[]{userId}, Integer.class);
    }

    @Test
    void testGetFollowingsCount_InvalidInput() {
        // Sample data
        long userId = 1L;

        // Mock behavior
        when(jdbcTemplate.queryForObject(SQL_GET_FOLLOWINGS_COUNT, new Object[]{userId}, Integer.class))
                .thenReturn(null);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            repository.getFollowingsCount(userId);
        });

        assertEquals("Invalid Input", exception.getMessage());

        // Verify interaction
        verify(jdbcTemplate, times(1))
                .queryForObject(SQL_GET_FOLLOWINGS_COUNT, new Object[]{userId}, Integer.class);
    }

    @Test
    void unfollowUser_ShouldThrowException_WhenNullParameters() {
        assertThrows(NullPointerException.class, () -> {
            repository.unfollowUser(null, testUser2.getId());
        });
    }

    @Test
    void getFollowers_ShouldReturnList() {
        List<User> expectedFollowers = Arrays.asList(testUser1, testUser2);
        when(jdbcTemplate.query(
                eq(SQL_GET_FOLLOWERS),
                any(RowMapper.class),
                eq(testUser1.getId())
        )).thenReturn(expectedFollowers);

        List<User> result = repository.getFollowers(testUser1.getId());

        assertEquals(expectedFollowers, result);
    }

    @Test
    void getFollowings_ShouldReturnList() {
        List<User> expectedFollowings = Arrays.asList(testUser1, testUser2);
        when(jdbcTemplate.query(
                eq(SQL_GET_FOLLOWINGS),
                any(RowMapper.class),
                eq(testUser1.getId())
        )).thenReturn(expectedFollowings);

        List<User> result = repository.getFollowings(testUser1.getId());

        assertEquals(expectedFollowings, result);
    }

    @Test
    void followUser_WhenUserTriesToFollowThemself_ShouldReturnFalse() {
        Long userId = testUser1.getId();

        when(jdbcTemplate.queryForObject(
                eq(SQL_IS_FOLLOWING),
                eq(new Object[]{ userId, userId }),
                eq(Integer.class)
        )).thenReturn(0);

        Boolean result = repository.followUser(userId, userId);

        assertFalse(result);
        verify(jdbcTemplate, never()).update(any(), any(), any());
    }

    @Test
    void followUser_WhenDatabaseError_ShouldReturnFalse() {
        when(jdbcTemplate.queryForObject(
                eq(SQL_IS_FOLLOWING),
                eq(new Object[]{testUser1.getId(), testUser2.getId()}),
                eq(Integer.class)
        )).thenReturn(0);

        when(jdbcTemplate.update(
                eq(SQL_FOLLOW_USER),
                eq(testUser1.getId()),
                eq(testUser2.getId())
        )).thenReturn(0);

        Boolean result = repository.followUser(testUser1.getId(), testUser2.getId());

        assertFalse(result);
    }

    @Test
    void followUser_WhenNullUserId_ShouldThrowException() {
        assertThrows(NullPointerException.class, () -> {
            repository.followUser(null, testUser2.getId());
        });
    }

    @Test
    void isFollowing_WhenDatabaseError_ShouldReturnFalse() {
        when(jdbcTemplate.queryForObject(
                eq(SQL_IS_FOLLOWING),
                eq(new Object[]{testUser1.getId(), testUser2.getId()}),
                eq(Integer.class)
        )).thenReturn(0);

        Boolean result = repository.isFollowing(testUser1.getId(), testUser2.getId());

        assertFalse(result);
    }

    @Test
    void getFollowers_WhenUserHasNoFollowers_ShouldReturnEmptyList() {
        when(jdbcTemplate.query(
                eq(SQL_GET_FOLLOWERS),
                any(RowMapper.class),
                eq(testUser1.getId())
        )).thenReturn(List.of());

        List<User> result = repository.getFollowers(testUser1.getId());

        assertTrue(result.isEmpty());
    }

    @Test
    void getFollowers_WhenDatabaseError_ShouldThrowException() {
        when(jdbcTemplate.query(
                eq(SQL_GET_FOLLOWERS),
                any(RowMapper.class),
                eq(testUser1.getId())
        )).thenThrow(new RuntimeException("Database error"));

        assertThrows(RuntimeException.class, () -> {
            repository.getFollowers(testUser1.getId());
        });
    }

    @Test
    void unfollowUser_WhenNotFollowing_ShouldReturnFalse() {
        when(jdbcTemplate.update(
                eq(SQL_DELETE_FOLLOWER),
                eq(testUser1.getId()),
                eq(testUser2.getId())
        )).thenReturn(0);

        Boolean result = repository.unfollowUser(testUser1.getId(), testUser2.getId());

        assertFalse(result);
    }

    @Test
    void getFollowings_WhenUserFollowsNoOne_ShouldReturnEmptyList() {
        when(jdbcTemplate.query(
                eq(SQL_GET_FOLLOWINGS),
                any(RowMapper.class),
                eq(testUser1.getId())
        )).thenReturn(List.of());

        List<User> result = repository.getFollowings(testUser1.getId());

        assertTrue(result.isEmpty());
        verify(jdbcTemplate).query(
                eq(SQL_GET_FOLLOWINGS),
                any(RowMapper.class),
                eq(testUser1.getId())
        );
    }


    @Test
    void followMultipleUsers_ShouldReturnTrue_WhenSuccessful() {
        when(jdbcTemplate.queryForObject(
                eq(SQL_IS_FOLLOWING),
                eq(new Object[]{testUser1.getId(), testUser2.getId()}),
                eq(Integer.class)
        )).thenReturn(0);

        when(jdbcTemplate.queryForObject(
                eq(SQL_IS_FOLLOWING),
                eq(new Object[]{testUser1.getId(), testUser3.getId()}),
                eq(Integer.class)
        )).thenReturn(0);

        when(jdbcTemplate.queryForObject(
                eq(SQL_IS_FOLLOWING),
                eq(new Object[]{testUser1.getId(), testUser4.getId()}),
                eq(Integer.class)
        )).thenReturn(0);

        when(jdbcTemplate.queryForObject(
                eq(SQL_IS_FOLLOWING),
                eq(new Object[]{testUser1.getId(), testUser5.getId()}),
                eq(Integer.class)
        )).thenReturn(0);

        when(jdbcTemplate.queryForObject(
                eq(SQL_IS_FOLLOWING),
                eq(new Object[]{testUser1.getId(), testUser6.getId()}),
                eq(Integer.class)
        )).thenReturn(0);

        when(jdbcTemplate.update(
                eq(SQL_FOLLOW_USER),
                eq(testUser1.getId()),
                eq(testUser2.getId())
        )).thenReturn(1);

        when(jdbcTemplate.update(
                eq(SQL_FOLLOW_USER),
                eq(testUser1.getId()),
                eq(testUser3.getId())
        )).thenReturn(1);

        when(jdbcTemplate.update(
                eq(SQL_FOLLOW_USER),
                eq(testUser1.getId()),
                eq(testUser4.getId())
        )).thenReturn(1);

        when(jdbcTemplate.update(
                eq(SQL_FOLLOW_USER),
                eq(testUser1.getId()),
                eq(testUser5.getId())
        )).thenReturn(1);

        when(jdbcTemplate.update(
                eq(SQL_FOLLOW_USER),
                eq(testUser1.getId()),
                eq(testUser6.getId())
        )).thenReturn(1);

        Boolean result1 = repository.followUser(testUser1.getId(), testUser2.getId());
        Boolean result2 = repository.followUser(testUser1.getId(), testUser3.getId());
        Boolean result3 = repository.followUser(testUser1.getId(), testUser4.getId());
        Boolean result4 = repository.followUser(testUser1.getId(), testUser5.getId());
        Boolean result5 = repository.followUser(testUser1.getId(), testUser6.getId());

        assertTrue(result1);
        assertTrue(result2);
        assertTrue(result3);
        assertTrue(result4);
        assertTrue(result5);

        verify(jdbcTemplate).update(
                eq(SQL_FOLLOW_USER),
                eq(testUser1.getId()),
                eq(testUser2.getId())
        );
        verify(jdbcTemplate).update(
                eq(SQL_FOLLOW_USER),
                eq(testUser1.getId()),
                eq(testUser3.getId())
        );
        verify(jdbcTemplate).update(
                eq(SQL_FOLLOW_USER),
                eq(testUser1.getId()),
                eq(testUser4.getId())
        );
        verify(jdbcTemplate).update(
                eq(SQL_FOLLOW_USER),
                eq(testUser1.getId()),
                eq(testUser5.getId())
        );
        verify(jdbcTemplate).update(
                eq(SQL_FOLLOW_USER),
                eq(testUser1.getId()),
                eq(testUser6.getId())
        );
    }

    @Test
    void getFollowings_ShouldReturnMultipleUsers() {
        List<User> expectedFollowings = Arrays.asList(testUser2, testUser3, testUser4, testUser5, testUser6);

        when(jdbcTemplate.query(
                eq(SQL_GET_FOLLOWINGS),
                any(RowMapper.class),
                eq(testUser1.getId())
        )).thenReturn(expectedFollowings);

        List<User> result = repository.getFollowings(testUser1.getId());

        assertEquals(expectedFollowings, result);
    }

    @Test
    void addMultipleFollowers_ShouldReturnTrue_WhenSuccessful() {

        when(jdbcTemplate.queryForObject(
                eq(SQL_IS_FOLLOWING),
                eq(new Object[]{testUser2.getId(), testUser1.getId()}),
                eq(Integer.class)
        )).thenReturn(0);

        when(jdbcTemplate.queryForObject(
                eq(SQL_IS_FOLLOWING),
                eq(new Object[]{testUser3.getId(), testUser1.getId()}),
                eq(Integer.class)
        )).thenReturn(0);

        when(jdbcTemplate.queryForObject(
                eq(SQL_IS_FOLLOWING),
                eq(new Object[]{testUser4.getId(), testUser1.getId()}),
                eq(Integer.class)
        )).thenReturn(0);

        when(jdbcTemplate.queryForObject(
                eq(SQL_IS_FOLLOWING),
                eq(new Object[]{testUser5.getId(), testUser1.getId()}),
                eq(Integer.class)
        )).thenReturn(0);

        when(jdbcTemplate.queryForObject(
                eq(SQL_IS_FOLLOWING),
                eq(new Object[]{testUser6.getId(), testUser1.getId()}),
                eq(Integer.class)
        )).thenReturn(0);

        // Simulate the following action for each follower
        when(jdbcTemplate.update(
                eq(SQL_FOLLOW_USER),
                eq(testUser2.getId()),
                eq(testUser1.getId())
        )).thenReturn(1);

        when(jdbcTemplate.update(
                eq(SQL_FOLLOW_USER),
                eq(testUser3.getId()),
                eq(testUser1.getId())
        )).thenReturn(1);

        when(jdbcTemplate.update(
                eq(SQL_FOLLOW_USER),
                eq(testUser4.getId()),
                eq(testUser1.getId())
        )).thenReturn(1);

        when(jdbcTemplate.update(
                eq(SQL_FOLLOW_USER),
                eq(testUser5.getId()),
                eq(testUser1.getId())
        )).thenReturn(1);

        when(jdbcTemplate.update(
                eq(SQL_FOLLOW_USER),
                eq(testUser6.getId()),
                eq(testUser1.getId())
        )).thenReturn(1);

        Boolean result1 = repository.followUser(testUser2.getId(), testUser1.getId());
        Boolean result2 = repository.followUser(testUser3.getId(), testUser1.getId());
        Boolean result3 = repository.followUser(testUser4.getId(), testUser1.getId());
        Boolean result4 = repository.followUser(testUser5.getId(), testUser1.getId());
        Boolean result5 = repository.followUser(testUser6.getId(), testUser1.getId());

        assertTrue(result1);
        assertTrue(result2);
        assertTrue(result3);
        assertTrue(result4);
        assertTrue(result5);

        verify(jdbcTemplate).update(
                eq(SQL_FOLLOW_USER),
                eq(testUser2.getId()),
                eq(testUser1.getId())
        );
        verify(jdbcTemplate).update(
                eq(SQL_FOLLOW_USER),
                eq(testUser3.getId()),
                eq(testUser1.getId())
        );
        verify(jdbcTemplate).update(
                eq(SQL_FOLLOW_USER),
                eq(testUser4.getId()),
                eq(testUser1.getId())
        );
        verify(jdbcTemplate).update(
                eq(SQL_FOLLOW_USER),
                eq(testUser5.getId()),
                eq(testUser1.getId())
        );
        verify(jdbcTemplate).update(
                eq(SQL_FOLLOW_USER),
                eq(testUser6.getId()),
                eq(testUser1.getId())
        );
    }

    @Test
    void getFollowers_ShouldReturnMultipleUsers() {
        List<User> expectedFollowers = Arrays.asList(testUser2, testUser3, testUser4, testUser5, testUser6);

        when(jdbcTemplate.query(
                eq(SQL_GET_FOLLOWERS),
                any(RowMapper.class),
                eq(testUser1.getId())
        )).thenReturn(expectedFollowers);

        List<User> result = repository.getFollowers(testUser1.getId());

        assertEquals(expectedFollowers, result);
    }

    @Test
    void updateBio_WhenSuccessful_ShouldReturnTrue() {
        String newBio = "Updated bio";
        Long userId = testUser1.getId();

        when(jdbcTemplate.update(
                eq(SQL_UPDATE_BIO),
                eq(newBio),
                eq(userId)
        )).thenReturn(1);

        boolean result = repository.updateBio(newBio, userId);

        assertTrue(result);
        verify(jdbcTemplate).update(
                eq(SQL_UPDATE_BIO),
                eq(newBio),
                eq(userId)
        );
    }

    @Test
    void updateBio_WhenNoRowsAffected_ShouldReturnFalse() {
        String newBio = "Updated bio";
        Long userId = testUser1.getId();

        when(jdbcTemplate.update(
                eq(SQL_UPDATE_BIO),
                eq(newBio),
                eq(userId)
        )).thenReturn(0);

        boolean result = repository.updateBio(newBio, userId);

        assertFalse(result);
    }

    @Test
    void updateBio_WhenUserIdIsNull_ShouldThrowException() {
        String newBio = "Updated bio";

        assertThrows(NullPointerException.class, () -> {
            repository.updateBio(newBio, null);
        });
    }

    @Test
    void updateBio_WhenBioIsNull_ShouldThrowException() {
        Long userId = testUser1.getId();

        assertThrows(NullPointerException.class, () -> {
            repository.updateBio(null, userId);
        });
    }

}

class FollowerIdTest {

    @Test
    void equals_ShouldReturnTrue_WhenSameContent() {
        FollowerId id1 = new FollowerId(1L, 2L);
        FollowerId id2 = new FollowerId(1L, 2L);

        assertEquals(id1, id2);
        assertEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    void equals_ShouldReturnFalse_WhenDifferentContent() {
        FollowerId id1 = new FollowerId(1L, 2L);
        FollowerId id2 = new FollowerId(2L, 1L);

        assertNotEquals(id1, id2);
        assertNotEquals(id1.hashCode(), id2.hashCode());
    }
}

class FollowerTest {

    @Test
    void constructor_ShouldSetFields() {
        User follower = User.builder()
                .id(1L)
                .userName("follower")
                .email("follower@test.com")
                .password("password")
                .reputation(100L)
                .isAdmin(false)
                .build();

        User followed = User.builder()
                .id(2L)
                .userName("followed")
                .email("followed@test.com")
                .password("password")
                .reputation(200L)
                .isAdmin(false)
                .build();

        Follower relationship = new Follower(null, follower, followed);

        assertEquals(follower, relationship.getFollower());
        assertEquals(followed, relationship.getFollowed());
    }
}
