package com.example.BugByte_backend.repositories;

import com.example.BugByte_backend.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class userProfileRepository {
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
                SELECT COUNT(*)
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

    private static final String SQL_GET_POSITIVE_INTERACTIONS = """
                SELECT
                    COUNT(*)
                FROM
                    posts p JOIN upvotes uv ON p.id = uv.post_id
                WHERE
                    op_name = ?
            """;

    private static final String SQL_GET_NEGATIVE_INTERACTIONS = """
                SELECT
                    COUNT(*)
                FROM
                    posts p JOIN downvotes dv ON p.id = dv.post_id
                WHERE
                    op_name = ?
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
        Integer count = jdbcTemplate.queryForObject(SQL_IS_FOLLOWING, new Object[]{ userId, followingId }, Integer.class);
        if (count == null)
            throw new RuntimeException("Invalid Input");

        return count == 1;
    }

    public Boolean unfollowUser(Long userId, Long followingId) {
        if (userId == null || followingId == null)
            throw new NullPointerException("UserId or FollowingId is Null");

        int rows = jdbcTemplate.update(SQL_DELETE_FOLLOWER, userId, followingId);
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

    public Boolean updateBio(String newBio, Long userId) {
        if (userId == null)
            throw new NullPointerException("UserId is Null");

        if (newBio == null)
            throw new NullPointerException("Bio is Null");

        int row = jdbcTemplate.update(SQL_UPDATE_BIO, newBio, userId);
        return row > 0;
    }

    public Integer getFollowersCount(Long userId) {
        Integer count = jdbcTemplate.queryForObject(SQL_GET_FOLLOWERS_COUNT, new Object[]{ userId }, Integer.class);
        if (count == null)
            throw new RuntimeException("Invalid Input");

        return count;
    }

    public Integer getFollowingsCount(Long userId) {
        Integer count = jdbcTemplate.queryForObject(SQL_GET_FOLLOWINGS_COUNT, new Object[]{ userId }, Integer.class);
        if (count == null)
            throw new RuntimeException("Invalid Input");

        return count;
    }

    public Integer getReputation(String userName) {
        if (userName == null) {
            throw new IllegalArgumentException("Username cannot be null");
        }

        try {
            Integer positiveReputation = jdbcTemplate.queryForObject(
                    SQL_GET_POSITIVE_INTERACTIONS,
                    new Object[]{ userName },
                    Integer.class
            );

            Integer negativeReputation = jdbcTemplate.queryForObject(
                    SQL_GET_NEGATIVE_INTERACTIONS,
                    new Object[]{ userName },
                    Integer.class
            );

            Integer rep = positiveReputation - negativeReputation;

            System.out.println("Reputation is : " + rep);
            return  rep != null ? rep : 0;

        } catch (Exception e) {
            throw new NullPointerException("Cannot get the reputation");
        }
    }

    private final RowMapper<User> userRowMapper = ((rs, rowNum) -> User.builder()
            .id(rs.getLong("id"))
            .userName(rs.getString("user_name"))
            .email(rs.getString("email"))
            .password(rs.getString("password"))
            .bio(rs.getString("bio"))
            .reputation(rs.getLong("reputation"))
            .isAdmin(rs.getBoolean("is_admin"))
            .build()
    );
}
