package com.example.BugByte_backend.AdapterTest;

import com.example.BugByte_backend.Adapters.CommunityAdapter;
import com.example.BugByte_backend.Adapters.UserAdapter;
import com.example.BugByte_backend.models.Community;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import static graphql.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CommunityAdapterTest {
    private final CommunityAdapter communityAdapter = new CommunityAdapter();

    @Test
    public void testToMap() {
        ArrayList<String> tags = new ArrayList<String>();
        tags.add("tags");
        Community community = Community.builder()
                .name("TechForum")
                .adminId(1L)
                .description("A forum for tech enthusiasts.")
                .creationDate(new Date(2024, 12, 14, 10, 0))
                .id(2L)
                .tags(tags)
                .build();

        Map<String, Object> communityMap = communityAdapter.toMap(community);

        assertEquals("TechForum", communityMap.get("name"));
        assertEquals("A forum for tech enthusiasts.", communityMap.get("description"));

    }
    @Test
    public void testToMapNullCommunity() {
        assertThrows(NullPointerException.class, () -> communityAdapter.toMap(null));
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
    }
    @Test
    public void NullMap_testFromMap() {
        assertThrows(NullPointerException.class, () -> communityAdapter.fromMap(null));
    }

}
