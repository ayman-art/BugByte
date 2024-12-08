package com.example.BugByte_backend.repositories;
import com.example.BugByte_backend.models.Post;
import com.example.BugByte_backend.models.Question;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
@SpringBootTest
public class PostingRepositoryQuestionTest {
    private static final String SQL_INSERT_QUESTION = """
                INSERT INTO questions
                    (id, community_id, up_votes, down_votes)
                VALUES
                    (?, ?, 0, 0);
            """;
    private static final String SQL_DELETE_QUESTION_BY_ID = "DELETE FROM questions WHERE id = ?;";
    private static final String SQL_UPDATE_UP_VOTES_QUESTIONS = """
                UPDATE questions
                SET up_votes = up_votes + ?
                WHERE id = ?;
            """;
    private static final String SQL_UPDATE_DOWN_VOTES_QUESTIONS = """
                UPDATE questions
                SET down_votes = down_votes + ?
                WHERE id = ?;
            """;
    private static final String SQL_VERIFY_ANSWER = """
                UPDATE questions
                SET validated_answer = ?
                WHERE id = ?;
            """;
    private static final String SQL_GET_QUESTIONS_BY_USERNAME = """
                SELECT *
                FROM questions q
                JOIN posts p on p.id = q.id
                WHERE p.op_name = ?
                ORDER BY p.posted_on DESC
                LIMIT ?
                OFFSET ?;
            """;
    private static final String SQL_GET_QUESTIONS_BY_COMMUNITY = """
                SELECT *
                FROM questions q
                JOIN posts p on p.id = q.id
                WHERE q.community_id = ?
                ORDER BY p.posted_on DESC
                LIMIT ?
                OFFSET ?;
            """;
    private static final String SQL_GET_QUESTION_BY_ID = """
                SELECT *
                FROM questions q
                JOIN posts p on p.id = q.id
                WHERE q.id = ?;
            """;
    private static final String SQL_DELETE_ANSWERS_BY_QUESTION_ID = "DELETE FROM answers WHERE question_id = ?;";
    private static final String SQL_DELETE_POST_BY_ID = "DELETE FROM posts WHERE id = ?;";


    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private PostingRepository postingRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public void testInsertQuestion_validInput() {
        Long questionId = 1L;
        Long communityId = 1L;
        when(jdbcTemplate.update(eq(SQL_INSERT_QUESTION), eq(questionId), eq(communityId)))
                .thenReturn(1);

        Boolean result = postingRepository.insertQuestion(questionId, communityId);
        assertTrue(result);
    }

    @Test
    public void testInsertQuestion_nullInput() {
        Exception exception = null;

        try {
            postingRepository.insertQuestion(null, 1L);
        } catch (Exception e) {
            exception = e;
        }
        assertEquals(NullPointerException.class, exception.getClass());
        assertEquals("question id or community id is null", exception.getMessage());
    }
    @Test
    public void testDeleteQuestion_validInput() {
        Long questionId = 1L;
        when(jdbcTemplate.update(eq(SQL_DELETE_ANSWERS_BY_QUESTION_ID), eq(questionId)))
                .thenReturn(1);
        when(jdbcTemplate.update(eq(SQL_DELETE_QUESTION_BY_ID), eq(questionId)))
                .thenReturn(1);
        when(jdbcTemplate.update(eq(SQL_DELETE_POST_BY_ID), eq(questionId)))
                .thenReturn(1);

        Boolean result = postingRepository.deleteQuestion(questionId);
        assertTrue(result);
    }

    @Test
    public void testDeleteQuestion_nullInput() {
        Exception exception = null;
        try {
            postingRepository.deleteQuestion(null);
        } catch (Exception e) {
            exception = e;
        }
        assertEquals(NullPointerException.class, exception.getClass());
        assertEquals("question id is null", exception.getMessage());
    }
    @Test
    public void testUpVoteQuestion_validInput() {
        Long questionId = 1L;
        Integer value = 1;
        when(jdbcTemplate.update(eq(SQL_UPDATE_UP_VOTES_QUESTIONS), eq(value), eq(questionId)))
                .thenReturn(1);

        Boolean result = postingRepository.upVoteQuestion(questionId, value);
        assertTrue(result);
    }

