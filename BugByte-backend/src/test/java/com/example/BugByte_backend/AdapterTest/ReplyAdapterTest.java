package com.example.BugByte_backend.AdapterTest;

import static org.junit.jupiter.api.Assertions.*;

import com.example.BugByte_backend.Adapters.ReplyAdapter;
import com.example.BugByte_backend.models.Reply;
import org.junit.jupiter.api.Test;
import java.util.*;

class ReplyAdapterTest {

    private final ReplyAdapter replyAdapter = new ReplyAdapter();

    @Test
    void testToMap() {
        // Arrange: Create a Reply object
        Reply reply = Reply.builder()
                .id(1L)
                .creatorUserName("user123")
                .mdContent("This is a sample reply content.")
                .postedOn(new Date())
                .answerId(101L)
                .build();

        // Act: Convert the Reply object to a map
        Map<String, Object> result = replyAdapter.toMap(reply);

        // Assert: Check the map contains the expected values
        assertEquals(1L, result.get("replyId"));
        assertEquals("user123", result.get("opName"));
        assertEquals("This is a sample reply content.", result.get("mdContent"));
        assertNotNull(result.get("postedOn"));
        assertEquals(101L, result.get("answerId"));
    }

    @Test
    void testFromMap() {
        // Arrange: Create a map with sample data
        Map<String, Object> map = new HashMap<>();
        map.put("replyId", 1L);
        map.put("opName", "user123");
        map.put("mdContent", "This is a sample reply content.");
        map.put("postedOn", new Date());
        map.put("answerId", 101L);

        // Act: Convert the map to a Reply object
        Reply result = replyAdapter.fromMap(map);

        // Assert: Check the fields in the resulting Reply object
        assertEquals(1L, result.getId());
        assertEquals("user123", result.getCreatorUserName());
        assertEquals("This is a sample reply content.", result.getMdContent());
        assertNotNull(result.getPostedOn());
        assertEquals(101L, result.getAnswerId());
    }

    @Test
    void testToJson() {
        // Arrange: Create a Reply object
        Reply reply = Reply.builder()
                .id(1L)
                .creatorUserName("user123")
                .mdContent("This is a sample reply content.")
                .postedOn(new Date())
                .answerId(101L)
                .build();

        // Act: Convert the Reply object to a JSON string
        String json = replyAdapter.toJson(reply);

        // Assert: Check that the JSON string is not null or empty
        assertNotNull(json);
        assertTrue(json.contains("\"id\":1"));
        assertTrue(json.contains("\"creatorUserName\":\"user123\""));
        assertTrue(json.contains("\"mdContent\":\"This is a sample reply content.\""));
        assertTrue(json.contains("\"answerId\":101"));
    }
}
