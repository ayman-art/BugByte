package com.example.BugByte_backend.repositories;

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
            	WHERE p.id in (
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
            	ORDER BY (up_votes - down_votes) DESC
            	LIMIT 20
            )
            ORDER BY RAND();
            """;

    private static final Long GENERAL_COMMUNITY = 1L;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Question> generateFeedForUser(Long userId) {
        if (userId == null)
            throw new NullPointerException("User id can't be null!");

        return jdbcTemplate.query(SQL_GET_RECOMMENDED_QUESTIONS,
                new Object[] { userId, userId, GENERAL_COMMUNITY, GENERAL_COMMUNITY },
                questionRowMapper);
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
}
