package com.example.BugByte_backend.services;

import com.example.BugByte_backend.models.Question;
import com.example.BugByte_backend.repositories.SearchingFilteringQuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SearchingFilteringQuestionService {
    @Autowired
    private SearchingFilteringQuestionRepository questionRepository;

    public Question saveQuestion(Question question) {
        if (question == null)
            throw new NullPointerException("Question can't be null");
        return questionRepository.save(question);
    }

    public Page<Question> searchQuestions(String query, int page, int size) {
        if (query == null)
            throw new NullPointerException("Query can't be null");

        Pageable pageable = PageRequest.of(page, size);
        System.out.println("Page");
        return questionRepository.findByMdContentAndTags(query, pageable);
    }

    public Page<Question> getQuestionsByTags(List<String> tags, int page, int size) {
        if (tags == null || tags.isEmpty())
            throw new NullPointerException("tags can't be null or empty");

        Pageable pageable = PageRequest.of(page, size);
        return questionRepository.findByTagsIn(tags, pageable);
    }

    public void deleteQuestion(Question question) {
        if (question == null)
            throw new NullPointerException("question is null");

        questionRepository.delete(question);
    }
}
