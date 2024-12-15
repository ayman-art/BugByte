package com.example.BugByte_backend.facades;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.BugByte_backend.facades.AdministrativeFacade;
import com.example.BugByte_backend.models.Community;
import com.example.BugByte_backend.models.User;
import com.example.BugByte_backend.services.CommunityService;
import com.example.BugByte_backend.services.ModeratorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

public class AdministrativeFacadeCommunityTest {

    @Mock
    private CommunityService communityService;

    @Mock
    private ModeratorService moderatorService;

    @InjectMocks
    private AdministrativeFacade administrativeFacade;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetCommunityInfo() {
        Long communityId = 1L;
        Community community = new Community(communityId, "Community1", "Description");
        when(communityService.getCommunityById(communityId)).thenReturn(community);

        Map<String, Object> communityInfo = administrativeFacade.getCommunityInfo(communityId);

        assertNotNull(communityInfo);
        assertEquals("Community1", communityInfo.get("name"));
        assertEquals("Description", communityInfo.get("description"));
    }

    @Test
    public void testCreateCommunity() {
        Map<String, Object> communityMap = new HashMap<>();
        communityMap.put("name", "Community2");
        communityMap.put("description", "Description");

        when(communityService.createCommunity(any(Community.class))).thenReturn(1L);

        boolean result = administrativeFacade.createCommunity(communityMap);

        assertTrue(result);
    }

    @Test
    public void testDeleteCommunity() {
        Long communityId = 1L;
        when(communityService.deleteCommunity(communityId)).thenReturn(true);

        boolean result = administrativeFacade.deleteCommunity(communityId);

        assertTrue(result);
    }

    @Test
    public void testEditCommunity() {
        Long communityId = 1L;
        Map<String, Object> updateData = new HashMap<>();
        updateData.put("name", "Updated Community");
        updateData.put("description", "Updated Description");

        when(communityService.updateCommunity(any(Community.class))).thenReturn(true);

        boolean result = administrativeFacade.editCommunity(communityId, updateData);

        assertTrue(result);
    }

    @Test
    public void testSetModerator() {
        Map<String, Object> req = new HashMap<>();
        req.put("moderator_id", 1L);
        req.put("community_id", 1L);

        when(moderatorService.setModerator(1L, 1L)).thenReturn(true);

        boolean result = administrativeFacade.setModerator(req);

        assertTrue(result);
    }

    @Test
    public void testRemoveModerator() {
        Map<String, Object> req = new HashMap<>();
        req.put("moderator_id", 1L);
        req.put("community_id", 1L);

        when(moderatorService.removeModerator(1L, 1L)).thenReturn(true);

        boolean result = administrativeFacade.removeModerator(req);

        assertTrue(result);
    }

    @Test
    public void testRemoveMember() {
        Map<String, Object> req = new HashMap<>();
        req.put("community_id", 1L);
        req.put("user_name", "user1");

        when(communityService.deleteMember(1L, "user1")).thenReturn(true);

        boolean result = administrativeFacade.removeMember(req);

        assertTrue(result);
    }

    @Test
    public void testGetAdmins() {
        Long communityId = 1L;
        List<User> admins = new ArrayList<>();
        User admin = User.builder().userName("admin1").build();
        admins.add(admin);

        when(communityService.getCommunityAdmins(communityId)).thenReturn(admins);

        List<Map<String, Object>> adminsMap = administrativeFacade.getAdmins(communityId);

        assertNotNull(adminsMap);
        assertFalse(adminsMap.isEmpty());
        assertEquals("admin1", adminsMap.get(0).get("userName"));
        assertFalse(adminsMap.get(0).containsKey("password"));
        assertFalse(adminsMap.get(0).containsKey("email"));
    }

    @Test
    public void testJoinCommunity() {
        Map<String, Object> req = new HashMap<>();
        req.put("community_id", 1L);
        req.put("id", 1L);

        when(communityService.joinCommunity(1L, 1L)).thenReturn(true);

        boolean result = administrativeFacade.joinCommunity(req);

        assertTrue(result);
    }
}
