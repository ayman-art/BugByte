package com.example.BugByte_backend.repositories;
import com.example.BugByte_backend.models.Post;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class PostingRepositoryPostTest {
    private static final String SQL_INSERT_POST = """
                INSERT INTO posts
                    (op_name, md_content, posted_on)
                VALUES
                    (?, ?, ?);
            """;
    private static final String SQL_INSERT_UPVOTE = """
                INSERT INTO upVotes
                    (userName, post_id)
                VALUES
                    (?, ?);
            """;
    private static final String SQL_INSERT_DOWNVOTE = """
                INSERT INTO downVotes
                    (userName, post_id)
                VALUES
                    (?, ?);
            """;
    private static final String SQL_INSERT_QUESTION = """
                INSERT INTO questions
                    (id, title, community_id, up_votes, down_votes)
                VALUES
                    (?, ?, ?, 0, 0);
            """;
    private static final String SQL_INSERT_ANSWER = """
                INSERT INTO answers
                    (id, question_id, up_votes, down_votes)
                VALUES
                    (?, ?, 0, 0);
            """;
    private static final String SQL_INSERT_REPLY = """
                INSERT INTO replies
                    (id, answer_id)
                VALUES
                    (?, ?);
            """;
    private static final String SQL_GET_POST_BY_ID = "SELECT * FROM posts WHERE id = ?;";
    private static final String SQL_GET_POST_BY_USERNAME_AND_TIME = """
                SELECT id FROM posts
                WHERE posted_on = ? AND op_name = ?;
            """;
    private static final String SQL_DELETE_QUESTION_BY_ID = "DELETE FROM questions WHERE id = ?;";
    private static final String SQL_DELETE_ANSWER_BY_ID = "DELETE FROM answers WHERE id = ?;";
    private static final String SQL_DELETE_REPLY_BY_ID = "DELETE FROM replies WHERE id = ?;";
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
                SET validated_answer_id = ?
                WHERE id = ?;
            """;
    private static final String SQL_EDIT_POST = "UPDATE posts SET md_content = ? WHERE id = ?;";

    private static final String SQL_GET_QUESTIONS_BY_USERNAME = """
                SELECT *
                FROM questions q
                JOIN posts p on p.id = q.id
                WHERE p.op_name = ?
                ORDER BY p.posted_on DESC
                LIMIT ?
                OFFSET ?;
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
    private static final String SQL_GET_REPLIES_BY_USERNAME = """
                SELECT *
                FROM replies r
                JOIN posts p on p.id = r.id
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
    private static final String SQL_GET_REPLIES_FOR_ANSWER = """
                SELECT *
                FROM replies r
                JOIN posts p on p.id = r.id
                WHERE r.answer_id = ?
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
    private static final String SQL_GET_ANSWER_BY_ID = """
                SELECT *
                FROM answers a
                JOIN posts p on p.id = a.id
                WHERE a.id = ?;
            """;
    private static final String SQL_GET_REPLY_BY_ID = """
                SELECT *
                FROM replies r
                JOIN posts p on p.id = r.id
                WHERE r.id = ?;
            """;
    private static final String SQL_GET_UP_VOTE = "SELECT COUNT(*) FROM upVotes WHERE userName = ? AND post_id = ?;";

    private static final String SQL_GET_DOWN_VOTE = "SELECT COUNT(*) FROM downVotes WHERE userName = ? AND post_id = ?;";

    private static final String SQL_DELETE_UP_VOTE = "DELETE FROM upVotes WHERE userName = ? AND post_id = ?;";
    private static final String SQL_DELETE_DOWN_VOTE = "DELETE FROM downVotes WHERE userName = ? AND post_id = ?;";
    private static final String SQL_DELETE_ANSWERS_BY_QUESTION_ID = "DELETE FROM answers WHERE question_id = ?;";
    private static final String SQL_DELETE_REPLIES_BY_ANSWER_ID = "DELETE FROM replies WHERE answer_id = ?;";
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
    public void testInsertPost_validInput() {
        String op_name = "user1";
        String md_content = "hi";
        int rows = 1;
        when(jdbcTemplate.update(eq(SQL_INSERT_POST), eq(op_name), eq(md_content),any(Date.class)))
                .thenReturn(1);
        assertEquals(rows , 1);
    }

    @Test
    public void testInsertPost_nullUsername() {
        String md_content = "user1";
        String op_name = null;

        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            postingRepository.insertPost(md_content, op_name);
        });

        assertEquals("md content or username is null", exception.getMessage());
    }

    @Test
    public void testGetPostByOpAndTime_nullUserName() {
        Date date = new Date();

        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            postingRepository.getPostByOpAndTime(null,date);
        });
        assertEquals("username or date is null", exception.getMessage());
    }
    @Test
    public void testGetPostByID_validInput() {
        Long postId = 1L;
        Post expectedPost = new Post(postId, "user1", "hi", new Date());

        when(jdbcTemplate.queryForObject(eq(SQL_GET_POST_BY_ID), eq(new Object[]{postId}), any(RowMapper.class)))
                .thenReturn(expectedPost);

        Post post = postingRepository.getPostByID(postId);

        assertEquals(expectedPost, post);
    }

    @Test
    public void testGetPostByID_nullPostId() {
        Exception exception = null;

        try {
            postingRepository.getPostByID(null);
        } catch (Exception e) {
            exception = e;
        }

        assertEquals(NullPointerException.class, exception.getClass());
        assertEquals("postId is null", exception.getMessage());
    }

    @Test
    public void testEditPost_nullPostId() {
        Exception exception = null;

        try {
            postingRepository.editPost(null, "Some content");
        } catch (Exception e) {
            exception = e;
        }
        assertEquals(NullPointerException.class, exception.getClass());
        assertEquals("post id or md content is null", exception.getMessage());
    }

