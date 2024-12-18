package com.example.BugByte_backend.AdapterTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.BugByte_backend.Adapters.AnswerAdapter;
import com.example.BugByte_backend.models.Answer;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

class AnswerAdapterTest {
    private final AnswerAdapter answerAdapter = new AnswerAdapter();

    @Test
    void testToMap() {
        // Arrange: Create an Answer object
        Answer answer = Answer.builder()
                .id(1L)
                .creatorUserName("user123")
                .mdContent("This is an example content.")
                .postedOn(new Date())
                .questionId(101L)
                .upVotes(5L)
                .downVotes(2L)
                .build();

        // Act: Convert to a map
        Map<String, Object> result = answerAdapter.toMap(answer);

        // Assert: Check the map contains the expected values
        assertEquals(1L, result.get("answerId"));
        assertEquals("user123", result.get("opName"));
        assertEquals("This is an example content.", result.get("mdContent"));
        assertNotNull(result.get("postedOn"));
        assertEquals(101L, result.get("questionId"));
        assertEquals(5L, result.get("upVotes"));
        assertEquals(2L, result.get("downVotes"));
    }

    @Test
    void testFromMap() {
        // Arrange: Create a map with sample data
        Map<String, Object> map = new HashMap<>();
        map.put("answerId", 1L);
        map.put("opName", "user123");
        map.put("mdContent", "This is an example content.");
        map.put("postedOn", new Date());
        map.put("questionId", 101L);
        map.put("upVotes", 5L);
        map.put("downVotes", 2L);

        // Act: Convert the map to an Answer object
        Answer result = answerAdapter.fromMap(map);

        // Assert: Check the fields in the resulting Answer object
        assertEquals(1L, result.getId());
        assertEquals("user123", result.getCreatorUserName());
        assertEquals("This is an example content.", result.getMdContent());
        assertNotNull(result.getPostedOn());
        assertEquals(101L, result.getQuestionId());
        assertEquals(5L, result.getUpVotes());
        assertEquals(2L, result.getDownVotes());
    }

    @Test
    void testToJson() {
        // Arrange: Create an Answer object
        Answer answer = Answer.builder()
                .id(1L)
                .creatorUserName("user123")
                .mdContent("This is an example content.")
                .postedOn(new Date())
                .questionId(101L)
                .upVotes(5L)
                .downVotes(2L)
                .build();

        // Act: Convert the Answer object to a JSON string
        String json = answerAdapter.toJson(answer);

        // Assert: Check that the JSON string is not null or empty
        assertNotNull(json);
        assertTrue(json.contains("\"id\":1"));
        assertTrue(json.contains("\"creatorUserName\":\"user123\""));
        assertTrue(json.contains("\"mdContent\":\"This is an example content.\""));
    }
}
