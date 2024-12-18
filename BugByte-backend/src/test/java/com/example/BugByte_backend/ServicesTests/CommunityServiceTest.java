package com.example.BugByte_backend.ServicesTests;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.BugByte_backend.models.Community;
import com.example.BugByte_backend.models.User;
import com.example.BugByte_backend.repositories.CommunityRepository;
import com.example.BugByte_backend.services.CommunityService;
import com.example.BugByte_backend.services.SearchingFilteringCommunityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class CommunityServiceTest {

    @Mock
    private CommunityRepository communityRepository;

    @Mock
    private SearchingFilteringCommunityService searchingFilteringCommunityService;

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
    public void testCreateCommunity() {
        when(communityRepository.insertCommunity("Test Community", 100L)).thenReturn(1L);
        Long communityId = communityService.createCommunity(community);

        assertEquals(1L, communityId);
        verify(communityRepository, times(1)).insertCommunity("Test Community", 100L);
        verify(searchingFilteringCommunityService, times(1)).saveCommunity(community);
    }

    @Test
    public void testUpdateCommunity() {
        when(communityRepository.updateCommunityNameAndDescription(community)).thenReturn(true);

        boolean result = communityService.updateCommunity(community);

        assertTrue(result);
        verify(communityRepository, times(1)).updateCommunityNameAndDescription(community);
        verify(searchingFilteringCommunityService, times(1)).saveCommunity(community);
    }

    @Test
    public void testUpdateCommunityFails() {
        when(communityRepository.updateCommunityNameAndDescription(community)).thenReturn(false);

        boolean result = communityService.updateCommunity(community);

        assertFalse(result);
        verify(communityRepository, times(1)).updateCommunityNameAndDescription(community);
    }

    @Test
    public void testGetCommunityById() {
        when(communityRepository.findCommunityById(1L)).thenReturn(community);

        Community result = communityService.getCommunityById(1L);

        assertNotNull(result);
        assertEquals("Test Community", result.getName());
        verify(communityRepository, times(1)).findCommunityById(1L);
    }

    @Test
    public void testUpdateCommunityDescription() {
        when(communityRepository.updateCommunityDescription(1L, "New Description")).thenReturn(true);

        boolean result = communityService.updateCommunityDescription(1L, "New Description");

        assertTrue(result);
        verify(communityRepository, times(1)).updateCommunityDescription(1L, "New Description");
    }

    @Test
    public void testDeleteMember() {
        when(communityRepository.deleteMemberByUsername(1L, "user123")).thenReturn(true);

        boolean result = communityService.deleteMember(1L, "user123");

        assertTrue(result);
        verify(communityRepository, times(1)).deleteMemberByUsername(1L, "user123");
    }

    @Test
    public void testJoinCommunity() {
        when(communityRepository.insertMember(100L, 1L)).thenReturn(true);

        boolean result = communityService.joinCommunity(1L, 100L);

        assertTrue(result);
        verify(communityRepository, times(1)).insertMember(100L, 1L);
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
        verify(communityRepository, times(1)).findModeratorsByCommunityId(1L);
    }

    @Test
    public void testDeleteCommunity() {
        when(communityRepository.deleteCommunityById(1L)).thenReturn(true);
        when(communityRepository.findCommunityById(1L)).thenReturn(community);

        boolean result = communityService.deleteCommunity(1L);

        assertTrue(result);
        verify(communityRepository, times(1)).deleteCommunityById(1L);
        verify(searchingFilteringCommunityService, times(1)).deleteCommunity(community);
    }

    @Test
    public void testGetUserCommunities() {
        List<Community> communities = new ArrayList<>();
        communities.add(community);

        when(communityRepository.getUserCommunities(100L)).thenReturn(communities);

        List<Community> result = communityService.getUserCommunities(100L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Community", result.get(0).getName());
        verify(communityRepository, times(1)).getUserCommunities(100L);
    }
}
