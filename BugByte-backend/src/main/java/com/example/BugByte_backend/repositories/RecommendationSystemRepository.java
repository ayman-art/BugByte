package com.example.BugByte_backend.repositories;

import com.example.BugByte_backend.models.Community;
import com.example.BugByte_backend.models.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class RecommendationSystemRepository {
    private static final String SQL_GET_RECOMMENDED_QUESTIONS = """
            (
            	SELECT p.id, op_name, title, md_content, community_id, up_votes, down_votes, validated_answer_id, posted_on
            	FROM posts p
            	JOIN questions q ON q.id = p.id
            	JOIN users u ON op_name = u.user_name
              	WHERE u.id in (
              		SELECT followed_id FROM followers WHERE follower_id = ?
              	)
            	AND posted_on >= NOW() - INTERVAL 1 DAY
            	ORDER BY RAND()
            	LIMIT 30
            )
            UNION ALL
            (
            	SELECT p.id, op_name, title, md_content, community_id, up_votes, down_votes, validated_answer_id, posted_on
            	FROM posts p
            	JOIN questions q ON q.id = p.id
            	WHERE q.community_id in (
            		SELECT community_id FROM community_members WHERE member_id = ?
            	)
            	AND q.community_id != ?
            	AND posted_on >= NOW() - INTERVAL 1 DAY
            	ORDER BY RAND()
            	LIMIT 50
            )
            UNION ALL
            (
            	SELECT p.id, op_name, title, md_content, community_id, up_votes, down_votes, validated_answer_id, posted_on
            	FROM posts p
            	JOIN questions q ON q.id = p.id
            	AND q.community_id = ?
            	AND posted_on >= NOW() - INTERVAL 1 DAY
            	JOIN users u ON op_name = u.user_name
                AND u.id <> ?
            	ORDER BY (up_votes - down_votes) DESC
            	LIMIT 20
            )
            ORDER BY RAND();
            """;
    private static final String SQL_GET_RECOMMENDED_COMMUNITIES = """
            (
                SELECT c.id, c.name, c.description
                FROM communities c
                JOIN community_members cm ON c.id = cm.community_id
                WHERE cm.member_id IN (
                    SELECT followed_id
                    FROM followers
                    WHERE follower_id = ?
                )
                AND c.id NOT IN (
                    SELECT community_id
                    FROM community_members
                    WHERE member_id = ?
                )
                LIMIT 10
            )
            UNION
            (
                SELECT c.id, c.name, c.description
                FROM communities c
                LEFT JOIN (
                    SELECT community_id, COUNT(*) AS engagement_score
                    FROM community_members
                    GROUP BY community_id
                ) e ON c.id = e.community_id
                WHERE c.id NOT IN (
                    SELECT community_id
                    FROM community_members
                    WHERE member_id = ?
                )
                ORDER BY e.engagement_score DESC
                LIMIT 10
            );
            """;
    private static final Long GENERAL_COMMUNITY = 1L;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Question> generateFeedForUser(Long userId) {
        if (userId == null)
            throw new IllegalArgumentException("User id can't be null!");

        return jdbcTemplate.query(SQL_GET_RECOMMENDED_QUESTIONS,
                new Object[] { userId, userId, GENERAL_COMMUNITY, GENERAL_COMMUNITY, userId },
                questionRowMapper);
    }

    public List<Community> generateRecommendedCommunitiesForUser(Long userId) {
        if (userId == null)
            throw new IllegalArgumentException("User id can't be null!");

        return jdbcTemplate.query(SQL_GET_RECOMMENDED_COMMUNITIES,
                new Object[]{ userId, userId, userId },
                communityRowMapper);
    }

    private final RowMapper<Question> questionRowMapper = ((rs, rowNum) ->
            Question.builder()
                    .id(rs.getLong("id"))
                    .creatorUserName(rs.getString("op_name"))
                    .mdContent(rs.getString("md_content"))
                    .postedOn(new Date(rs.getDate("posted_on").getTime()))
                    .title(rs.getString("title"))
                    .communityId(rs.getLong("community_id"))
                    .upVotes(rs.getLong("up_votes"))
                    .downVotes(rs.getLong("down_votes"))
                    .validatedAnswerId(rs.getLong("validated_answer_id"))
                    .build()
    );

    private final RowMapper<Community> communityRowMapper = ((rs, rowNum) -> Community.builder()
            .id(rs.getLong("id"))
            .name(rs.getString("name"))
            .description(rs.getString("description"))
            .adminId(rs.getLong("admin_id"))
            .creationDate(rs.getDate("creation_date"))
            .build()
    );
}
