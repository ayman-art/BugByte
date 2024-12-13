package com.example.BugByte_backend.repositories;

import com.example.BugByte_backend.models.Community;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SearchingFilteringCommunityRepository extends ElasticsearchRepository<Community, String> {
    @Query("{\"bool\": {\"must\": [" +
            "{\"multi_match\": {\"query\": \"?0\", \"fields\": [\"description\", \"name^2\"], \"fuzziness\": \"AUTO\"}}], " +
            "\"should\": [" +
            "{\"match\": {\"description\": {\"query\": \"?0\", \"boost\": 1.5}}}" +
            "]}}")
    Page<Community> findByMdContentAndTags(String query, Pageable pageable);

    @Query("{\"bool\": {\"must\": [" +
            "{\"terms\": {\"tags\": ?0}}" +
            "]}}")
    Page<Community> findByTags(List<String> tags, Pageable pageable);
}
