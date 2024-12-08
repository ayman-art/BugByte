package com.example.BugByte_backend.repositories;
import com.example.BugByte_backend.models.Answer;
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
public class PostingRepositoryAnswerTest {
    private static final String SQL_INSERT_ANSWER = """
                INSERT INTO answers
                    (id, question_id, up_votes, down_votes)
                VALUES
                    (?, ?, 0, 0);
            """;
    private static final String SQL_DELETE_ANSWER_BY_ID = "DELETE FROM answers WHERE id = ?;";
    private static final String SQL_UPDATE_UP_VOTES_ANSWERS = """
                UPDATE answers
                SET up_votes = up_votes + ?
                WHERE id = ?;
            """;
    private static final String SQL_UPDATE_DOWN_VOTES_ANSWERS = """
                UPDATE answers
                SET down_votes = down_votes + ?
                WHERE id = ?;
            """;
    private static final String SQL_GET_ANSWERS_BY_USERNAME = """
                SELECT *
                FROM answers a
                JOIN posts p on p.id = a.id
                WHERE p.op_name = ?
                ORDER BY p.posted_on DESC
                LIMIT ?
                OFFSET ?;
            """;
    private static final String SQL_GET_ANSWERS_FOR_QUESTION = """
                SELECT *
                FROM answers a
                JOIN posts p on p.id = a.id
                WHERE a.question_id = ?
                ORDER BY p.posted_on DESC
                LIMIT ? OFFSET ?;
            """;
    private static final String SQL_GET_ANSWER_BY_ID = """
                SELECT *
                FROM answers a
                JOIN posts p on p.id = a.id
                WHERE a.id = ?;
            """;
    private static final String SQL_DELETE_REPLIES_BY_ANSWER_ID = "DELETE FROM replies WHERE answer_id = ?;";
    private static final String SQL_DELETE_POST_BY_ID = "DELETE FROM posts WHERE id = ?;";

    @Mock
    private JdbcTemplate jdbcTemplate ;
    @InjectMocks
    private PostingRepository postingRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public void testInsertAnswer_validInput() {
        Long answerId = 1L;
        Long questionId = 2L;
        when(jdbcTemplate.update(eq(SQL_INSERT_ANSWER), eq(answerId), eq(questionId)))
                .thenReturn(1);

        Boolean result = postingRepository.insertAnswer(answerId, questionId);
        assertTrue(result);
    }

    @Test
    public void testInsertAnswer_nullAnswerId() {
        Exception exception = null;

        try {
            postingRepository.insertAnswer(null, 2L);
        } catch (Exception e) {
            exception = e;
        }
        assertEquals(NullPointerException.class, exception.getClass());
        assertEquals("question id or answer id is null", exception.getMessage());
    }

    @Test
    public void testInsertAnswer_nullQuestionId() {
        Exception exception = null;

        try {
            postingRepository.insertAnswer(1L, null);
        } catch (Exception e) {
            exception = e;
        }
        assertEquals(NullPointerException.class, exception.getClass());
        assertEquals("question id or answer id is null", exception.getMessage());
    }
    @Test
    public void testDeleteAnswer_validInput() {
        Long answerId = 1L;
        when(jdbcTemplate.update(eq(SQL_DELETE_REPLIES_BY_ANSWER_ID), eq(answerId))).thenReturn(1);
        when(jdbcTemplate.update(eq(SQL_DELETE_ANSWER_BY_ID), eq(answerId))).thenReturn(1);
        when(jdbcTemplate.update(eq(SQL_DELETE_POST_BY_ID), eq(answerId))).thenReturn(1);

        Boolean result = postingRepository.deleteAnswer(answerId);
        assertTrue(result);
    }

    @Test
    public void testDeleteAnswer_nullAnswerId() {
        Exception exception = null;

        try {
            postingRepository.deleteAnswer(null);
        } catch (Exception e) {
            exception = e;
        }
        assertEquals(NullPointerException.class, exception.getClass());
        assertEquals("answer id is null", exception.getMessage());
    }

    @Test
    public void testDeleteAnswer_noRowsAffected() {
        Long answerId = 1L;
        when(jdbcTemplate.update(eq(SQL_DELETE_REPLIES_BY_ANSWER_ID), eq(answerId))).thenReturn(0);
        when(jdbcTemplate.update(eq(SQL_DELETE_ANSWER_BY_ID), eq(answerId))).thenReturn(0);
        when(jdbcTemplate.update(eq(SQL_DELETE_POST_BY_ID), eq(answerId))).thenReturn(0);

        Exception exception = null;
        try {
            postingRepository.deleteAnswer(answerId);
        } catch (Exception e) {
            exception = e;
        }
        assertEquals(RuntimeException.class, exception.getClass());
        assertEquals("Invalid input", exception.getMessage());
    }

