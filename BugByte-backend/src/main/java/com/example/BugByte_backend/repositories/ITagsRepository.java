package com.example.BugByte_backend.repositories;

import java.util.List;

public interface ITagsRepository {

    void insertTags(List<String> tags);

    Long bulkAddTagsToQuestion(Long questionId, List<String> tags);

    Boolean removeTagsFromQuestion(Long questionId);

    List<String> findTagsByQuestion(Long questionId);
}
