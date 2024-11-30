package com.example.BugByte_backend.repositories;

import com.example.BugByte_backend.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class userProfileRepository {
    public boolean followUser(long userId , long followingId){
        return true;
    }
    public boolean isFollowing(long userId , long followingId){
        return false;
    }
    public boolean unfollowUser(long userId , long followingId){
        return true;
    }
    public List<User> getFollowings(long userId){
        return null;
    }
    public List<User> getFollowers(long userId){
        return null;
    }

    public int getFollowersCount(long userId){
        return 0;
    }
    public int getFollowingsCount(long userId){
        return 0;
    }

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

    @Autowired
    private JdbcTemplate jdbcTemplate;


    public Boolean followUser(Long userId, Long followingId) {
        if (userId == null || followingId == null)
            throw new NullPointerException("User or following id is null");

        Boolean alreadyFollowing = isFollowing(userId, followingId);

        if (alreadyFollowing)
            return true;

        int rows = jdbcTemplate.update(SQL_FOLLOW_USER, userId, followingId);

        return rows != 0;
    }

    public Boolean isFollowing(Long userId, Long followingId) {
        return  jdbcTemplate.queryForObject(
                SQL_IS_FOLLOWING,
                new Object[]{userId, followingId},
                (rs, rowNum) -> true) != null;
    }

    public Boolean unfollowUser(Long userId, Long followingId) {
        if (userId == null || followingId == null)
            throw new NullPointerException("UserId or FollowingId is Null");

        int rows = jdbcTemplate.update(SQL_DELETE_FOLLOWER, userId);
        return rows == 1;
    }

    public List<User> getFollowings(Long userId) {
        if (userId == null)
            throw new NullPointerException("UserId is Null");

        return jdbcTemplate.query(SQL_GET_FOLLOWINGS, userRowMapper, userId);
    }

    public List<User> getFollowers(Long userId) {
        if (userId == null)
            throw new NullPointerException("UserId is Null");

        return jdbcTemplate.query(SQL_GET_FOLLOWERS, userRowMapper, userId);
    }

    private final RowMapper<User> userRowMapper = ((rs, rowNum) -> new User(
            rs.getLong("id"),
            rs.getString("user_name"),
            rs.getString("email"),
            rs.getString("password"),
            rs.getLong("reputation"),
            rs.getBoolean("is_admin")
    ));

}