    @Test
    public void testUpVoteAnswer_validInput() {
        Long answerId = 1L;
        Integer value = 5;
        when(jdbcTemplate.update(eq(SQL_UPDATE_UP_VOTES_ANSWERS), eq(value), eq(answerId)))
                .thenReturn(1);

        Boolean result = postingRepository.upVoteAnswer(answerId, value);
        assertTrue(result);
    }

    @Test
    public void testUpVoteAnswer_nullAnswerId() {
        Exception exception = null;

        try {
            postingRepository.upVoteAnswer(null, 5);
        } catch (Exception e) {
            exception = e;
        }
        assertEquals(NullPointerException.class, exception.getClass());
        assertEquals("answer id or value is null", exception.getMessage());
    }

    @Test
    public void testDownVoteAnswer_validInput() {
        Long answerId = 1L;
        Integer value = -5;
        when(jdbcTemplate.update(eq(SQL_UPDATE_DOWN_VOTES_ANSWERS), eq(value), eq(answerId)))
                .thenReturn(1);

        Boolean result = postingRepository.downVoteAnswer(answerId, value);
        assertTrue(result);
    }

    @Test
    public void testDownVoteAnswer_nullAnswerId() {
        Exception exception = null;
        try {
            postingRepository.downVoteAnswer(null, -5);
        } catch (Exception e) {
            exception = e;
        }
        assertEquals(NullPointerException.class, exception.getClass());
        assertEquals("answer id or value is null", exception.getMessage());
    }
    @Test
    public void testGetAnswersByUserName_validInput() {
        String userName = "user1";
        Integer limit = 10;
        Integer offset = 0;

        List<Answer> expectedAnswers = new ArrayList<>();
        when(jdbcTemplate.query(eq(SQL_GET_ANSWERS_BY_USERNAME), eq(new Object[]{userName, limit, offset}), any(RowMapper.class)))
                .thenReturn(expectedAnswers);

        List<Answer> actualAnswers = postingRepository.getAnswersByUserName(userName, limit, offset);
        assertEquals(expectedAnswers, actualAnswers);
    }

    @Test
    public void testGetAnswersByUserName_nullUserName() {
        Exception exception = null;

        try {
            postingRepository.getAnswersByUserName(null, 10, 0);
        } catch (Exception e) {
            exception = e;
        }
        assertEquals(NullPointerException.class, exception.getClass());
        assertEquals("username is null", exception.getMessage());
    }
    @Test
    public void testGetAnswersForQuestion_validInput() {
        Long questionId = 1L;
        Integer limit = 10;
        Integer offset = 0;

        List<Answer> expectedAnswers = new ArrayList<>();
        when(jdbcTemplate.query(eq(SQL_GET_ANSWERS_FOR_QUESTION), eq(new Object[]{questionId, limit, offset}), any(RowMapper.class)))
                .thenReturn(expectedAnswers);

        List<Answer> actualAnswers = postingRepository.getAnswersForQuestion(questionId, limit, offset);
        assertEquals(expectedAnswers, actualAnswers);
    }

    @Test
    public void testGetAnswersForQuestion_nullQuestionId() {
        Exception exception = null;

        try {
            postingRepository.getAnswersForQuestion(null, 10, 0);
        } catch (Exception e) {
            exception = e;
        }
        assertEquals(NullPointerException.class, exception.getClass());
        assertEquals("username is null", exception.getMessage());
    }
    @Test
    public void testGetAnswerById_validInput() {
        Long answerId = 1L;
        Answer expectedAnswer = new Answer();
        expectedAnswer.setId(answerId);
        when(jdbcTemplate.queryForObject(eq(SQL_GET_ANSWER_BY_ID), eq(new Object[]{answerId}), any(RowMapper.class)))
                .thenReturn(expectedAnswer);

        Answer actualAnswer = postingRepository.getAnswerById(answerId);
        assertEquals(expectedAnswer, actualAnswer);
    }

    @Test
    public void testGetAnswerById_nullAnswerId() {
        Exception exception = null;

        try {
            postingRepository.getAnswerById(null);
        } catch (Exception e) {
            exception = e;
        }
        assertEquals(NullPointerException.class, exception.getClass());
        assertEquals("questionId is null", exception.getMessage());
    }

}
