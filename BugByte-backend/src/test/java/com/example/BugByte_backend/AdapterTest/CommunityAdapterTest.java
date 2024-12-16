package com.example.BugByte_backend.AdapterTest;

import com.example.BugByte_backend.Adapters.CommunityAdapter;
import com.example.BugByte_backend.Adapters.UserAdapter;
import com.example.BugByte_backend.models.Community;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import java.util.Date;
import java.util.Map;

import static graphql.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommunityAdapterTest {
    private final CommunityAdapter communityAdapter = new CommunityAdapter();

    @Test
    public void testToMap() {
        Community community = Community.builder()
                .name("TechForum")
                .adminId(1L)
                .description("A forum for tech enthusiasts.")
                .creationDate(new Date(2024, 12, 14, 10, 0))
                .build();

        Map<String, Object> communityMap = communityAdapter.toMap(community);

        assertEquals("TechForum", communityMap.get("name"));
        assertEquals(1L, communityMap.get("admin_id"));
        assertEquals("A forum for tech enthusiasts.", communityMap.get("description"));
        assertEquals(new Date(2024, 12, 14, 10, 0), communityMap.get("creation_date"));
    }
    @Test
    public void testFromMap() {
        Map<String, Object> communityMap = Map.of(
                "name", "TechForum",
                "admin_id", 1L,
                "description", "A forum for tech enthusiasts.",
                "creation_date", new Date()
        );

        Community community = communityAdapter.fromMap(communityMap);

        assertEquals("TechForum", community.getName());
        assertEquals(1L, community.getAdminId());
        assertEquals("A forum for tech enthusiasts.", community.getDescription());
        assertEquals(new Date(), community.getCreationDate());
    }

//    @Test
//    public void testToJson() throws JsonProcessingException {
//        Community community = Community.builder()
//                .id(2L)
//                .name("Community2")
//                .adminId(null)
//                .description(null)
//                .creationDate(new Date())
//                .build();
//
//        String communityString = communityAdapter.toJson(community);
//
//        String expected = "{\"id\":2,\"name\":\"Community2\",\"adminId\":null," +
//                "\"description\":null,\"creationDate\":\"" + community.getCreationDate().toString()+ "\"}";
//
//        assertEquals(expected, communityString);
//    }


//    @Test
//    public void testToJsonWithNullFields() throws JsonProcessingException {
//        ObjectMapper objectMapper = new ObjectMapper();
//        Date creationDate = new Date();
//
//        Community community = Community.builder()
//                .id(2L)
//                .name("Community2")
//                .adminId(null)
//                .description(null)
//                .creationDate(creationDate)
//                .build();
//
//        String json = communityAdapter.toJson(community);
//
//        Map<String, Object> jsonMap = objectMapper.readValue(json, Map.class);
//
//        assertEquals("Community2", jsonMap.get("name"));
//        assertNull(jsonMap.get("adminId")); // Null field handling
//        assertNull(jsonMap.get("description"));
//        assertEquals(creationDate.toLocaleString(), jsonMap.get("creationDate"));
//        assertEquals(2L, jsonMap.get("id"));
//    }



}
