package com.example.BugByte_backend.Controllers;

import com.example.BugByte_backend.controllers.CommunityController;
import com.example.BugByte_backend.facades.AdministrativeFacade;
import com.example.BugByte_backend.models.Community;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class CommunityControllerTest {

    @InjectMocks
    private CommunityController communityController;

    @Mock
    private AdministrativeFacade administrativeFacade;

    private String authToken;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initializes mocks
        authToken = "Bearer testToken";
    }

    @Test
    void testGetCommunity_Success() throws Exception {
        // Arrange
        Community community = new Community();
        community.setId(1L);
        community.setName("Test Community");

        when(administrativeFacade.getCommunityInfo(any())).thenReturn(community);

        // Act
        ResponseEntity<?> response = communityController.getCommunity(authToken, "1");

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(community, response.getBody());
    }

    @Test
    void testGetCommunity_NotFound() throws Exception {
        // Arrange
        when(administrativeFacade.getCommunityInfo(any()))
                .thenThrow(new RuntimeException("Community not found"));

        // Act
        ResponseEntity<?> response = communityController.getCommunity(authToken, "1");

        // Assert
        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Community not found", response.getBody());
    }


    @Test
    void testCreateCommunity_Failure() {
        // Arrange
        when(administrativeFacade.createCommunity(any())).thenReturn(false);

        // Act
        ResponseEntity<?> response = communityController.createCommunity(Map.of("name", "Test Community"), authToken);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void testDeleteCommunity_Success() {
        // Arrange
        when(administrativeFacade.deleteCommunity(any())).thenReturn(true);

        // Act
        ResponseEntity<?> response = communityController.deleteCommunity(Map.of("communityId", "1"));

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Community deleted Successfully", response.getBody());
    }

    @Test
    void testEditCommunity_Success() {
        // Arrange
        when(administrativeFacade.editCommunity(any())).thenReturn(true);

        // Act
        ResponseEntity<?> response = communityController.editCommunity(Map.of("communityId", "1", "name", "Updated Name"));

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Community edited Successfully", response.getBody());
    }

    @Test
    void testSetModerator_Success() {
        // Arrange
        when(administrativeFacade.setModerator(any())).thenReturn(true);

        // Act
        ResponseEntity<?> response = communityController.setModerator(Map.of("communityId", "1", "userId", "2"));

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("User is Moderator now", response.getBody());
    }

    @Test
    void testRemoveModerator_Success() {
        // Arrange
        when(administrativeFacade.removeModerator(any())).thenReturn(true);

        // Act
        ResponseEntity<?> response = communityController.removeModerator(Map.of("communityId", "1", "userId", "2"));

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("moderator removed Successfully", response.getBody());
    }

    @Test
    void testRemoveMember_Success() {
        // Arrange
        when(administrativeFacade.removeMember(any())).thenReturn(true);

        // Act
        ResponseEntity<?> response = communityController.removeMember(Map.of("communityId", "1", "userId", "3"));

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("member removed Successfully", response.getBody());
    }

    @Test
    void testGetAdmins_Success() throws Exception {
        // Arrange
        List<Map<String, Object>> admins = List.of(
                Map.of("id", 1, "name", "Admin 1"),
                Map.of("id", 2, "name", "Admin 2")
        );
        when(administrativeFacade.getAdmins(any())).thenReturn(admins);

        // Act
        ResponseEntity<?> response = communityController.getAdmins(Map.of("communityId", "1"));

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(Map.of("adminsList", admins), response.getBody());
    }


    @Test
    void testJoinedCommunities_Unauthorized() throws Exception {
        // Arrange
        when(administrativeFacade.getUserJoinedCommunities(any()))
                .thenThrow(new RuntimeException("unauthorized"));

        // Act
        ResponseEntity<?> response = communityController.joinedCommunities(authToken);

        // Assert
        assertEquals(401, response.getStatusCodeValue());
        assertEquals(Map.of("message", "unauthorized"), response.getBody());
    }
}