    @Test
    public void testUpVoteQuestion_nullInput() {
        Exception exception = null;
        try {
            postingRepository.upVoteQuestion(null, 1);
        } catch (Exception e) {
            exception = e;
        }
        assertEquals(NullPointerException.class, exception.getClass());
        assertEquals("question id or value is null", exception.getMessage());
    }
    @Test
    public void testDownVoteQuestion_validInput() {
        Long questionId = 1L;
        Integer value = -1;

        when(jdbcTemplate.update(eq(SQL_UPDATE_DOWN_VOTES_QUESTIONS), eq(value), eq(questionId)))
                .thenReturn(1);

        Boolean result = postingRepository.downVoteQuestion(questionId, value);
        assertTrue(result);
    }

    @Test
    public void testDownVoteQuestion_nullInput() {
        Exception exception = null;

        try {
            postingRepository.downVoteQuestion(null, -1);
        } catch (Exception e) {
            exception = e;
        }
        assertEquals(NullPointerException.class, exception.getClass());
        assertEquals("question id or value is null", exception.getMessage());
    }
    @Test
    public void testVerifyAnswer_validInput() {
        Long answerId = 1L;
        when(jdbcTemplate.update(eq(SQL_VERIFY_ANSWER), eq(answerId)))
                .thenReturn(1);

        Boolean result = postingRepository.verifyAnswer(answerId);
        assertTrue(result);
    }

    @Test
    public void testVerifyAnswer_nullInput() {
        Exception exception = null;
        try {
            postingRepository.verifyAnswer(null);
        } catch (Exception e) {
            exception = e;
        }
        assertEquals(NullPointerException.class, exception.getClass());
        assertEquals("answer id is null", exception.getMessage());
    }
    @Test
    public void testGetQuestionsByUserName_validInput() {
        String userName = "user1";
        Integer limit = 10;
        Integer offset = 0;
        List<Question> expectedQuestions = new ArrayList<>();

        when(jdbcTemplate.query(eq(SQL_GET_QUESTIONS_BY_USERNAME), eq(new Object[]{userName, limit, offset}), any(RowMapper.class)))
                .thenReturn(expectedQuestions);

        List<Question> result = postingRepository.getQuestionsByUserName(userName, limit, offset);

        assertEquals(expectedQuestions, result);
    }

    @Test
    public void testGetQuestionsByUserName_nullUserName() {
        Exception exception = null;

        try {
            postingRepository.getQuestionsByUserName(null, 10, 0);
        } catch (Exception e) {
            exception = e;
        }
        assertEquals(NullPointerException.class, exception.getClass());
        assertEquals("username is null", exception.getMessage());
    }

    @Test
    public void testGetQuestionsByCommunity_validInput() {
        Long communityId = 1L;
        Integer limit = 10;
        Integer offset = 0;
        List<Question> expectedQuestions = new ArrayList<>();
        when(jdbcTemplate.query(eq(SQL_GET_QUESTIONS_BY_COMMUNITY), eq(new Object[]{communityId, limit, offset}), any(RowMapper.class)))
                .thenReturn(expectedQuestions);

        List<Question> result = postingRepository.getQuestionsByCommunity(communityId, limit, offset);
        assertEquals(expectedQuestions, result);
    }

    @Test
    public void testGetQuestionsByCommunity_nullCommunityId() {
        Exception exception = null;
        try {
            postingRepository.getQuestionsByCommunity(null, 10, 0);
        } catch (Exception e) {
            exception = e;
        }
        assertEquals(NullPointerException.class, exception.getClass());
        assertEquals("username is null", exception.getMessage());
    }
    @Test
    public void testGetQuestionById_validInput() {
        Long questionId = 1L;
        Question expectedQuestion = new Question();
        expectedQuestion.setPostId(questionId);
        when(jdbcTemplate.queryForObject(eq(SQL_GET_QUESTION_BY_ID), eq(new Object[]{questionId}), any(RowMapper.class)))
                .thenReturn(expectedQuestion);

        Question result = postingRepository.getQuestionById(questionId);
        assertEquals(expectedQuestion, result);
    }

    @Test
    public void testGetQuestionById_nullQuestionId() {
        Exception exception = null;
        try {
            postingRepository.getQuestionById(null);
        } catch (Exception e) {
            exception = e;
        }
        assertEquals(NullPointerException.class, exception.getClass());
        assertEquals("questionId is null", exception.getMessage());
    }
}
