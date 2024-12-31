package com.example.BugByte_backend.facades;

import com.example.BugByte_backend.models.Community;
import com.example.BugByte_backend.models.User;
import com.example.BugByte_backend.services.AuthenticationService;
import com.example.BugByte_backend.services.CommunityService;
import com.example.BugByte_backend.services.ModeratorService;
import com.example.BugByte_backend.services.RegistrationService;
import com.example.BugByte_backend.services.UserService;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdministrativeFacadeCommunityTest {

    @InjectMocks
    private AdministrativeFacade administrativeFacade;

    @Mock
    private UserService userService;

    @Mock
    private RegistrationService registrationService;

    @Mock
    private CommunityService communityService;

    @Mock
    private ModeratorService moderatorService;
    @Mock
    AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void testGetCommunityInfoThrowsException() {
        // Arrange
        Map<String, Object> map = new HashMap<>();
        map.put("jwt", "mock_jwt_token");

        when(authenticationService.getUserNameFromJwt(any(String.class))).thenReturn(null);

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> administrativeFacade.getCommunityInfo(map));
        assertEquals("userName is null", exception.getMessage());
    }
    @Test
    void testDeleteCommunitySuccess() throws Exception {
        // Arrange
        Map<String, Object> map = new HashMap<>();
        map.put("jwt", "mock_jwt_token");
        map.put("communityId", "123");

        when(authenticationService.getIsAdminFromJwt("mock_jwt_token")).thenReturn(true);
        when(communityService.deleteCommunity(123L)).thenReturn(true);

        // Act
        boolean result = administrativeFacade.deleteCommunity(map);

        // Assert
        assertTrue(result);
    }
    @Test
    void testDeleteCommunityInvalidCommunityId() {
        // Arrange
        Map<String, Object> map = new HashMap<>();
        map.put("jwt", "mock_jwt_token");
        map.put("communityId", 0);

        when(authenticationService.getIsAdminFromJwt("mock_jwt_token")).thenReturn(true);

        // Act
        boolean result = administrativeFacade.deleteCommunity(map);

        // Assert
        assertFalse(result);
    }

    @Test
    void testEditCommunityInvalidCommunityId() {
        // Arrange
        Map<String, Object> map = new HashMap<>();
        map.put("jwt", "mock_jwt_token");
        map.put("communityId", "invalid_id");
        map.put("name", "New Community Name");
        map.put("desription", "Updated Description");

        when(authenticationService.getIsAdminFromJwt("mock_jwt_token")).thenReturn(true);

        // Act
        boolean result = administrativeFacade.editCommunity(map);

        // Assert
        assertFalse(result);
    }

    @Test
    void testEditCommunityInvalidData() {
        // Arrange
        Map<String, Object> map = new HashMap<>();
        map.put("jwt", "mock_jwt_token");
        map.put("communityId", "123");
        map.put("name", null); // Invalid data
        map.put("desription", "Updated Description");

        when(authenticationService.getIsAdminFromJwt("mock_jwt_token")).thenReturn(true);

        // Act
        boolean result = administrativeFacade.editCommunity(map);

        // Assert
        assertFalse(result);
    }

    @Test
    void testRemoveModeratorSuccess() throws Exception {
        // Arrange
        Map<String, Object> req = new HashMap<>();
        req.put("jwt", "mock_jwt_token");
        req.put("moderatorName", "moderator1");
        req.put("communityId", 1L);

        when(authenticationService.getIsAdminFromJwt("mock_jwt_token")).thenReturn(true);
        when(moderatorService.removeModerator("moderator1", 1L)).thenReturn(true);

        // Act
        boolean result = administrativeFacade.removeModerator((Long) req.get("communityId"), (String) req.get("moderatorName"),
                (String) req.get("jwt"));

        // Assert
        assertTrue(result);
    }

    @Test
    void testRemoveModeratorException() {
        // Arrange
        Map<String, Object> req = new HashMap<>();
        req.put("jwt", "mock_jwt_token");
        req.put("moderatorName", "moderator1");
        req.put("communityId", 1L);

        when(authenticationService.getIsAdminFromJwt("mock_jwt_token")).thenReturn(true);
        when(moderatorService.removeModerator("moderator1", 1L)).thenThrow(new RuntimeException("Service error"));

        // Act
        boolean result = administrativeFacade.removeModerator((Long) req.get("communityId"), (String) req.get("moderatorName"),
                (String) req.get("jwt"));

        // Assert
        assertFalse(result);
    }
    @Test
    void testRemoveMemberSuccess() throws Exception {
        // Arrange
        Map<String, Object> req = new HashMap<>();
        req.put("jwt", "mock_jwt_token");
        req.put("communityId", 1L);
        req.put("user_name", "testUser");

        when(authenticationService.getIsAdminFromJwt("mock_jwt_token")).thenReturn(true);
        when(communityService.deleteMember(1L, "testUser")).thenReturn(true);

        // Act
        boolean result = administrativeFacade.removeMember((Long) req.get("communityId"), (String) req.get("user_name"),
                (String) req.get("jwt"));

        // Assert
        assertTrue(result);
    }

    @Test
    void testJoinCommunityInvalidToken() {
        // Arrange
        Map<String, Object> req = new HashMap<>();
        req.put("jwt", "invalid_jwt_token");

        when(authenticationService.getIdFromJwt("invalid_jwt_token")).thenThrow(new IllegalArgumentException());

        // Act
        boolean result = administrativeFacade.joinCommunity(req);

        // Assert
        assertFalse(result);
    }
    @Test
    void testGetUserJoinedCommunitiesSuccess() throws Exception {
        // Arrange
        String jwt = "mock_jwt_token";
        long userId = 123L;

        Community community1 = new Community(1L, "Community1", "Description1");
        Community community2 = new Community(2L, "Community2", "Description2");
        List<Community> communities = Arrays.asList(community1, community2);

        when(authenticationService.getIdFromJwt(jwt)).thenReturn(userId);
        when(communityService.getUserCommunities(userId)).thenReturn(communities);

        // Act
        List<Community> result = administrativeFacade.getUserJoinedCommunities(jwt);

        // Assert
        assertEquals(2, result.size());
        assertEquals("Community1", result.get(0).getName());
        assertEquals("Community2", result.get(1).getName());
    }

    @Test
    void testGetUserJoinedCommunitiesException() throws Exception {
        // Arrange
        String jwt = "mock_jwt_token";
        long userId = 123L;

        when(authenticationService.getIdFromJwt(jwt)).thenReturn(userId);
        when(communityService.getUserCommunities(userId)).thenThrow(new RuntimeException("Service error"));

        // Act
        Exception exception = assertThrows(RuntimeException.class, () -> administrativeFacade.getUserJoinedCommunities(jwt));

        // Assert
        assertEquals("Service error", exception.getMessage());
    }

    @Test
    void testJoinCommunityException() {
        // Arrange
        Map<String, Object> req = new HashMap<>();
        req.put("jwt", "mock_jwt_token");
        req.put("communityId", 1L);

        when(authenticationService.getIdFromJwt("mock_jwt_token")).thenReturn(123L);
        when(communityService.joinCommunity(1L, 123L)).thenThrow(new RuntimeException("Service error"));

        // Act
       boolean res = administrativeFacade.joinCommunity(req);

        // Assert
        assertFalse(res);
    }
    @Test
    void testGetAllCommunitiesSuccess() throws Exception {
        // Arrange
        String jwt = "mock_jwt_token";
        long userId = 123L;
        int pageSize = 10;
        int pageNumber = 1;

        Community community1 = new Community(1L, "Community1", "Description1");
        Community community2 = new Community(2L, "Community2", "Description2");
        List<Community> communities = Arrays.asList(community1, community2);

        when(authenticationService.getIdFromJwt(jwt)).thenReturn(userId);
        when(communityService.getAllCommunities(pageSize, pageNumber)).thenReturn(communities);

        // Act
        List<Community> result = administrativeFacade.getAllCommunities(jwt, pageSize, pageNumber);

        // Assert
        assertEquals(2, result.size());
        assertEquals("Community1", result.get(0).getName());
        assertEquals("Community2", result.get(1).getName());
    }

    @Test
    void testGetAllCommunitiesException() throws Exception {
        // Arrange
        String jwt = "mock_jwt_token";
        long userId = 123L;
        int pageSize = 10;
        int pageNumber = 1;

        when(authenticationService.getIdFromJwt(jwt)).thenReturn(userId);
        when(communityService.getAllCommunities(pageSize, pageNumber)).thenThrow(new RuntimeException("Service error"));

        // Act
        Exception exception = assertThrows(RuntimeException.class, () -> administrativeFacade.getAllCommunities(jwt, pageSize, pageNumber));

        // Assert
        assertEquals("Service error", exception.getMessage());
    }

    @Test
    public void testGetFollowers_Success() throws Exception {
        Map<String, Object> userdata = Map.of("userName", "test_user");

        List<User> mockedResult = List.of(
                User.builder()
                        .id(1L)
                        .userName("test_user1")
                        .email("email1")
                        .password("password1")
                        .reputation(100L)
                        .isAdmin(false)
                        .build(),
                User.builder()
                        .id(2L)
                        .userName("test_user2")
                        .email("email2")
                        .password("password2")
                        .reputation(200L)
                        .isAdmin(true)
                        .build()
        );

        when(userService.getFollowers("test_user")).thenReturn(mockedResult);

        List<Map<String, Object>> result = administrativeFacade.getFollowers(userdata);

        assertEquals(2, result.size());

        assertFalse(result.get(0).containsKey("id"));
        assertFalse(result.get(0).containsKey("password"));
        assertFalse(result.get(0).containsKey("email"));
        assertEquals("test_user1", result.get(0).get("userName"));
        assertEquals(100L, result.get(0).get("reputation"));
        assertFalse((boolean) result.get(0).get("isAdmin"));

        assertFalse(result.get(1).containsKey("id"));
        assertFalse(result.get(1).containsKey("password"));
        assertFalse(result.get(1).containsKey("email"));
        assertEquals("test_user2", result.get(1).get("userName"));
        assertEquals(200L, result.get(1).get("reputation"));
        assertTrue((boolean) result.get(1).get("isAdmin"));

        verify(userService, times(1)).getFollowers("test_user");
    }

    @Test
    public void testGetFollowers_Failure() throws Exception {
        Map<String, Object> userdata = Map.of("userName", "test_user");

        when(userService.getFollowers("test_user")).thenThrow(new Exception());

        assertThrows(Exception.class, () -> administrativeFacade.getFollowers(userdata));

        verify(userService, times(1)).getFollowers("test_user");
    }

}


