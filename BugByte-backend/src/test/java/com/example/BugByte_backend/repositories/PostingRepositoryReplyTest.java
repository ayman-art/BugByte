package com.example.BugByte_backend.repositories;
import com.example.BugByte_backend.models.Post;
import com.example.BugByte_backend.models.Question;
import com.example.BugByte_backend.models.Reply;
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
public class PostingRepositoryReplyTest {
    private static final String SQL_INSERT_REPLY = """
                INSERT INTO replies
                    (id, answer_id)
                VALUES
                    (?, ?);
            """;
    private static final String SQL_DELETE_REPLY_BY_ID = "DELETE FROM replies WHERE id = ?;";
    private static final String SQL_GET_REPLIES_BY_USERNAME = """
                SELECT *
                FROM replies r
                JOIN posts p on p.id = r.id
                WHERE p.op_name = ?
                ORDER BY p.posted_on DESC
                LIMIT ?
                OFFSET ?;
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
    private static final String SQL_GET_REPLY_BY_ID = """
                SELECT *
                FROM replies r
                JOIN posts p on p.id = r.id
                WHERE r.id = ?;
            """;
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
    public void testInsertReply_validInput() {
        Long replyId = 1L;
        Long answerId = 1L;
        when(jdbcTemplate.update(eq(SQL_INSERT_REPLY), eq(replyId), eq(answerId)))
                .thenReturn(1);

        Boolean result = postingRepository.insertReply(replyId, answerId);
        assertTrue(result);
    }

    @Test
    public void testInsertReply_nullIds() {
        Exception exception = null;

        try {
            postingRepository.insertReply(null, null);
        } catch (Exception e) {
            exception = e;
        }
        assertEquals(NullPointerException.class, exception.getClass());
        assertEquals("reply id or answer id is null", exception.getMessage());
    }

    @Test
    public void testDeleteReply_validInput() {
        Long replyId = 1L;
        when(jdbcTemplate.update(eq(SQL_DELETE_REPLY_BY_ID), eq(replyId))).thenReturn(1);
        when(jdbcTemplate.update(eq(SQL_DELETE_POST_BY_ID), eq(replyId))).thenReturn(1);

        Boolean result = postingRepository.deleteReply(replyId);
        assertTrue(result);
    }
    @Test
    public void testDeleteReply_nullId() {
        Exception exception = null;

        try {
            postingRepository.deleteReply(null);
        } catch (Exception e) {
            exception = e;
        }
        assertEquals(NullPointerException.class, exception.getClass());
        assertEquals("reply id is null", exception.getMessage());
    }

    @Test
    public void testDeleteReply_invalidInput() {
        Long replyId = 1L;
        when(jdbcTemplate.update(eq(SQL_DELETE_REPLY_BY_ID), eq(replyId))).thenReturn(0); // Simulate no rows affected

        Exception exception = null;
        try {
            postingRepository.deleteReply(replyId);
        } catch (Exception e) {
            exception = e;
        }
        assertEquals(RuntimeException.class, exception.getClass());
        assertEquals("Invalid input", exception.getMessage());
    }
    @Test
    public void testGetRepliesByUserName_validInput() {
        String userName = "user1";
        Integer limit = 10;
        Integer offset = 0;
        List<Reply> expectedReplies = new ArrayList<>();
        when(jdbcTemplate.query(eq(SQL_GET_REPLIES_BY_USERNAME), eq(new Object[]{userName, limit, offset}), any(RowMapper.class)))
                .thenReturn(expectedReplies);

        List<Reply> replies = postingRepository.getRepliesByUserName(userName, limit, offset);
        assertNotNull(replies);
        assertEquals(replies , expectedReplies);
    }

    @Test
    public void testGetRepliesByUserName_nullUserName() {
        Exception exception = null;

        try {
            postingRepository.getRepliesByUserName(null, 10, 0);
        } catch (Exception e) {
            exception = e;
        }
        assertEquals(NullPointerException.class, exception.getClass());
        assertEquals("username is null", exception.getMessage());
    }
    @Test
    public void testGetRepliesForAnswer_validInput() {
        Long answerId = 1L;
        Integer limit = 10;
        Integer offset = 0;
        Post post = new Post();
        Reply reply = new Reply(post , answerId);

        List<Reply> expectedReplies = new ArrayList<>();
        expectedReplies.add(reply);
        when(jdbcTemplate.query(eq(SQL_GET_REPLIES_FOR_ANSWER), eq(new Object[]{answerId, limit, offset}), any(RowMapper.class)))
                .thenReturn(expectedReplies);

        List<Reply> replies = postingRepository.getRepliesForAnswer(answerId, limit, offset);
        assertNotNull(replies);
        assertEquals(expectedReplies , replies);
        assertEquals(1 , replies.size());

    }

    @Test
    public void testGetRepliesForAnswer_nullAnswerId() {
        Exception exception = null;

        try {
            postingRepository.getRepliesForAnswer(null, 10, 0);
        } catch (Exception e) {
            exception = e;
        }
        assertEquals(NullPointerException.class, exception.getClass());
        assertEquals("username is null", exception.getMessage());
    }

    @Test
    public void testGetRepliesForAnswer_invalidInput() {
        Long answerId = 1L;
        Integer limit = 10;
        Integer offset = 0;

        when(jdbcTemplate.query(eq(SQL_GET_REPLIES_FOR_ANSWER),eq( new Object[]{answerId, limit, offset}), any(RowMapper.class)))
                .thenReturn(List.of());

        List<Reply> replies = postingRepository.getRepliesForAnswer(answerId, limit, offset);
        assertTrue(replies.isEmpty());
    }
    @Test
    public void testGetReplyById_validInput() {
        Long replyId = 1L;
        Post post = new Post();
        post.setId(1L);
        Reply expectedReply = new Reply(post , 2L);
        when(jdbcTemplate.queryForObject(eq(SQL_GET_REPLY_BY_ID), eq(new Object[]{replyId}), any(RowMapper.class)))
                .thenReturn(expectedReply);

        Reply actualReply = postingRepository.getReplyById(replyId);

        assertNotNull(actualReply);
        assertEquals(expectedReply.getPost().getId(), actualReply.getPost().getId());
    }

    @Test
    public void testGetReplyById_nullReplyId() {
        Exception exception = null;

        try {
            postingRepository.getReplyById(null);
        } catch (Exception e) {
            exception = e;
        }
        assertNotNull(exception);
        assertEquals(NullPointerException.class, exception.getClass());
        assertEquals("questionId is null", exception.getMessage());
    }

    @Test
    public void testGetReplyById_notFound() {
        Long replyId = 1L;
        when(jdbcTemplate.queryForObject(eq(SQL_GET_REPLY_BY_ID), eq(new Object[]{replyId}), any(RowMapper.class)))
                .thenThrow(new RuntimeException("Reply not found"));

        Exception exception = null;

        try {
            postingRepository.getReplyById(replyId);
        } catch (Exception e) {
            exception = e;
        }
        assertNotNull(exception);
        assertEquals(RuntimeException.class, exception.getClass());
        assertEquals("Reply not found", exception.getMessage());
    }

}
