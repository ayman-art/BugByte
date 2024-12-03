package com.example.BugByte_backend.facades;

import com.example.BugByte_backend.models.User;
import com.example.BugByte_backend.services.AuthenticationService;
import com.example.BugByte_backend.services.UserService;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AdministrativeFacadeTest {

    @Mock
    private UserService userServiceMock;

    @Mock
    private Claims claimsMock;

    @InjectMocks
    private AdministrativeFacade administrativeFacade;



    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetProfile_Success() throws Exception {
        Map<String, Object> userdata = Map.of("user-name", "test_user");

        Map<String, Object> mockProfile = Map.of(
                "user-name", "test_user",
                "reputation", 100,
                "is_admin", true,
                "no-followers", 50,
                "no-following", 30
        );

        when(userServiceMock.getProfile("test_user")).thenReturn(mockProfile);

        Map<String, Object> result = administrativeFacade.getProfile(userdata);

        assertEquals(mockProfile, result);
        assertEquals("test_user", result.get("user-name"));
        assertEquals(100, result.get("reputation"));
        assertEquals(true, result.get("is_admin"));
        assertEquals(50, result.get("no-followers"));
        assertEquals(30, result.get("no-following"));

        verify(userServiceMock, times(1)).getProfile("test_user");
    }

    @Test
    public void testGetProfile_Failure() throws Exception {
        Map<String, Object> userdata = Map.of("user-name", "test_user");

        when(userServiceMock.getProfile("test_user")).thenThrow(new Exception());

        assertThrows(Exception.class, () -> administrativeFacade.getProfile(userdata));

        verify(userServiceMock, times(1)).getProfile("test_user");
    }

    @Test
    public void testGetFollowers_Success() throws Exception {
        Map<String, Object> userdata = Map.of("user-name", "test_user");

        List<User> mockedResult = List.of(
                new User(1L, "test_user1", "password1", "email1", 100L, false),
                new User(2L, "test_user2", "password2", "email2", 200L, true)
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
        Map<String, Object> userdata = Map.of("user-name", "test_user");

        when(userServiceMock.getFollowers("test_user")).thenThrow(new Exception());

        assertThrows(Exception.class, () -> administrativeFacade.getFollowers(userdata));

        verify(userServiceMock, times(1)).getFollowers("test_user");
    }

    @Test
    public void testGetFollowings_Success() throws Exception {
        Map<String, Object> userdata = Map.of("user-name", "test_user");

        List<User> mockedResult = List.of(
                new User(1L, "test_user1", "password1", "email1", 100L, false),
                new User(2L, "test_user2", "password2", "email2", 200L, true)
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
        Map<String, Object> userdata = Map.of("user-name", "test_user");

        when(userServiceMock.getFollowings("test_user")).thenThrow(new Exception());

        assertThrows(Exception.class, () -> administrativeFacade.getFollowings(userdata));

        verify(userServiceMock, times(1)).getFollowings("test_user");
    }


}




