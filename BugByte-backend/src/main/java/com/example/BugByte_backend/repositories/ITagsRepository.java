package com.example.BugByte_backend.repositories;

import java.util.List;

public interface ITagsRepository {

    Integer insertTags(List<String> tags);

    List<Long> getTagIdsByName(List<String> tags);

    Integer bulkAddTagsToQuestion(Long questionId, List<String> tags);

    Integer removeTagsFromQuestion(Long questionId);

    List<String> findTagsByQuestion(Long questionId);

    Integer bulkAddTagsToCommunity(Long communityId, List<String> tags);

    Integer removeTagsFromCommunity(Long communityId);

    List<String> findTagsByCommunity(Long communityId);
}
