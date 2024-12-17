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

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void testGetCommunityInfo() throws Exception {
        // Given
        String jwt = "valid.jwt.token";
        Long communityId = 123L;
        Map<String, Object> map = Map.of("jwt", jwt, "communityId", communityId);

        Claims claims = mock(Claims.class);
        when(claims.getSubject()).thenReturn("testUser");
        when(AuthenticationService.parseToken(jwt)).thenReturn(claims);

        Community community = new Community(communityId, "Test Community", "Description");
        when(communityService.getCommunityById(communityId)).thenReturn(community);

        Community expectedCommunity = community; // This will be returned as per your current implementation

        // When
        Community result = administrativeFacade.getCommunityInfo(map);

        // Then
        assertNotNull(result);
        assertEquals(expectedCommunity.getId(), result.getId());
        assertEquals(expectedCommunity.getName(), result.getName());
        assertEquals(expectedCommunity.getDescription(), result.getDescription());
    }


    @Test
    void testCreateCommunity_Success() {
        // Mock token parsing
        Claims claims = mock(Claims.class);
        when(claims.get("is_admin")).thenReturn(true);
        when(claims.getId()).thenReturn("1");

        // Mock community service
        when(communityService.createCommunity(any(Community.class))).thenReturn(1L);

        // Input data
        Map<String, Object> input = new HashMap<>();
        input.put("jwt", "dummyToken");
        input.put("name", "New Community");
        input.put("description", "A test community");

        // Test
        boolean result = administrativeFacade.createCommunity(input);

        // Verify
        assertTrue(result);
        verify(communityService, times(1)).createCommunity(any(Community.class));
    }

    @Test
    void testCreateCommunity_UserNotAdmin() {
        // Mock token parsing
        Claims claims = mock(Claims.class);
        when(claims.get("is_admin")).thenReturn(false);

        // Input data
        Map<String, Object> input = new HashMap<>();
        input.put("jwt", "dummyToken");
        input.put("name", "New Community");
        input.put("description", "A test community");

        // Test
        boolean result = administrativeFacade.createCommunity(input);

        // Verify
        assertFalse(result);
        verify(communityService, never()).createCommunity(any(Community.class));
    }

    @Test
    void testDeleteCommunity_Success() {
        // Mock token parsing
        Claims claims = mock(Claims.class);
        when(claims.get("is_admin")).thenReturn(true);

        // Mock community service
        when(communityService.deleteCommunity(1L)).thenReturn(true);

        // Input data
        Map<String, Object> input = new HashMap<>();
        input.put("jwt", "dummyToken");
        input.put("communityId", "1");

        // Test
        boolean result = administrativeFacade.deleteCommunity(input);

        // Verify
        assertTrue(result);
        verify(communityService, times(1)).deleteCommunity(1L);
    }

    @Test
    void testDeleteCommunity_UserNotAdmin() {
        // Mock token parsing
        Claims claims = mock(Claims.class);
        when(claims.get("is_admin")).thenReturn(false);

        // Input data
        Map<String, Object> input = new HashMap<>();
        input.put("jwt", "dummyToken");
        input.put("communityId", "1");

        // Test
        boolean result = administrativeFacade.deleteCommunity(input);

        // Verify
        assertFalse(result);
        verify(communityService, never()).deleteCommunity(anyLong());
    }

    @Test
    void testJoinCommunity_Success() {
        // Mock token parsing
        Claims claims = mock(Claims.class);
        when(claims.getId()).thenReturn("1");

        // Mock community service
        when(communityService.joinCommunity(anyLong(), anyLong())).thenReturn(true);

        // Input data
        Map<String, Object> input = new HashMap<>();
        input.put("jwt", "dummyToken");
        input.put("communityId", 1);

        // Test
        boolean result = administrativeFacade.joinCommunity(input);

        // Verify
        assertTrue(result);
        verify(communityService, times(1)).joinCommunity(1L, 1L);
    }

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
        when(userService.getProfile("test_user")).thenReturn(mockProfile);

        Map<String, Object> result = administrativeFacade.getProfile(userdata);

        assertEquals(mockProfile, result);
        assertEquals("test_user", result.get("userName"));
        assertEquals(100, result.get("reputation"));
        assertEquals(true, result.get("isAdmin"));
        assertEquals(50, result.get("no-followers"));
        assertEquals(30, result.get("no-following"));

        verify(userService, times(1)).getProfile("test_user");
    }

    @Test
    public void testGetProfile_Failure() throws Exception {
        Map<String, Object> userdata = Map.of("userName", "test_user");

        when(userService.getProfile("test_user")).thenThrow(new Exception());

        assertThrows(Exception.class, () -> administrativeFacade.getProfile(userdata));

        verify(userService, times(1)).getProfile("test_user");
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


