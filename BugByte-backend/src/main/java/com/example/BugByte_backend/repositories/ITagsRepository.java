package com.example.BugByte_backend.repositories;

import java.util.List;

public interface ITagsRepository {

    Integer insertTags(List<String> tags);

    Integer bulkAddTagsToQuestion(Long questionId, List<String> tags);

    Integer removeTagsFromQuestion(Long questionId);

    List<String> findTagsByQuestion(Long questionId);

    List<Long> getTagIdsByName(List<String> tags);
}
