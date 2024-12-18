package com.example.BugByte_backend.repositories;

import com.example.BugByte_backend.models.Question;
import com.example.BugByte_backend.repositories.RecommendationSystemRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

@SpringBootTest
public class RecommendationSystemRepositoryTest {

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

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private RecommendationSystemRepository recommendationSystemRepository;

    @Test
    public void testGenerateFeedForUser_withValidUserId() {
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
        Long userId = 1L;
        long generalCommunity = 1L;

        when(jdbcTemplate.query(eq(SQL_GET_RECOMMENDED_QUESTIONS),
                eq(new Object[] { userId, userId, generalCommunity, generalCommunity }),
                any(RowMapper.class))).thenReturn(mockFeed);

        List<Question> result = recommendationSystemRepository.generateFeedForUser(userId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(result, List.of(q1, q2));
    }

    @Test
    public void testGenerateFeedForUser_withEmptyResult() {
        List<Question> mockFeed = List.of();
        Long userId = 1L;
        long generalCommunity = 1L;

        when(jdbcTemplate.query(eq(SQL_GET_RECOMMENDED_QUESTIONS),
                eq(new Object[] { userId, userId, generalCommunity, generalCommunity }),
                any(RowMapper.class))).thenReturn(mockFeed);


        List<Question> result = recommendationSystemRepository.generateFeedForUser(userId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
