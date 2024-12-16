package com.example.BugByte_backend.ServicesTests;

import com.example.BugByte_backend.models.Community;
import com.example.BugByte_backend.repositories.SearchingFilteringCommunityRepository;
import com.example.BugByte_backend.services.SearchingFilteringCommunityService;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest
public class SearchingFilteringCommunityServiceTest {
    @Mock
    private SearchingFilteringCommunityRepository communityRepository;

    @InjectMocks
    private SearchingFilteringCommunityService searchingFilteringCommunityService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveQuestion_ShouldReturnSavedQuestion() {
        List<String> tags = List.of("java", "python");
        Community community = Community.builder()
                .id(1L)
                .name("name")
                .description("description")
                .tags(tags)
                .adminId(0L)
                .build();
        when(communityRepository.save(eq(community))).thenReturn(community);

        Community savedCommunity = searchingFilteringCommunityService.saveCommunity(community);

        assertNotNull(savedCommunity);
        assertEquals(community.getId(), savedCommunity.getId());
        assertEquals(community.getName(), savedCommunity.getName());
        assertEquals(community.getDescription(), savedCommunity.getDescription());
    }

    @Test
    void testSaveQuestion_ShouldThrowException_WhenQuestionIsNull() {
        assertThrows(NullPointerException.class, () -> searchingFilteringCommunityService.saveCommunity(null));
    }

    @Test
    void testSearchQuestions_ShouldReturnQuestions() {
        String query = "world";
        List<String> tags = List.of("java", "python");
        Community community = Community.builder()
                .id(1L)
                .name("name")
                .description("hello world")
                .tags(tags)
                .adminId(0L)
                .build();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Community> communityPage = new PageImpl<>(List.of(community), pageable, 1);

        when(communityRepository.findByMdContentAndTags(query, pageable)).thenReturn(communityPage);

        Page<Community> result = searchingFilteringCommunityService.searchCommunity(query, 0, 10);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertTrue(result.getContent().getFirst().getDescription().contains(query));
    }

    @Test
    void testSearchQuestions_ShouldThrowException_WhenQueryIsNull() {
        assertThrows(NullPointerException.class, () -> searchingFilteringCommunityService.searchCommunity(null, 0, 10));
    }

    @Test
    void testGetQuestionsByTags_ShouldReturnQuestions() {
        List<String> tags = List.of("java", "python");
        Pageable pageable = PageRequest.of(0, 10);
        Community c1 = Community.builder()
                .id(1L)
                .name("name")
                .description("hello world")
                .tags(tags)
                .adminId(0L)
                .build();
        Community c2 = Community.builder()
                .id(2L)
                .name("name2")
                .description("hello world2")
                .tags(tags)
                .adminId(2L)
                .build();
        Page<Community> communityPage = new PageImpl<>(List.of(c1, c2), pageable, 2);

        when(communityRepository.findByTags(tags, pageable)).thenReturn(communityPage);

        Page<Community> result = searchingFilteringCommunityService.getCommunityByTags(tags, 0, 10);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
    }

    @Test
    void testGetQuestionsByTags_ShouldThrowException_WhenTagsAreNullOrEmpty() {
        assertThrows(NullPointerException.class, () -> searchingFilteringCommunityService.getCommunityByTags(null, 0, 10));
        assertThrows(NullPointerException.class, () -> searchingFilteringCommunityService.getCommunityByTags(List.of(), 0, 10));
    }
}
