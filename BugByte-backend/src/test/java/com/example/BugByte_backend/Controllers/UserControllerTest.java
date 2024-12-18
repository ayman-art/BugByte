package com.example.BugByte_backend.Controllers;

import com.example.BugByte_backend.controllers.UserController;
import com.example.BugByte_backend.facades.AdministrativeFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private AdministrativeFacade administrativeFacade;
    @InjectMocks
    private UserController userController;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void testUpdateProfilePicture_Success() throws Exception {
        // Arrange
        String token = "Bearer validToken";
        String expectedToken = "validToken";
        Map<String, Object> data = new HashMap<>();
        data.put("profilePicture", "newPictureData");

        doNothing().when(administrativeFacade).updateUserProfilePicture(anyMap());

        // Act
        ResponseEntity<?> response = userController.updateProfilePicture(token, data);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Profile picture updated successfully", response.getBody());

        // Verify
        data.put("jwt", expectedToken);
        verify(administrativeFacade, times(1)).updateUserProfilePicture(data);
    }

    @Test
    void testUpdateProfilePicture_InvalidToken() {
        // Arrange
        String token = "InvalidToken";
        Map<String, Object> data = new HashMap<>();
        data.put("profilePicture", "newPictureData");

        // Act
        ResponseEntity<?> response = userController.updateProfilePicture(token, data);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("JWT String argument cannot be null or empty.", response.getBody()); // Adjust based on your exception message
    }

    @Test
    void testUpdateProfilePicture_ExceptionThrown() throws Exception {
        // Arrange
        String token = "Bearer validToken";
        String expectedToken = "validToken";
        Map<String, Object> data = new HashMap<>();
        data.put("profilePicture", "newPictureData");

        doThrow(new RuntimeException("Unexpected error"))
                .when(administrativeFacade).updateUserProfilePicture(anyMap());

        // Act
        ResponseEntity<?> response = userController.updateProfilePicture(token, data);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Unexpected error", response.getBody());

        // Verify
        data.put("jwt", expectedToken);
        verify(administrativeFacade, times(1)).updateUserProfilePicture(data);
    }
    @Test
    void testGetProfile_Success() throws Exception {
        // Mock input and behavior
        String token = "Bearer validToken";
        String username = "john_doe";
        Map<String, Object> profile = Map.of("name", "John Doe", "username", "john_doe");
        when(administrativeFacade.getProfile(Map.of("jwt", "validToken", "userName", username))).thenReturn(profile);

        // Call the method
        ResponseEntity<?> result = userController.getProfile(token, username);

        // Validate results
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(profile, result.getBody());
    }

    @Test
    void testGetProfile_Error() throws Exception {
        // Mock input and behavior
        String token = "Bearer invalidToken";
        String username = "john_doe";
        when(administrativeFacade.getProfile(Map.of("jwt", "invalidToken", "userName", username))).thenThrow(new Exception("Unauthorized"));

        // Call the method
        ResponseEntity<?> result = userController.getProfile(token, username);

        // Validate results
        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        assertEquals("Unauthorized", result.getBody());
    }

    @Test
    void testFollowUser_Success() throws Exception {
        // Mock input and behavior
        String token = "Bearer validToken";
        String username = "jane_doe";
        when(administrativeFacade.followUser(Map.of("jwt", "validToken", "userName", username))).thenReturn(true);

        // Call the method
        ResponseEntity<?> result = userController.followUser(token, username);

        // Validate results
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("User followed successfully.", result.getBody());
    }

    @Test
    void testFollowUser_Error() throws Exception {
        // Mock input and behavior
        String token = "Bearer invalidToken";
        String username = "jane_doe";
        when(administrativeFacade.followUser(Map.of("jwt", "invalidToken", "userName", username))).thenThrow(new Exception("Unauthorized"));

        // Call the method
        ResponseEntity<?> result = userController.followUser(token, username);

        // Validate results
        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        assertEquals("Unauthorized", result.getBody());
    }

    @Test
    void testUnFollowUser_Success() throws Exception {
        // Mock input and behavior
        String token = "Bearer validToken";
        String username = "jane_doe";
        when(administrativeFacade.unfollowUser(Map.of("jwt", "validToken", "userName", username))).thenReturn(true);

        // Call the method
        ResponseEntity<?> result = userController.unFollowUser(token, username);

        // Validate results
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("User unfollowed successfully.", result.getBody());
    }

    @Test
    void testUnFollowUser_Error() throws Exception {
        // Mock input and behavior
        String token = "Bearer invalidToken";
        String username = "jane_doe";
        when(administrativeFacade.unfollowUser(Map.of("jwt", "invalidToken", "userName", username))).thenThrow(new Exception("Unauthorized"));

        // Call the method
        ResponseEntity<?> result = userController.unFollowUser(token, username);

        // Validate results
        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        assertEquals("Unauthorized", result.getBody());
    }

    @Test
    void testGetFollowings_Success() throws Exception {
        // Mock input and behavior
        String token = "Bearer validToken";
        String username = "john_doe";
        List<Map<String, Object>> followings = List.of(Map.of("username", "jane_doe"));
        when(administrativeFacade.getFollowings(Map.of("jwt", "validToken", "userName", username))).thenReturn(followings);

        // Call the method
        ResponseEntity<?> result = userController.getFollowings(token, username);

        // Validate results
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(followings, result.getBody());
    }

    @Test
    void testGetFollowings_Error() throws Exception {
        // Mock input and behavior
        String token = "Bearer invalidToken";
        String username = "john_doe";
        when(administrativeFacade.getFollowings(Map.of("jwt", "invalidToken", "userName", username))).thenThrow(new Exception("Unauthorized"));

        // Call the method
        ResponseEntity<?> result = userController.getFollowings(token, username);

        // Validate results
        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        assertEquals("Unauthorized", result.getBody());
    }

    @Test
    void testMakeAdmin_Success() throws Exception {
        // Mock input and behavior
        String token = "Bearer validToken";
        String username = "john_doe";
        when(administrativeFacade.makeAdmin(Map.of("jwt", "validToken", "userName", username))).thenReturn(true);

        // Call the method
        ResponseEntity<?> result = userController.makeAdmin(token, username);

        // Validate results
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("User promoted to admin successfully.", result.getBody());
    }

    @Test
    void testMakeAdmin_Error() throws Exception {
        // Mock input and behavior
        String token = "Bearer invalidToken";
        String username = "john_doe";
        when(administrativeFacade.makeAdmin(Map.of("jwt", "invalidToken", "userName", username))).thenThrow(new Exception("Unauthorized"));

        // Call the method
        ResponseEntity<?> result = userController.makeAdmin(token, username);

        // Validate results
        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        assertEquals("Unauthorized", result.getBody());
    }
}