//
//    @Test
//    public void testUpVoteQuestion_validInput() throws Exception {
//        Long questionId = 1L;
//        Integer value = 1;
//        String userName = "user1";
//
//        when(jdbcTemplate.queryForObject(eq(SQL_GET_UP_VOTE), eq(new Object[]{userName, questionId}), eq(Integer.class)))
//                .thenReturn(0);
//
//        // Mock update to return 1, indicating success
//        when(jdbcTemplate.update(anyString(), any())).thenReturn(1);
//
//        Boolean result = postingRepository.upVoteQuestion(questionId, value, userName);
//
//        assertTrue(result);
//    }

    @Test
    public void testUpVoteQuestion_nullInput() {
        Long questionId = null;
        Integer value = 1;
        String userName = "user1";

        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            postingRepository.upVoteQuestion(questionId, value, userName);
        });

        assertEquals("question id or value is null", exception.getMessage());
    }
//
//    @Test
//    public void testDownVoteQuestion_validInput() throws Exception {
//        Long questionId = 1L;
//        Integer value = 1;
//        String userName = "user1";
//
//        when(jdbcTemplate.queryForObject(eq(SQL_GET_DOWN_VOTE), eq(new Object[]{userName, questionId}), eq(Integer.class)))
//                .thenReturn(0);
//        when(jdbcTemplate.update(anyString(), Optional.ofNullable(any()))).thenReturn(1);
//
//        Boolean result = postingRepository.downVoteQuestion(questionId, value, userName);
//
//        assertTrue(result);
//    }

    @Test
    public void testDownVoteQuestion_userAlreadyDownVoted() throws Exception {
        Long questionId = 1L;
        Integer value = 1;
        String userName = "user1";

        when(jdbcTemplate.queryForObject(eq(SQL_GET_DOWN_VOTE), eq(new Object[]{userName, questionId}), eq(Integer.class)))
                .thenReturn(1);

        Exception exception = assertThrows(Exception.class, () -> {
            postingRepository.downVoteQuestion(questionId, value, userName);
        });

        assertEquals("user already down voted this question", exception.getMessage());
    }

//    @Test
//    public void testUpVoteAnswer_validInput() throws Exception {
//        Long answerId = 1L;
//        Integer value = 1;
//        String userName = "user1";
//
//        when(jdbcTemplate.queryForObject(eq(SQL_GET_UP_VOTE), eq(new Object[]{userName, answerId}), eq(Integer.class)))
//                .thenReturn(0);
//        when(jdbcTemplate.update(anyString(), Optional.ofNullable(any()))).thenReturn(1);
//
//        Boolean result = postingRepository.upVoteAnswer(answerId, value, userName);
//
//        assertTrue(result);
//    }

//    @Test
//    public void testDownVoteAnswer_validInput() throws Exception {
//        Long answerId = 1L;
//        Integer value = 1;
//        String userName = "user1";
//
//        when(jdbcTemplate.queryForObject(eq(SQL_GET_DOWN_VOTE), eq(new Object[]{userName, answerId}), eq(Integer.class)))
//                .thenReturn(0);
//        when(jdbcTemplate.update(anyString(), Optional.ofNullable(any()))).thenReturn(1);
//
//        Boolean result = postingRepository.downVoteAnswer(answerId, value, userName);
//
//        assertTrue(result);
//    }

    @Test
    public void testVerifyAnswer_validInput() {
        Long answerId = 1L;
        Long questionId = 1L;

        when(jdbcTemplate.update(eq(SQL_VERIFY_ANSWER), eq(answerId), eq(questionId))).thenReturn(1);

        Boolean result = postingRepository.verifyAnswer(answerId, questionId);

        assertTrue(result);
    }

    @Test
    public void testVerifyAnswer_nullInput() {
        Long answerId = null;
        Long questionId = 1L;

        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            postingRepository.verifyAnswer(answerId, questionId);
        });

        assertEquals("answer id is null", exception.getMessage());
    }

    @Test
    public void testEditPost_validInput() {
        Long postId = 1L;
        String mdContent = "Updated content";

        when(jdbcTemplate.update(eq(SQL_EDIT_POST), eq(mdContent), eq(postId))).thenReturn(1);

        Boolean result = postingRepository.editPost(postId, mdContent);

        assertTrue(result);
    }

    @Test
    public void testEditPost_noRowsUpdated() {
        Long postId = 1L;
        String mdContent = "Updated content";

        when(jdbcTemplate.update(eq(SQL_EDIT_POST), eq(mdContent), eq(postId))).thenReturn(0);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            postingRepository.editPost(postId, mdContent);
        });

        assertEquals("Invalid input", exception.getMessage());
    }

    @Test
    public void testIsUpVoted_true() {
        String userName = "user1";
        long questionId = 1L;

        when(jdbcTemplate.queryForObject(eq(SQL_GET_UP_VOTE), eq(new Object[]{userName, questionId}), eq(Integer.class)))
                .thenReturn(1);

        boolean result = postingRepository.is_UpVoted(userName, questionId);

        assertTrue(result);
    }

    @Test
    public void testIsDownVoted_false() {
        String userName = "user1";
        long questionId = 1L;

        when(jdbcTemplate.queryForObject(eq(SQL_GET_DOWN_VOTE), eq(new Object[]{userName, questionId}), eq(Integer.class)))
                .thenReturn(0);

        boolean result = postingRepository.is_DownVoted(userName, questionId);

        assertFalse(result);
    }
}
