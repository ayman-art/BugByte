package com.example.BugByte_backend.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.BugByte_backend.models.Question;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SearchingFilteringQuestionRepository extends  ElasticsearchRepository<Question, String> {
    @Query("{\"bool\": {\"must\": [" +
            "{\"multi_match\": {\"query\": \"?0\", \"fields\": [\"mdContent\", \"tags^2\", \"title^3\"], \"fuzziness\": \"AUTO\"}}], " +
            "\"should\": [" +
            "{\"match\": {\"mdContent\": {\"query\": \"?0\", \"boost\": 1.5}}}" +
            "]}}")
    Page<Question> findByMdContentAndTags(String query, Pageable pageable);

    @Query("{\"bool\": {\"must\": [{" +
            "\"terms\": {\"tags\": ?0}" +
            "}]}, " +
            "\"sort\": [{" +
            "\"postedOn\": {\"order\": \"desc\"}" +
            "}]}}")
    Page<Question> findByTagsIn(List<String> tags, Pageable pageable);
}
