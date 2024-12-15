package com.example.BugByte_backend.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest
public class TagsRepositoryTest {

    private static final String SQL_DELETE_TAGS_FROM_QUESTION = """
            DELETE FROM question_tag
            WHERE question_id = ?;
            """;
    private static final String SQL_FIND_TAGS_BY_QUESTION = """
            SELECT name
            FROM question_tag qt
            JOIN tag t ON qt.tag_id = t.id
            WHERE qt.question_id = ?;
            """;
    private static final String SQL_DELETE_TAGS_FROM_COMMUNITY = """
            DELETE FROM community_tag
            WHERE community_id = ?;
            """;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private TagsRepository tagsRepositoryMock;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testInsertTagsValid() {
        List<String> tags = Arrays.asList("python", "vim", "git");

        when(jdbcTemplate.update(anyString())).thenReturn(tags.size());

        Integer result = tagsRepositoryMock.insertTags(tags);

        assertEquals(tags.size(), result);
        Mockito.verify(jdbcTemplate, Mockito.times(1)).update(anyString());
    }

    @Test
    void testInsertTagsNull() {
        assertThrows(NullPointerException.class, () -> tagsRepositoryMock.insertTags(null));
    }

    @Test
    void testInsertTagsEmptyList() {
        assertThrows(NullPointerException.class, () -> tagsRepositoryMock.insertTags(Collections.emptyList()));
    }

    @Test
    void testGetTagIdsByNameValid() {
        List<String> tags = Arrays.asList("python", "vim");
        List<Long> expectedTagIds = Arrays.asList(1L, 2L);

        when(jdbcTemplate.queryForList(anyString(), eq(Long.class))).thenReturn(expectedTagIds);

        List<Long> result = tagsRepositoryMock.getTagIdsByName(tags);

        assertEquals(expectedTagIds, result);
    }

    @Test
    void testGetTagIdsByNameNull() {
        assertThrows(NullPointerException.class, () -> tagsRepositoryMock.getTagIdsByName(null));
    }

    @Test
    void testGetTagIdsByNameEmptyList() {
        assertThrows(NullPointerException.class, () -> tagsRepositoryMock.getTagIdsByName(Collections.emptyList()));
    }

    @Test
    void testBulkAddTagsToQuestionValid() {
        Long questionId = 1L;
        List<String> tags = Arrays.asList("python", "vim");

        when(jdbcTemplate.queryForList(anyString(), Mockito.eq(Long.class)))
                .thenReturn(Arrays.asList(1L, 2L));
        when(jdbcTemplate.update(eq(SQL_DELETE_TAGS_FROM_QUESTION), eq(questionId))).thenReturn(1);
        when(jdbcTemplate.update(anyString())).thenReturn(1);

        Integer result = tagsRepositoryMock.bulkAddTagsToQuestion(questionId, tags);

        assertEquals(1, result);
        Mockito.verify(jdbcTemplate, Mockito.times(2)).update(anyString());
    }

    @Test
    void testBulkAddTagsToQuestionNull() {
        assertThrows(NullPointerException.class, () -> tagsRepositoryMock.bulkAddTagsToQuestion(null, Arrays.asList("java", "cpp")));
        assertThrows(NullPointerException.class, () -> tagsRepositoryMock.bulkAddTagsToQuestion(1L, null));
        assertThrows(NullPointerException.class, () -> tagsRepositoryMock.bulkAddTagsToQuestion(1L, Collections.emptyList()));
    }

    @Test
    void testRemoveTagsFromQuestionValid() {
        Long questionId = 1L;

        when(jdbcTemplate.update(eq(SQL_DELETE_TAGS_FROM_QUESTION), eq(questionId))).thenReturn(1);

        Integer result = tagsRepositoryMock.removeTagsFromQuestion(questionId);

        assertEquals(1, result);
    }

    @Test
    void testRemoveTagsFromQuestionNull() {
        assertThrows(NullPointerException.class, () -> tagsRepositoryMock.removeTagsFromQuestion(null));
    }

    @Test
    void testFindTagsByQuestionValid() {
        Long questionId = 1L;
        List<String> expectedTags = Arrays.asList("cpp", "vim");

        when(jdbcTemplate.queryForList(eq(SQL_FIND_TAGS_BY_QUESTION), eq(String.class), eq(questionId)))
                .thenReturn(expectedTags);

        List<String> result = tagsRepositoryMock.findTagsByQuestion(questionId);

        assertEquals(expectedTags, result);
    }

    @Test
    void testFindTagsByQuestionNull() {
        assertThrows(NullPointerException.class, () -> tagsRepositoryMock.findTagsByQuestion(null));
    }

    @Test
    void testBulkAddTagsToCommunityValid() {
        Long communityId = 1L;
        List<String> tags = Arrays.asList("cpp", "vim");

        Mockito.doReturn(1).when(jdbcTemplate).update(Mockito.anyString());
        Mockito.when(jdbcTemplate.queryForList(Mockito.anyString(), Mockito.eq(Long.class)))
                .thenReturn(Arrays.asList(1L, 2L));

        Integer result = tagsRepositoryMock.bulkAddTagsToCommunity(communityId, tags);

        assertEquals(1, result);
    }

    @Test
    void testBulkAddTagsToCommunityNull() {
        assertThrows(NullPointerException.class, () -> tagsRepositoryMock.bulkAddTagsToCommunity(null, Arrays.asList("java", "cpp")));
        assertThrows(NullPointerException.class, () -> tagsRepositoryMock.bulkAddTagsToCommunity(1L, null));
        assertThrows(NullPointerException.class, () -> tagsRepositoryMock.bulkAddTagsToCommunity(1L, Collections.emptyList()));
    }

    @Test
    void testRemoveTagsFromCommunityValid() {
        Long communityId = 1L;

        when(jdbcTemplate.update(eq(SQL_DELETE_TAGS_FROM_COMMUNITY), eq(communityId))).thenReturn(1);

        Integer result = tagsRepositoryMock.removeTagsFromCommunity(communityId);

        assertEquals(1, result);
    }

    @Test
    void testRemoveTagsFromCommunityNull() {
        assertThrows(NullPointerException.class, () -> tagsRepositoryMock.removeTagsFromCommunity(null));
    }

    @Test
    void testFindTagsByCommunityValid() {
        Long communityId = 1L;
        List<String> expectedTags = Arrays.asList("java", "spring");

        when(jdbcTemplate.queryForList(Mockito.anyString(), eq(String.class), eq(communityId)))
                .thenReturn(expectedTags);

        List<String> result = tagsRepositoryMock.findTagsByCommunity(communityId);

        assertEquals(expectedTags, result);
    }

    @Test
    void testFindTagsByCommunityNull() {
        assertThrows(NullPointerException.class, () -> tagsRepositoryMock.findTagsByCommunity(null));
    }

}
