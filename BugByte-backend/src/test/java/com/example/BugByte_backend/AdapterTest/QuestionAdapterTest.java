package com.example.BugByte_backend.AdapterTest;

import static org.junit.jupiter.api.Assertions.*;

import com.example.BugByte_backend.Adapters.QuestionAdapter;
import com.example.BugByte_backend.models.Question;
import org.junit.jupiter.api.Test;
import java.util.*;

class QuestionAdapterTest {

    private final QuestionAdapter questionAdapter = new QuestionAdapter();

    @Test
    void testToMap() {
        // Arrange: Create a Question object
        Question question = Question.builder()
                .id(1L)
                .creatorUserName("user123")
                .mdContent("This is a sample question content.")
                .postedOn(new Date())
                .communityId(201L)
                .upVotes(10L)
                .downVotes(3L)
                .validatedAnswerId(101L)
                .title("Sample Question Title")
                .tags(new ArrayList<>(List.of("java", "testing")))
                .build();

        // Act: Convert the Question object to a map
        Map<String, Object> result = questionAdapter.toMap(question);

        // Assert: Check the map contains the expected values
        assertEquals(1L, result.get("questionId"));
        assertEquals("user123", result.get("opName"));
        assertEquals("This is a sample question content.", result.get("mdContent"));
        assertNotNull(result.get("postedOn"));
        assertEquals(201L, result.get("communityId"));
        assertEquals(10L, result.get("upVotes"));
        assertEquals(3L, result.get("downVotes"));
        assertEquals(101L, result.get("validatedAnswerId"));
        assertEquals("Sample Question Title", result.get("title"));
        assertEquals(List.of("java", "testing"), result.get("tags"));
    }

    @Test
    void testFromMap() {
        // Arrange: Create a map with sample data
        Map<String, Object> map = new HashMap<>();
        map.put("questionId", 1L);
        map.put("opName", "user123");
        map.put("mdContent", "This is a sample question content.");
        map.put("postedOn", new Date());
        map.put("communityId", 201L);
        map.put("upVotes", 10L);
        map.put("downVotes", 3L);
        map.put("validatedAnswerId", 101L);
        map.put("title", "Sample Question Title");
        map.put("tags", new ArrayList<>(List.of("java", "testing")));

        // Act: Convert the map to a Question object
        Question result = questionAdapter.fromMap(map);

        // Assert: Check the fields in the resulting Question object
        assertEquals(1L, result.getId());
        assertEquals("user123", result.getCreatorUserName());
        assertEquals("This is a sample question content.", result.getMdContent());
        assertNotNull(result.getPostedOn());
        assertEquals(201L, result.getCommunityId());
        assertEquals(10L, result.getUpVotes());
        assertEquals(3L, result.getDownVotes());
        assertEquals(101L, result.getValidatedAnswerId());
        assertEquals("Sample Question Title", result.getTitle());
        assertEquals(List.of("java", "testing"), result.getTags());
    }

    @Test
    void testToJson() {
        // Arrange: Create a Question object
        Question question = Question.builder()
                .id(1L)
                .creatorUserName("user123")
                .mdContent("This is a sample question content.")
                .postedOn(new Date())
                .communityId(201L)
                .upVotes(10L)
                .downVotes(3L)
                .validatedAnswerId(101L)
                .title("Sample Question Title")
                .tags(new ArrayList<>(List.of("java", "testing")))
                .build();

        // Act: Convert the Question object to a JSON string
        String json = questionAdapter.toJson(question);

        // Assert: Check that the JSON string is not null or empty
        assertNotNull(json);
        assertTrue(json.contains("\"id\":1"));
        assertTrue(json.contains("\"creatorUserName\":\"user123\""));
        assertTrue(json.contains("\"title\":\"Sample Question Title\""));
        assertTrue(json.contains("\"tags\":[\"java\",\"testing\"]"));
    }
}
