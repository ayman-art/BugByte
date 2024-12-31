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
                    (id, title, community_id, up_votes, down_votes)
                VALUES
                    (?, ?, ?, 0, 0);
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
    private static final String SQL_GET_UP_VOTE = "SELECT COUNT(*) FROM up_votes WHERE user_name = ? AND question_id = ?";
    private static final String SQL_DELETE_DOWN_VOTE = "DELETE FROM down_votes WHERE user_name = ? AND question_id = ?";
    private static final String SQL_INSERT_UPVOTE = "INSERT INTO up_votes(user_name, question_id) VALUES (?, ?)";
    private static final String SQL_DELETE_UP_VOTE = "DELETE FROM up_votes WHERE user_name = ? AND question_id = ?";
    private static final String SQL_GET_DOWN_VOTE = "SELECT COUNT(*) FROM down_votes WHERE user_name = ? AND question_id = ?";

    private static final String SQL_INSERT_DOWNVOTE = "INSERT INTO down_votes(user_name, question_id) VALUES (?, ?)";
    private static final String SQL_UPDATE_REPUTATION_POSITIVELY_QUESTIONS = """
            UPDATE users
            SET reputation = reputation + 1
            WHERE user_name = (
                SELECT posts.op_name
                FROM questions
                JOIN posts ON questions.id = posts.id
                WHERE questions.id = ?
            );          
            """;
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
        String title = "title";
        Long communityId = 1L;
        when(jdbcTemplate.update(eq(SQL_INSERT_QUESTION), eq(questionId), eq(title), eq(communityId)))
                .thenReturn(1);

        Boolean result = postingRepository.insertQuestion(questionId, title, communityId);
        assertTrue(result);
    }

    @Test
    public void testInsertQuestion_nullInput() {
        Exception exception = null;

        try {
            postingRepository.insertQuestion(null, "title", 1L);
        } catch (Exception e) {
            exception = e;
        }
        assertEquals(NullPointerException.class, exception.getClass());
        assertEquals("question id or title or community id is null", exception.getMessage());
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
    public void testVerifyAnswer_nullInput() {
        Exception exception = null;
        Long questionId = 1L;
        try {
            postingRepository.verifyAnswer(null , questionId);
        } catch (Exception e) {
            exception = e;
        }
        assertEquals(NullPointerException.class, exception.getClass());
        assertEquals("answer id is null", exception.getMessage());
    }
    @Test
    public void testUpVoteQuestion_UserAlreadyUpVoted() {
        Long questionId = 1L;
        Integer value = 1;
        String userName = "user1";

        when(jdbcTemplate.queryForObject(SQL_GET_UP_VOTE, new Object[]{userName, questionId}, Integer.class))
                .thenReturn(1);

        Exception exception = assertThrows(Exception.class, () -> {
            postingRepository.upVoteQuestion(questionId, value, userName);
        });
    }

    @Test
    public void testUpVoteQuestion_UserDidNotUpVoteBefore() {
        Long questionId = 1L;
        Integer value = 0;
        String userName = "user1";

        when(jdbcTemplate.queryForObject(SQL_GET_UP_VOTE, new Object[]{userName, questionId}, Integer.class))
                .thenReturn(0);

        Exception exception = assertThrows(Exception.class, () -> {
            postingRepository.upVoteQuestion(questionId, value, userName);
        });
    }

    @Test
    public void testUpVoteQuestion_RemoveUpVote_Success() throws Exception {
        Long questionId = 1L;
        Integer value = 0;
        String userName = "user1";

        when(jdbcTemplate.queryForObject(SQL_GET_UP_VOTE, new Object[]{userName, questionId}, Integer.class))
                .thenReturn(1);

        when(jdbcTemplate.update(eq(SQL_DELETE_UP_VOTE), eq(userName), eq(questionId))).thenReturn(1);
        when(jdbcTemplate.update(eq(SQL_UPDATE_UP_VOTES_QUESTIONS), eq(value), eq(questionId))).thenReturn(1);
        when(jdbcTemplate.update(eq(SQL_UPDATE_REPUTATION_POSITIVELY_QUESTIONS), eq(questionId))).thenReturn(1);
        Boolean result = postingRepository.upVoteQuestion(questionId, value, userName);

        assertTrue(result);

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
        expectedQuestion.setId(questionId);
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
    @Test
    public void testDownVoteQuestion_UserAlreadyDownVoted() {
        Long questionId = 1L;
        Integer value = 1;
        String userName = "user1";

        when(jdbcTemplate.queryForObject(SQL_GET_DOWN_VOTE, new Object[]{userName, questionId}, Integer.class))
                .thenReturn(1);

        Exception exception = assertThrows(Exception.class, () -> {
            postingRepository.downVoteQuestion(questionId, value, userName);
        });

        assertEquals("user already down voted this question", exception.getMessage());
    }

    @Test
    public void testDownVoteQuestion_UserDidNotDownVoteBefore() {
        Long questionId = 1L;
        Integer value = 0;
        String userName = "user1";

        when(jdbcTemplate.queryForObject(SQL_GET_DOWN_VOTE, new Object[]{userName, questionId}, Integer.class))
                .thenReturn(0);

        Exception exception = assertThrows(Exception.class, () -> {
            postingRepository.downVoteQuestion(questionId, value, userName);
        });
    }

}
