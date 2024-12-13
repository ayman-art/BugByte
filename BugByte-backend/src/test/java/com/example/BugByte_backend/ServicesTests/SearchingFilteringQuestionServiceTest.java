package com.example.BugByte_backend.ServicesTests;

import com.example.BugByte_backend.models.Question;
import com.example.BugByte_backend.repositories.SearchingFilteringQuestionRepository;
import com.example.BugByte_backend.services.SearchingFilteringQuestionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest
public class SearchingFilteringQuestionServiceTest {

    @Mock
    private SearchingFilteringQuestionRepository questionRepository;

    @InjectMocks
    private SearchingFilteringQuestionService searchingFilteringQuestionService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveQuestion_ShouldReturnSavedQuestion() {
        List<String> tags = List.of("java", "python");
        Question question = Question.builder()
                .id(1L)
                .title("title")
                .mdContent("mdContent")
                .tags(tags)
                .upVotes(0L)
                .downVotes(0L)
                .communityId(1L)
                .validatedAnswerId(1L)
                .creatorUserName("Ashraf")
                .build();
        when(questionRepository.save(eq(question))).thenReturn(question);

        Question savedQuestion = searchingFilteringQuestionService.saveQuestion(question);

        assertNotNull(savedQuestion);
        assertEquals(question.getId(), savedQuestion.getId());
        assertEquals(question.getTitle(), savedQuestion.getTitle());
        assertEquals(question.getMdContent(), savedQuestion.getMdContent());
    }

    @Test
    void testSaveQuestion_ShouldThrowException_WhenQuestionIsNull() {
        assertThrows(NullPointerException.class, () -> searchingFilteringQuestionService.saveQuestion(null));
    }

    @Test
    void testSearchQuestions_ShouldReturnQuestions() {
        // Arrange
        String query = "world";
        List<String> tags = List.of("java", "python");
        Question question = Question.builder()
                .id(1L)
                .title("title")
                .mdContent("hello world")
                .tags(tags)
                .upVotes(0L)
                .downVotes(0L)
                .communityId(1L)
                .validatedAnswerId(1L)
                .creatorUserName("Ashraf")
                .build();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Question> questionPage = new PageImpl<>(List.of(question), pageable, 1);

        when(questionRepository.findByMdContentAndTags(query, pageable)).thenReturn(questionPage);

        Page<Question> result = searchingFilteringQuestionService.searchQuestions(query, 0, 10);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertTrue(result.getContent().getFirst().getMdContent().contains(query));
    }

    @Test
    void testSearchQuestions_ShouldThrowException_WhenQueryIsNull() {
        assertThrows(NullPointerException.class, () -> searchingFilteringQuestionService.searchQuestions(null, 0, 10));
    }

    @Test
    void testGetQuestionsByTags_ShouldReturnQuestions() {
        List<String> tags = List.of("java", "python");
        Pageable pageable = PageRequest.of(0, 10);
        Question q1 = Question.builder()
                .id(1L)
                .title("title")
                .mdContent("hello world")
                .tags(tags)
                .upVotes(0L)
                .downVotes(0L)
                .communityId(1L)
                .validatedAnswerId(1L)
                .creatorUserName("Ashraf")
                .build();
        Question q2 = Question.builder()
                .id(2L)
                .title("title2")
                .mdContent("hello world2")
                .tags(tags)
                .upVotes(0L)
                .downVotes(0L)
                .communityId(2L)
                .validatedAnswerId(2L)
                .creatorUserName("Ashraf")
                .build();
        Page<Question> questionList = new PageImpl<>(List.of(q1, q2), pageable, 2);

        when(questionRepository.findByTagsIn(tags, pageable)).thenReturn(questionList);

        Page<Question> result = searchingFilteringQuestionService.getQuestionsByTags(tags, 0, 10);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
    }

    @Test
    void testGetQuestionsByTags_ShouldThrowException_WhenTagsAreNullOrEmpty() {
        assertThrows(NullPointerException.class, () -> searchingFilteringQuestionService.getQuestionsByTags(null, 0, 10));
        assertThrows(NullPointerException.class, () -> searchingFilteringQuestionService.getQuestionsByTags(List.of(), 0, 10));
    }
}
