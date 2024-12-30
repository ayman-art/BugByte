package com.example.BugByte_backend.ServicesTests;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.BugByte_backend.models.Community;
import com.example.BugByte_backend.models.User;
import com.example.BugByte_backend.repositories.CommunityRepository;
import com.example.BugByte_backend.repositories.TagsRepository;
import com.example.BugByte_backend.services.CommunityService;
import com.example.BugByte_backend.services.SearchingFilteringCommunityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class CommunityServiceTest {

    @Mock
    private CommunityRepository communityRepository;

    @Mock
    private SearchingFilteringCommunityService searchingFilteringCommunityService;

    @Mock
    private TagsRepository tagsRepository;
    @InjectMocks
    private CommunityService communityService;

    private Community community;

    @BeforeEach
    public void setUp() {
        community = new Community();
        community.setId(1L);
        community.setName("Test Community");
        community.setAdminId(100L);
    }
    @Test
    public void testCreateCommunity_Success() {
        Community community = new Community();
        community.setName("Test Community");
        community.setAdminId(1L);
        community.setDescription("This is a test community");
        community.setTags(Arrays.asList("tag1", "tag2"));
        Long expectedCommunityId = 1L;

        when(communityRepository.insertCommunity(community.getName(), community.getAdminId())).thenReturn(expectedCommunityId);
        when(communityRepository.updateCommunityDescription(eq(expectedCommunityId), eq(community.getDescription()))).thenReturn(true);
        when(tagsRepository.bulkAddTagsToCommunity(eq(expectedCommunityId), anyList())).thenReturn(1);
        when(searchingFilteringCommunityService.saveCommunity(community)).thenReturn(community);

        Long communityId = communityService.createCommunity(community);
        assertEquals(expectedCommunityId, communityId);
    }
    @Test
    public void CreateCommunity_Failure1() {
        assertThrows(NullPointerException.class, () -> communityService.createCommunity(null));
    }


    @Test
    public void testUpdateCommunity() {
        when(communityRepository.updateCommunityNameAndDescription(community)).thenReturn(true);

        boolean result = communityService.updateCommunity(community);

        assertTrue(result);

    }

    @Test
    public void testUpdateCommunityFails() {
        assertThrows(NullPointerException.class, () -> communityService.updateCommunity(null));
    }

    @Test
    public void testUpdateCommunityDescription() {
        when(communityRepository.updateCommunityDescription(1L, "New Description")).thenReturn(true);

        boolean result = communityService.updateCommunityDescription(1L, "New Description");

        assertTrue(result);
    }
    @Test
    public void testUpdateCommunityDescription_Failure1() {
        boolean result = communityService.updateCommunityDescription(1L, null);
        assertFalse(result);
    }
    @Test
    public void testUpdateCommunityDescription_Failure2() {
        boolean result = communityService.updateCommunityDescription(null, "New Description");
        assertFalse(result);
    }
    @Test
    public void testUpdateCommunityDescription_Failure3() {
        boolean result = communityService.updateCommunityDescription(null, null);
        assertFalse(result);
    }


    @Test
    public void testDeleteMember() {
        when(communityRepository.deleteMemberByUsername(1L, "user123")).thenReturn(true);

        boolean result = communityService.deleteMember(1L, "user123");

        assertTrue(result);
    }

    @Test
    public void testJoinCommunity() {
        when(communityRepository.insertMember(100L, 1L)).thenReturn(true);

        boolean result = communityService.joinCommunity(1L, 100L);

        assertTrue(result);
    }

    @Test
    public void testGetCommunityAdmins() {
        List<User> admins = new ArrayList<>();
        User admin1 = new User();
        admin1.setId(100L);
        admin1.setUserName("admin1");
        admins.add(admin1);

        when(communityRepository.findModeratorsByCommunityId(1L)).thenReturn(admins);

        List<User> result = communityService.getCommunityAdmins(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("admin1", result.get(0).getUserName());
    }

    @Test
    public void testDeleteCommunity_Success() {
        Long communityId = 1L;

        when(communityRepository.deleteCommunityById(communityId)).thenReturn(true);
        boolean result = communityService.deleteCommunity(communityId);
        assertTrue(result);
    }
    @Test
    public void testDeleteCommunity_Failure() {
        Long communityId = 1L;
        when(communityRepository.deleteCommunityById(communityId)).thenReturn(false);
        boolean result = communityService.deleteCommunity(communityId);
        assertFalse(result);
    }

    @Test
    public void testGetUserCommunities_Success() {
        Long userId = 1L;

        List<Community> mockedCommunities = new ArrayList<>();
        Community community1 = new Community();
        community1.setId(1L);
        community1.setName("Community 1");

        Community community2 = new Community();
        community2.setId(2L);
        community2.setName("Community 2");

        mockedCommunities.add(community1);
        mockedCommunities.add(community2);

        when(communityRepository.getUserCommunities(userId)).thenReturn(mockedCommunities);

        List<String> tags1 = Arrays.asList("tag1", "tag2");
        List<String> tags2 = Arrays.asList("tag3", "tag4");

        when(tagsRepository.findTagsByCommunity(1L)).thenReturn(tags1);
        when(tagsRepository.findTagsByCommunity(2L)).thenReturn(tags2);

        List<Community> communities = communityService.getUserCommunities(userId);

        assertEquals(2, communities.size());
        assertEquals(tags1, communities.get(0).getTags());
        assertEquals(tags2, communities.get(1).getTags());
    }

    @Test
    public void testGetUserCommunities_Exception() {
        Long userId = 1L;

        when(communityRepository.getUserCommunities(userId)).thenThrow(new RuntimeException("Database error"));

        try {
            communityService.getUserCommunities(userId);
            fail("Exception should have been thrown");
        } catch (RuntimeException e) {
            assertEquals("Database error", e.getMessage());
        }

    }
    @Test
    public void testGetAllCommunities_Success() {
        int pageSize = 5;
        int pageNumber = 1;

        List<Community> mockedCommunities = new ArrayList<>();
        Community community1 = new Community();
        community1.setId(1L);
        community1.setName("Community 1");

        Community community2 = new Community();
        community2.setId(2L);
        community2.setName("Community 2");

        mockedCommunities.add(community1);
        mockedCommunities.add(community2);

        when(communityRepository.findAllCommunities(pageSize, pageNumber)).thenReturn(mockedCommunities);

        List<String> tags1 = Arrays.asList("tag1", "tag2");
        List<String> tags2 = Arrays.asList("tag3", "tag4");

        when(tagsRepository.findTagsByCommunity(1L)).thenReturn(tags1);
        when(tagsRepository.findTagsByCommunity(2L)).thenReturn(tags2);

        List<Community> communities = communityService.getAllCommunities(pageSize, pageNumber);

        assertEquals(2, communities.size());
        assertEquals(tags1, communities.get(0).getTags());
        assertEquals(tags2, communities.get(1).getTags());
    }

    @Test
    public void testGetAllCommunities_Exception() {
        int pageSize = 5;
        int pageNumber = 1;

        when(communityRepository.findAllCommunities(pageSize, pageNumber)).thenThrow(new RuntimeException("Database error"));

        try {
            communityService.getAllCommunities(pageSize, pageNumber);
            fail("Exception should have been thrown");
        } catch (RuntimeException e) {
            assertEquals("Database error", e.getMessage());
        }

    }
    @Test
    public void testDeleteMember_Success() {
        Long communityId = 1L;
        String username = "testUser";

        when(communityRepository.deleteMemberByUsername(communityId, username)).thenReturn(true);

        boolean result = communityService.deleteMember(communityId, username);

        assertTrue(result);
    }

    @Test
    public void testDeleteMember_Failure() {
        Long communityId = 1L;
        String username = "testUser";
        when(communityRepository.deleteMemberByUsername(communityId, username)).thenReturn(false);

        boolean result = communityService.deleteMember(communityId, username);
        assertFalse(result);
    }

    @Test
    public void testJoinCommunity_Success() {
        Long communityId = 1L;
        Long userId = 2L;

        when(communityRepository.insertMember(userId, communityId)).thenReturn(true);

        boolean result = communityService.joinCommunity(communityId, userId);

        assertTrue(result);
    }

    @Test
    public void testJoinCommunity_Failure() {
        Long communityId = 1L;
        Long userId = 2L;

        when(communityRepository.insertMember(userId, communityId)).thenReturn(false);

        boolean result = communityService.joinCommunity(communityId, userId);
        assertFalse(result);

    }
    @Test
    public void testGetCommunityById_Success() {
        Long communityId = 1L;
        Community community = new Community();
        community.setId(communityId);

        when(communityRepository.findCommunityById(communityId)).thenReturn(community);
        when(tagsRepository.findTagsByCommunity(communityId)).thenReturn(new ArrayList<>());

        Community result = communityService.getCommunityById(communityId);

        assertEquals(communityId, result.getId());
    }

    @Test
    public void testGetCommunityById_Exception() {
        Long communityId = 1L;

        when(communityRepository.findCommunityById(communityId)).thenThrow(new IllegalArgumentException("Invalid ID"));

        assertThrows(IllegalArgumentException.class, () -> communityService.getCommunityById(communityId));
    }
    @Test
    public void testGetCommunityAdmins_Success() {
        Long communityId = 1L;
        List<User> admins = new ArrayList<>();
        User admin1 = new User();
        User admin2 = new User();
        admin2.setUserName("admin2");
        admin1.setUserName("admin1");

        admins.add(admin1);
        admins.add(admin2);

        when(communityRepository.findModeratorsByCommunityId(communityId)).thenReturn(admins);

        List<User> result = communityService.getCommunityAdmins(communityId);

        assertEquals(2, result.size());
        assertEquals("admin1", result.get(0).getUserName());
        assertEquals("admin2", result.get(1).getUserName());
    }

    @Test
    public void testGetCommunityAdmins_Exception() {
        Long communityId = 1L;

        when(communityRepository.findModeratorsByCommunityId(communityId)).thenThrow(new RuntimeException("Database error"));

        try {
            communityService.getCommunityAdmins(communityId);
            fail("Exception should have been thrown");
        } catch (RuntimeException e) {
            assertEquals("Database error", e.getMessage());
        }

    }

    @Test
    public void testLeaveCommunity_Success() {
        String communityName = "testCommunity";
        Long memberId = 1L;

        when(communityRepository.leaveCommunity(communityName, memberId)).thenReturn(true);

        boolean result = communityService.leaveCommunity(communityName, memberId);
        assertTrue(result);

    }

    @Test
    public void testLeaveCommunity_Failure() {
        String communityName = "testCommunity";
        Long memberId = 1L;

        when(communityRepository.leaveCommunity(communityName, memberId)).thenReturn(false);

        boolean result = communityService.leaveCommunity(communityName, memberId);

        assertFalse(result);
    }


}
