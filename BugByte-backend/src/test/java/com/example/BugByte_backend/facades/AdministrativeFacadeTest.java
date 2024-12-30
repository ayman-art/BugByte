package com.example.BugByte_backend.facades;

import com.example.BugByte_backend.models.Community;
import com.example.BugByte_backend.models.User;
import com.example.BugByte_backend.services.*;
import io.jsonwebtoken.Claims;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class AdministrativeFacadeTest {
    @Mock
    private UserService userServiceMock;

    @Mock
    private Claims claimsMock;

    @Mock
    private AuthenticationService authenticationService;
    @Mock
    private ModeratorService moderatorService;
    @Mock
    private CommunityService communityService;

    @InjectMocks
    private AdministrativeFacade administrativeFacade;

    private Map<String, Object> requestData;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        requestData = new HashMap<>();
        requestData.put("jwt", "valid_jwt_token");
        requestData.put("communityId", "1");
        requestData.put("moderatorName", "test_moderator");
    }

    @Test
    void testSetModerator_Success() {
        when(authenticationService.getIsAdminFromJwt("valid_jwt_token")).thenReturn(true);
        when(moderatorService.setModerator("test_moderator", 1L)).thenReturn(true);

        boolean result = administrativeFacade.setModerator(requestData);

        assertTrue(result);
        verify(moderatorService, times(1)).setModerator("test_moderator", 1L);
    }

    @Test
    void testSetModerator_Failure_NotAdmin() {
        when(authenticationService.getIsAdminFromJwt("valid_jwt_token")).thenReturn(false);

        boolean result = administrativeFacade.setModerator(requestData);

        assertFalse(result);
        verify(moderatorService, never()).setModerator(anyString(), anyLong());
    }

    @Test
    void testRemoveModerator_Success() {
        when(authenticationService.getIsAdminFromJwt("valid_jwt_token")).thenReturn(true);
        when(moderatorService.removeModerator("test_moderator", 1L)).thenReturn(true);

        boolean result = administrativeFacade.removeModerator(requestData);

        assertTrue(result);
        verify(moderatorService, times(1)).removeModerator("test_moderator", 1L);
    }

    @Test
    void testRemoveModerator_Failure_NotAdmin() {
        when(authenticationService.getIsAdminFromJwt("valid_jwt_token")).thenReturn(false);

        boolean result = administrativeFacade.removeModerator(requestData);

        assertFalse(result);
        verify(moderatorService, never()).removeModerator(anyString(), anyLong());
    }

//    @Test
//    void testRemoveMember_Success() {
//        when(authenticationService.getIsAdminFromJwt("valid_jwt_token")).thenReturn(true);
//        when(communityService.deleteMember(1L, "test_user")).thenReturn(true);
//
//        boolean result = administrativeFacade.removeMember(requestData);
//        assertTrue(result);
//    }

    @Test
    void testRemoveMember_Failure_NotAdmin() {
        when(authenticationService.getIsAdminFromJwt("valid_jwt_token")).thenReturn(false);

        assertThrows(RuntimeException.class ,()-> administrativeFacade.removeMember(requestData));
    }

    @Test
    void testGetAdmins_Success() throws Exception {
        User user =new User();
        user.setUserName("admin_user");
        user.setId(1L);
        List<User> admins = List.of(user);
        when(authenticationService.getIsAdminFromJwt("valid_jwt_token")).thenReturn(true);
        when(communityService.getCommunityAdmins(1L)).thenReturn(admins);

        List<Map<String, Object>> adminsResult = administrativeFacade.getAdmins(requestData);

        assertNotNull(adminsResult);
        assertEquals(1, adminsResult.size());
        verify(communityService, times(1)).getCommunityAdmins(1L);
    }

    @Test
    void testGetAdmins_Failure_NotAdmin() {
        when(authenticationService.getIsAdminFromJwt("valid_jwt_token")).thenReturn(false);

        assertThrows(Exception.class, () -> {
            administrativeFacade.getAdmins(requestData);
        });
    }

    @Test
    void testJoinCommunity_Success() {
        when(authenticationService.getIdFromJwt("valid_jwt_token")).thenReturn(1L);
        when(communityService.joinCommunity(1L, 1L)).thenReturn(true);

        boolean result = administrativeFacade.joinCommunity(requestData);

        assertTrue(result);
        verify(communityService, times(1)).joinCommunity(1L, 1L);
    }

    @Test
    void testJoinCommunity_Failure_InvalidToken() {
        when(authenticationService.getIdFromJwt("valid_jwt_token")).thenReturn(1L);
        when(communityService.joinCommunity(1L, 1L)).thenReturn(false);

        boolean result = administrativeFacade.joinCommunity(requestData);

        assertFalse(result);
    }

    @Test
    void testGetUserJoinedCommunities_Success() throws Exception {

        List<Community> communities = List.of(new Community("Community 1",1L ));
        when(authenticationService.getIdFromJwt("valid_jwt_token")).thenReturn(1L);
        when(communityService.getUserCommunities(1L)).thenReturn(communities);

        List<Community> result = administrativeFacade.getUserJoinedCommunities("valid_jwt_token");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Community 1", result.get(0).getName());
    }

