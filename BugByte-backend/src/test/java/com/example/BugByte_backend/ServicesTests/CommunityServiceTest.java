package com.example.BugByte_backend.ServicesTests;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.BugByte_backend.models.Community;
import com.example.BugByte_backend.repositories.CommunityRepository;
import com.example.BugByte_backend.services.CommunityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CommunityServiceTest {

    @Mock
    private CommunityRepository communityRepository;

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
        Long communityId = communityService.createCommunity(new Community("Test Community", 100L));
        assertNotNull(communityId);
        assertEquals(1L, communityId);
    }

    @Test
    public void testCreateCommunityAlreadyExists() {
        when(communityRepository.insertCommunity("Test Community",100L))
                .thenThrow(new RuntimeException("Community with this name already exists."));
        Exception exception = assertThrows(RuntimeException.class, () ->
                communityService.createCommunity(new Community("Test Community", 100L))
        );
        assertEquals("Community with this name already exists.", exception.getMessage());
    }

    @Test
    public void testDeleteCommunity() {
        when(communityRepository.deleteCommunityById(1L)).thenReturn(true);
        boolean result = communityService.deleteCommunity(1L);
        assertTrue(result);
    }

    @Test
    public void testDeleteCommunityNotFound() {
        when(communityRepository.deleteCommunityById(11L)).thenReturn(false);
        boolean result =communityService.deleteCommunity(11L);
        assertEquals(result,false);
    }
    @Test
    public void testSetModerator() {
        when(communityRepository.setModerator(200L, "1")).thenReturn(true);

        boolean result = communityService.setModerator(200L, "1");

        assertTrue(result);
        verify(communityRepository, times(1)).setModerator(200L, "1");
    }
    @Test
    public void testSetModeratorFails() {
        when(communityRepository.setModerator(200L, "1")).thenReturn(false);

        boolean result = communityService.setModerator(200L, "1");

        assertFalse(result);
        verify(communityRepository, times(1)).setModerator(200L, "1");
    }

    @Test
    public void testRemoveModerator() {
        when(communityRepository.removeModerator(200L, "1")).thenReturn(true);

        boolean result = communityService.removeModerator(200L, "1");

        assertTrue(result);
        verify(communityRepository, times(1)).removeModerator(200L, "1");
    }

    @Test
    public void testRemoveModeratorFails() {
        when(communityRepository.removeModerator(200L, "1")).thenReturn(false);

        boolean result = communityService.removeModerator(200L, "1");

        assertFalse(result);
        verify(communityRepository, times(1)).removeModerator(200L, "1");
    }
}