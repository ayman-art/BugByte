package com.example.BugByte_backend.repositories;

import com.example.BugByte_backend.models.Community;
import com.example.BugByte_backend.models.Question;
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
                SELECT c.*
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
                SELECT c.*
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

        when(jdbcTemplate.query(anyString(),
                eq(new Object[] { userId, userId, generalCommunity, generalCommunity, userId }),
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

    @Test
    public void testGeneratedFeedForUser_withNullUserId() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            recommendationSystemRepository.generateRecommendedCommunitiesForUser(null);
        });

        assertEquals("User id can't be null!", exception.getMessage());
    }

    @Test
    public void testGenerateCommunitiesForUser_withValidUserId() {
        List<String> tags = List.of("java", "python");
        Community c1 = Community.builder()
                .id(1L)
                .name("Community 1")
                .description("Description 1")
                .adminId(2L)
                .tags(tags)
                .build();
        Community c2 = Community.builder()
                .id(2L)
                .name("Community 2")
                .description("Description 2")
                .adminId(3L)
                .tags(tags)
                .build();
        List<Community> mockCommunities = List.of(c1, c2);
        Long userId = 1L;

        when(jdbcTemplate.query(eq(SQL_GET_RECOMMENDED_COMMUNITIES),
                eq(new Object[] { userId, userId, userId }),
                any(RowMapper.class))).thenReturn(mockCommunities);

        List<Community> result = recommendationSystemRepository.generateRecommendedCommunitiesForUser(userId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(result, List.of(c1, c2));
    }

    @Test
    public void testGenerateCommunityForUser_withEmptyResult() {
        List<Community> mockCommunities = List.of();
        Long userId = 1L;

        when(jdbcTemplate.query(eq(SQL_GET_RECOMMENDED_COMMUNITIES),
                eq(new Object[] { userId, userId, userId }),
                any(RowMapper.class))).thenReturn(mockCommunities);


        List<Community> result = recommendationSystemRepository.generateRecommendedCommunitiesForUser(userId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGeneratedCommunityForUser_withNullUserId() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            recommendationSystemRepository.generateRecommendedCommunitiesForUser(null);
        });

        assertEquals("User id can't be null!", exception.getMessage());
    }
}
