package com.example.BugByte_backend.repositories;

import java.util.List;

public interface ITagsRepository {

    Long insertTags(List<String> name);

    Long bulkAddTagsToQuestion(Long questionId, List<String> tags);

    Boolean removeTagsFromQuestion(Long questionId);

    List<String> findTagsByQuestion(Long questionId);
}
