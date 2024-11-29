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
                SELECT follower_id, following_id FROM followers 
                WHERE follower_id = ? AND following_id = ?;
            """;

    private static final String SQL_DELETE_FOLLOWER = "DELETE FROM followers WHERE follower_id = ? AND followed_id = ?;";

    private static final String SQL_GET_FOLLOWERS = """
                SELECT u.* 
                FROM followers f
                JOIN users u ON f.follower_id = u.id
                WHERE f.followed_id = ?;
            """;

    private static final String SQL_GET_FOLLOWINGS = """
                SELECT u.* 
                FROM followers f
                JOIN users u ON f.followed_id = u.id
                WHERE f.follower_id = ?;
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

        testUser1 = new User(1L, "user1", "user1@test.com", "password", 100L, false);
        testUser2 = new User(2L, "user2", "user2@test.com", "password", 200L, false);
        testUser3 = new User(3L, "user3", "user3@test.com", "password", 300L, false);
        testUser4 = new User(4L, "user4", "user4@test.com", "password", 400L, false);
        testUser5 = new User(5L, "user5", "user5@test.com", "password", 500L, false);
        testUser6 = new User(6L, "user6", "user6@test.com", "password", 600L, false);
    }

    @Test
    void followUser_WhenNotAlreadyFollowing_ShouldReturnTrue() {
        when(jdbcTemplate.queryForObject(
                eq(SQL_IS_FOLLOWING),
                eq(new Object[]{testUser1.getId(), testUser2.getId()}),
                eq(Boolean.class)
        )).thenReturn(null);

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
                eq(testUser1.getId())
        )).thenReturn(1);

        Boolean result = repository.unfollowUser(testUser1.getId(), testUser2.getId());

        assertTrue(result);
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

        Boolean result = repository.followUser(userId, userId);

        assertFalse(result);
        verify(jdbcTemplate, never()).update(any(), any(), any());
    }

    @Test
    void followUser_WhenDatabaseError_ShouldReturnFalse() {
        when(jdbcTemplate.queryForObject(
                eq(SQL_IS_FOLLOWING),
                eq(new Object[]{testUser1.getId(), testUser2.getId()}),
                eq(Boolean.class)
        )).thenReturn(null);

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
                eq(Boolean.class)
        )).thenThrow(new RuntimeException("Database error"));

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
                eq(Boolean.class)
        )).thenReturn(null);

        when(jdbcTemplate.queryForObject(
                eq(SQL_IS_FOLLOWING),
                eq(new Object[]{testUser1.getId(), testUser3.getId()}),
                eq(Boolean.class)
        )).thenReturn(null);

        when(jdbcTemplate.queryForObject(
                eq(SQL_IS_FOLLOWING),
                eq(new Object[]{testUser1.getId(), testUser4.getId()}),
                eq(Boolean.class)
        )).thenReturn(null);

        when(jdbcTemplate.queryForObject(
                eq(SQL_IS_FOLLOWING),
                eq(new Object[]{testUser1.getId(), testUser5.getId()}),
                eq(Boolean.class)
        )).thenReturn(null);

        when(jdbcTemplate.queryForObject(
                eq(SQL_IS_FOLLOWING),
                eq(new Object[]{testUser1.getId(), testUser6.getId()}),
                eq(Boolean.class)
        )).thenReturn(null);

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
                eq(Boolean.class)
        )).thenReturn(null);

        when(jdbcTemplate.queryForObject(
                eq(SQL_IS_FOLLOWING),
                eq(new Object[]{testUser3.getId(), testUser1.getId()}),
                eq(Boolean.class)
        )).thenReturn(null);

        when(jdbcTemplate.queryForObject(
                eq(SQL_IS_FOLLOWING),
                eq(new Object[]{testUser4.getId(), testUser1.getId()}),
                eq(Boolean.class)
        )).thenReturn(null);

        when(jdbcTemplate.queryForObject(
                eq(SQL_IS_FOLLOWING),
                eq(new Object[]{testUser5.getId(), testUser1.getId()}),
                eq(Boolean.class)
        )).thenReturn(null);

        when(jdbcTemplate.queryForObject(
                eq(SQL_IS_FOLLOWING),
                eq(new Object[]{testUser6.getId(), testUser1.getId()}),
                eq(Boolean.class)
        )).thenReturn(null);

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
        User follower = new User(1L, "follower", "follower@test.com", "password", 100L, false);
        User followed = new User(2L, "followed", "followed@test.com", "password", 200L, false);

        Follower relationship = new Follower(follower, followed);

        assertEquals(follower, relationship.getFollower());
        assertEquals(followed, relationship.getFollowed());
    }
}
