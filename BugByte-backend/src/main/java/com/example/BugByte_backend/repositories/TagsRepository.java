package com.example.BugByte_backend.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class TagsRepository implements ITagsRepository {
    private static final String SQL_INSERT_TAGS = """
            INSERT INTO tag (name)
            VALUES %s
            ON DUPLICATE KEY UPDATE id = LAST_INSERT_ID(id);
            """;
    private static final String SQL_ADD_TAGS_TO_QUESTION = """
            INSERT INTO question_tag (question_id, tag_id)
            VALUES %s
            ON DUPLICATE KEY UPDATE question_id = question_id;
            """;
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

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void insertTags(List<String> tags) {
        String tagValues = tags.stream()
            .map(tag -> "('" + tag.replace("'", "''") + "')")
            .collect(Collectors.joining(", "));

        String formattedQuery = String.format(SQL_INSERT_TAGS, tagValues);
        jdbcTemplate.update(formattedQuery);
    }

    @Override
    public Long bulkAddTagsToQuestion(Long questionId, List<String> tags) {
        return null;
    }

    @Override
    public Boolean removeTagsFromQuestion(Long questionId) {
        return null;
    }

    @Override
    public List<String> findTagsByQuestion(Long questionId) {
        if (questionId == null)
            throw new NullPointerException("Question Id is null");

        return jdbcTemplate.queryForList(SQL_FIND_TAGS_BY_QUESTION, String.class, questionId);
    }
}
