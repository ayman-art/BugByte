package com.example.BugByte_backend.Controllers;

import com.example.BugByte_backend.controllers.CommunityController;
import com.example.BugByte_backend.facades.AdministrativeFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CommunityControllerTest {

    @Mock
    private AdministrativeFacade administrativeFacade;

    @InjectMocks
    private CommunityController communityController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetCommunity_Success() {
        Map<String, Object> input = new HashMap<>();
        input.put("communityId", "123");

        Map<String, Object> response = new HashMap<>();
        response.put("name", "Tech Community");

        when(administrativeFacade.getCommunityInfo(input)).thenReturn(response);

        ResponseEntity<?> result = communityController.getCommunity(input);

        assertEquals(200, result.getStatusCodeValue());
        assertEquals(response, result.getBody());
        verify(administrativeFacade, times(1)).getCommunityInfo(input);
    }

    @Test
    void testGetCommunity_Failure() {
        Map<String, Object> input = new HashMap<>();
        input.put("communityId", "123");

        when(administrativeFacade.getCommunityInfo(input)).thenThrow(new RuntimeException("Community not found"));

        ResponseEntity<?> result = communityController.getCommunity(input);

        assertEquals(400, result.getStatusCodeValue());
        assertEquals("Community not found", result.getBody());
        verify(administrativeFacade, times(1)).getCommunityInfo(input);
    }

//    @Test
//    void testCreateCommunity_Success() {
//        Map<String, Object> input = new HashMap<>();
//        input.put("name", "New Community");
//
//        when(administrativeFacade.createCommunity(input)).thenReturn(true);
//
//        ResponseEntity<?> result = communityController.createCommunity(input);
//
//        assertEquals(200, result.getStatusCodeValue());
//        assertEquals("Community Created Successfully", result.getBody());
//        verify(administrativeFacade, times(1)).createCommunity(input);
//    }
//
//    @Test
//    void testCreateCommunity_Failure() {
//        Map<String, Object> input = new HashMap<>();
//        input.put("name", "New Community");
//
//        when(administrativeFacade.createCommunity(input)).thenReturn(false);
//
//        ResponseEntity<?> result = communityController.createCommunity(input);
//
//        assertEquals(400, result.getStatusCodeValue());
//        assertEquals("error creating community", result.getBody());
//        verify(administrativeFacade, times(1)).createCommunity(input);
//    }

    @Test
    void testDeleteCommunity_Success() {
        Map<String, Object> input = new HashMap<>();
        input.put("communityId", "123");

        when(administrativeFacade.deleteCommunity(input)).thenReturn(true);

        ResponseEntity<?> result = communityController.deleteCommunity(input);

        assertEquals(200, result.getStatusCodeValue());
        assertEquals("Community deleted Successfully", result.getBody());
        verify(administrativeFacade, times(1)).deleteCommunity(input);
    }

    @Test
    void testDeleteCommunity_Failure() {
        Map<String, Object> input = new HashMap<>();
        input.put("communityId", "123");

        when(administrativeFacade.deleteCommunity(input)).thenReturn(false);

        ResponseEntity<?> result = communityController.deleteCommunity(input);

        assertEquals(400, result.getStatusCodeValue());
        assertEquals("error deleting community", result.getBody());
        verify(administrativeFacade, times(1)).deleteCommunity(input);
    }

    @Test
    void testEditCommunity_Success() {
        Map<String, Object> input = new HashMap<>();
        input.put("communityId", "123");
        input.put("name", "Updated Community");

        when(administrativeFacade.editCommunity(input)).thenReturn(true);

        ResponseEntity<?> result = communityController.editCommunity(input);

        assertEquals(200, result.getStatusCodeValue());
        assertEquals("Community edited Successfully", result.getBody());
        verify(administrativeFacade, times(1)).editCommunity(input);
    }

    @Test
    void testEditCommunity_Failure() {
        Map<String, Object> input = new HashMap<>();
        input.put("communityId", "123");
        input.put("name", "Updated Community");

        when(administrativeFacade.editCommunity(input)).thenReturn(false);

        ResponseEntity<?> result = communityController.editCommunity(input);

        assertEquals(400, result.getStatusCodeValue());
        assertEquals("error editing community", result.getBody());
        verify(administrativeFacade, times(1)).editCommunity(input);
    }

    @Test
    void testSetModerator_Success() {
        Map<String, Object> input = new HashMap<>();
        input.put("userId", "456");
        input.put("communityId", "123");

        when(administrativeFacade.setModerator(input)).thenReturn(true);

        ResponseEntity<?> result = communityController.setModerator(input);

        assertEquals(200, result.getStatusCodeValue());
        assertEquals("User is Moderator now", result.getBody());
        verify(administrativeFacade, times(1)).setModerator(input);
    }

    @Test
    void testSetModerator_Failure() {
        Map<String, Object> input = new HashMap<>();
        input.put("userId", "456");
        input.put("communityId", "123");

        when(administrativeFacade.setModerator(input)).thenReturn(false);

        ResponseEntity<?> result = communityController.setModerator(input);

        assertEquals(400, result.getStatusCodeValue());
        assertEquals("error adding moderator", result.getBody());
        verify(administrativeFacade, times(1)).setModerator(input);
    }

    @Test
    void testRemoveModerator_Success() {
        Map<String, Object> input = new HashMap<>();
        input.put("userId", "456");
        input.put("communityId", "123");

        when(administrativeFacade.removeModerator(input)).thenReturn(true);

        ResponseEntity<?> result = communityController.removeModerator(input);

        assertEquals(200, result.getStatusCodeValue());
        assertEquals("moderator removed Successfully", result.getBody());
        verify(administrativeFacade, times(1)).removeModerator(input);
    }

    @Test
    void testRemoveModerator_Failure() {
        Map<String, Object> input = new HashMap<>();
        input.put("userId", "456");
        input.put("communityId", "123");

        when(administrativeFacade.removeModerator(input)).thenReturn(false);

        ResponseEntity<?> result = communityController.removeModerator(input);

        assertEquals(400, result.getStatusCodeValue());
        assertEquals("error removing moderator", result.getBody());
        verify(administrativeFacade, times(1)).removeModerator(input);
    }

    @Test
    void testRemoveMember_Success() {
        Map<String, Object> input = new HashMap<>();
        input.put("userId", "456");
        input.put("communityId", "123");

        when(administrativeFacade.removeMember(input)).thenReturn(true);

        ResponseEntity<?> result = communityController.removeMember(input);

        assertEquals(200, result.getStatusCodeValue());
        assertEquals("member removed Successfully", result.getBody());
        verify(administrativeFacade, times(1)).removeMember(input);
    }

    @Test
    void testRemoveMember_Failure() {
        Map<String, Object> input = new HashMap<>();
        input.put("userId", "456");
        input.put("communityId", "123");

        when(administrativeFacade.removeMember(input)).thenReturn(false);

        ResponseEntity<?> result = communityController.removeMember(input);

        assertEquals(400, result.getStatusCodeValue());
        assertEquals("error removing member", result.getBody());
        verify(administrativeFacade, times(1)).removeMember(input);
    }

    @Test
    void testGetAdmins_Success() throws Exception {
        Map<String, Object> input = new HashMap<>();
        input.put("communityId", "123");

        List<Map<String, Object>> admins = List.of(Map.of("userId", "1"), Map.of("userId", "2"));

        when(administrativeFacade.getAdmins(input)).thenReturn(admins);

        ResponseEntity<?> result = communityController.getAdmins(input);

        assertEquals(200, result.getStatusCodeValue());
        assertEquals(Map.of("adminsList", admins), result.getBody());
        verify(administrativeFacade, times(1)).getAdmins(input);
    }

    @Test
    void testGetAdmins_Failure() throws Exception {
        Map<String, Object> input = new HashMap<>();
        input.put("communityId", "123");

        when(administrativeFacade.getAdmins(input)).thenThrow(new RuntimeException("No admins found"));

        ResponseEntity<?> result = communityController.getAdmins(input);

        assertEquals(400, result.getStatusCodeValue());
        assertEquals("No admins found", result.getBody());
        verify(administrativeFacade, times(1)).getAdmins(input);
    }

    @Test
    void testJoinCommunity_Success() {
        Map<String, Object> input = new HashMap<>();
        input.put("userId", "456");
        input.put("communityId", "123");

        when(administrativeFacade.joinCommunity(input)).thenReturn(true);

        ResponseEntity<?> result = communityController.joinCommunity(input);

        assertEquals(200, result.getStatusCodeValue());
        assertEquals("user joined  Successfully", result.getBody());
        verify(administrativeFacade, times(1)).joinCommunity(input);
    }

    @Test
    void testJoinCommunity_Failure() {
        Map<String, Object> input = new HashMap<>();
        input.put("userId", "456");
        input.put("communityId", "123");

        when(administrativeFacade.joinCommunity(input)).thenReturn(false);

        ResponseEntity<?> result = communityController.joinCommunity(input);

        assertEquals(400, result.getStatusCodeValue());
        assertEquals("error joining community", result.getBody());
        verify(administrativeFacade, times(1)).joinCommunity(input);
    }
}
