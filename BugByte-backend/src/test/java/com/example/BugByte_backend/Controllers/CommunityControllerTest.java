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

    @Test
    void testGetAllCommunities_Unauthorized() throws Exception {
        // Arrange
        String token = "Bearer invalidToken";
        Integer pageNumber = 1;
        Integer pageSize = 10;
        when(administrativeFacade.getAllCommunities(any(), any(), any())).thenThrow(new RuntimeException("unauthorized"));

        // Act
        ResponseEntity<?> response = communityController.getAllCommunities(token, pageNumber, pageSize);

        // Assert
        assertEquals(401, response.getStatusCodeValue());
        assertEquals(Map.of("message", "unauthorized"), response.getBody());
    }
    @Test
    void testLeaveCommunity_Success() {
        // Arrange
        String token = "Bearer testToken";
        String communityName = "Community 1";
        when(administrativeFacade.leaveCommunity(any(), any())).thenReturn(true);

        // Act
        ResponseEntity<?> response = communityController.updateQuestion(token, communityName);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(true, response.getBody());
    }

    @Test
    void testLeaveCommunity_Unauthorized() {
        // Arrange
        String token = "Bearer invalidToken";
        String communityName = "Community 1";
        when(administrativeFacade.leaveCommunity(any(), any())).thenThrow(new RuntimeException("unauthorized"));

        // Act
        ResponseEntity<?> response = communityController.updateQuestion(token, communityName);

        // Assert
        assertEquals(401, response.getStatusCodeValue());
        assertEquals("unauthorized", response.getBody());
    }

    @Test
    void testJoinCommunity_Success() {
        // Arrange
        String token = "Bearer testToken";
        Map<String, Object> communityData = Map.of("communityName", "Test Community");
        when(administrativeFacade.joinCommunity(any())).thenReturn(true);

        // Act
        ResponseEntity<?> response = communityController.joinCommunity(token, communityData);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("user joined Successfully", response.getBody());
    }

    @Test
    void testJoinCommunity_Failure() {
        // Arrange
        String token = "Bearer testToken";
        Map<String, Object> communityData = Map.of("communityName", "Test Community");
        when(administrativeFacade.joinCommunity(any())).thenReturn(false);

        // Act
        ResponseEntity<?> response = communityController.joinCommunity(token, communityData);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("error joining community", response.getBody());
    }

    @Test
    void testJoinCommunity_Exception() {
        // Arrange
        String token = "Bearer testToken";
        Map<String, Object> communityData = Map.of("communityName", "Test Community");
        when(administrativeFacade.joinCommunity(any())).thenThrow(new RuntimeException("error"));

        // Act
        ResponseEntity<?> response = communityController.joinCommunity(token, communityData);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("error", response.getBody());
    }

    @Test
    void testCreateCommunity_Error() {
        // Arrange
        String token = "Bearer testToken";
        Map<String, Object> communityData = Map.of("communityName", "Test Community");
        when(administrativeFacade.createCommunity(any())).thenReturn(false);

        // Act
        ResponseEntity<?> response = communityController.createCommunity(communityData, token);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        assertEquals(null, response.getBody());
    }

    @Test
    void testDeleteCommunity_Error() {
        // Arrange
        Map<String, Object> communityData = Map.of("communityName", "Test Community");
        when(administrativeFacade.deleteCommunity(any())).thenReturn(false);

        // Act
        ResponseEntity<?> response = communityController.deleteCommunity(communityData);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("error deleting community", response.getBody());
    }

    @Test
    void testEditCommunity_Error() {
        // Arrange
        Map<String, Object> communityData = Map.of("communityName", "Test Community");
        when(administrativeFacade.editCommunity(any())).thenReturn(false);

        // Act
        ResponseEntity<?> response = communityController.editCommunity(communityData);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("error editing community", response.getBody());
    }

    @Test
    void testSetModerator_Error() {
        // Arrange
        Map<String, Object> moderatorData = Map.of("communityName", "Test Community", "userName", "testUser");
        when(administrativeFacade.setModerator(any())).thenReturn(false);

        // Act
        ResponseEntity<?> response = communityController.setModerator(moderatorData);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("error adding moderator", response.getBody());
    }

    @Test
    void testRemoveModerator_Error() {
        // Arrange
        Map<String, Object> moderatorData = Map.of("communityName", "Test Community", "userName", "testUser");
        when(administrativeFacade.removeModerator(any())).thenReturn(false);

        // Act
        ResponseEntity<?> response = communityController.removeModerator(moderatorData);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("error removing moderator", response.getBody());
    }

    @Test
    void testRemoveMember_Error() {
        // Arrange
        Map<String, Object> memberData = Map.of("communityName", "Test Community", "userName", "testUser");
        when(administrativeFacade.removeMember(any())).thenReturn(false);

        // Act
        ResponseEntity<?> response = communityController.removeMember(memberData);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("error removing member", response.getBody());
    }

    @Test
    void testGetAdmins_Error() throws Exception {
        // Arrange
        Map<String, Object> communityData = Map.of("communityName", "Test Community");
        when(administrativeFacade.getAdmins(any())).thenThrow(new RuntimeException("error fetching admins"));

        // Act
        ResponseEntity<?> response = communityController.getAdmins(communityData);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("error fetching admins", response.getBody());
    }
    @Test
    void testCreateCommunity_Error9() {
        // Arrange
        String token = "Bearer testToken";
        Map<String, Object> communityData = Map.of("communityName", "Test Community");
        when(administrativeFacade.createCommunity(any())).thenReturn(false);

        // Act
        ResponseEntity<?> response = communityController.createCommunity(communityData, token);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        assertEquals(null, response.getBody());
    }

    @Test
    void testDeleteCommunity_Error8() {
        // Arrange
        Map<String, Object> communityData = Map.of("communityName", "Test Community");
        when(administrativeFacade.deleteCommunity(any())).thenReturn(false);

        // Act
        ResponseEntity<?> response = communityController.deleteCommunity(communityData);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("error deleting community", response.getBody());
    }

    @Test
    void testEditCommunity_Error6() {
        // Arrange
        Map<String, Object> communityData = Map.of("communityName", "Test Community");
        when(administrativeFacade.editCommunity(any())).thenReturn(false);

        // Act
        ResponseEntity<?> response = communityController.editCommunity(communityData);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("error editing community", response.getBody());
    }

    @Test
    void testSetModerator_Error5() {
        // Arrange
        Map<String, Object> moderatorData = Map.of("communityName", "Test Community", "userName", "testUser");
        when(administrativeFacade.setModerator(any())).thenReturn(false);

        // Act
        ResponseEntity<?> response = communityController.setModerator(moderatorData);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("error adding moderator", response.getBody());
    }

    @Test
    void testRemoveModerator_Error1() {
        // Arrange
        Map<String, Object> moderatorData = Map.of("communityName", "Test Community", "userName", "testUser");
        when(administrativeFacade.removeModerator(any())).thenReturn(false);

        // Act
        ResponseEntity<?> response = communityController.removeModerator(moderatorData);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("error removing moderator", response.getBody());
    }

    @Test
    void testRemoveMember_Error4() {
        // Arrange
        Map<String, Object> memberData = Map.of("communityName", "Test Community", "userName", "testUser");
        when(administrativeFacade.removeMember(any())).thenReturn(false);

        // Act
        ResponseEntity<?> response = communityController.removeMember(memberData);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("error removing member", response.getBody());
    }

    @Test
    void testGetAdmins_Error2() throws Exception {
        // Arrange
        Map<String, Object> communityData = Map.of("communityName", "Test Community");
        when(administrativeFacade.getAdmins(any())).thenThrow(new RuntimeException("error fetching admins"));

        // Act
        ResponseEntity<?> response = communityController.getAdmins(communityData);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("error fetching admins", response.getBody());
    }

}



