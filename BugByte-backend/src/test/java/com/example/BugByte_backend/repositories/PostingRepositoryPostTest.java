package com.example.BugByte_backend.repositories;
import com.example.BugByte_backend.models.Post;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import static org.mockito.ArgumentMatchers.any;

import java.util.Date;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest
public class PostingRepositoryPostTest {
    private static final String SQL_INSERT_POST = """
                INSERT INTO posts
                    (op_name, md_content, posted_on)
                VALUES
                    (?, ?, ?);
            """;
    private static final String SQL_GET_POST_BY_ID = "SELECT * FROM posts WHERE id = ?;";
    private static final String SQL_GET_POST_BY_USERNAME_AND_TIME = """
                SELECT id FROM posts
                "WHERE posted_on = ? AND op_name = ?;
            """;
    private static final String SQL_EDIT_POST = "UPDATE posts SET md_content = ? WHERE ID = ?;";
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
}