//    @Test
//    void testLeaveCommunity_Success() {
//        Claims mockClaims = mock(Claims.class);
//        when(mockClaims.getId()).thenReturn("1");
//
//        when(AuthenticationService.parseToken("valid_jwt_token")).thenReturn(mockClaims);
//
//        when(communityService.leaveCommunity("Community 1", 1L)).thenReturn(true);
//
//        boolean result = administrativeFacade.leaveCommunity("valid_jwt_token", "Community 1");
//
//        // Assertions
//        assertTrue(result);
//        verify(communityService, times(1)).leaveCommunity("Community 1", 1L);
//    }

    @Test
    public void testGetProfile_Success() throws Exception {
        Map<String, Object> userdata = new HashMap<>();
        userdata.put("userName", "test_user");
        userdata.put("jwt", "token");

        Map<String, Object> mockProfile = Map.of(
                "userName", "test_user",
                "reputation", 100,
                "isAdmin", true,
                "no-followers", 50,
                "no-following", 30
        );

        when(AuthenticationService.parseToken(anyString())).thenReturn(null);
        when(userServiceMock.getProfile("test_user")).thenReturn(mockProfile);

        Map<String, Object> result = administrativeFacade.getProfile(userdata);

        assertEquals(mockProfile, result);
        assertEquals("test_user", result.get("userName"));
        assertEquals(100, result.get("reputation"));
        assertEquals(true, result.get("isAdmin"));
        assertEquals(50, result.get("no-followers"));
        assertEquals(30, result.get("no-following"));

        verify(userServiceMock, times(1)).getProfile("test_user");
    }

    @Test
    public void testGetProfile_Failure() throws Exception {
        Map<String, Object> userdata = Map.of("userName", "test_user");

        when(userServiceMock.getProfile("test_user")).thenThrow(new Exception());

        assertThrows(Exception.class, () -> administrativeFacade.getProfile(userdata));

        verify(userServiceMock, times(1)).getProfile("test_user");
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

        when(userServiceMock.getFollowers("test_user")).thenReturn(mockedResult);

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

        verify(userServiceMock, times(1)).getFollowers("test_user");
    }

    @Test

    public void testGetFollowers_Failure() throws Exception {
        Map<String, Object> userdata = Map.of("userName", "test_user");

        when(userServiceMock.getFollowers("test_user")).thenThrow(new Exception());

        assertThrows(Exception.class, () -> administrativeFacade.getFollowers(userdata));

        verify(userServiceMock, times(1)).getFollowers("test_user");
    }

    @Test
    public void testGetFollowings_Success() throws Exception {
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

        when(userServiceMock.getFollowings("test_user")).thenReturn(mockedResult);

        List<Map<String, Object>> result = administrativeFacade.getFollowings(userdata);

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

        verify(userServiceMock, times(1)).getFollowings("test_user");
    }

    @Test
    public void testGetFollowings_Failure() throws Exception {
        Map<String, Object> userdata = Map.of("userName", "test_user");

        when(userServiceMock.getFollowings("test_user")).thenThrow(new Exception());

        assertThrows(Exception.class, () -> administrativeFacade.getFollowings(userdata));

        verify(userServiceMock, times(1)).getFollowings("test_user");
    }

}
