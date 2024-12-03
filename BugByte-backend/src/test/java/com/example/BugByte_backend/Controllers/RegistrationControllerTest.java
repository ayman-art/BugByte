package com.example.BugByte_backend.Controllers;

import com.example.BugByte_backend.controllers.RegistrationController;
import com.example.BugByte_backend.controllers.UserController;
import com.example.BugByte_backend.facades.AdministrativeFacade;
import com.example.BugByte_backend.services.RegistrationService;
import com.example.BugByte_backend.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@SpringBootTest
public class RegistrationControllerTest {

    @Mock
    private AdministrativeFacade administrativeFacade;
    @InjectMocks
    private RegistrationController registrationController;

    @InjectMocks
    private UserService userService;

    @InjectMocks
    private RegistrationService registrationService;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUser_Success() throws Exception {
        // Mock input and behavior
        Map<String, Object> userdata = Map.of("email", "test@example.com", "password", "password123");
        Map<String, Object> response = Map.of("userId", 1);
        when(administrativeFacade.registerUser(eq(userdata))).thenReturn(response);

        // Call the method
        ResponseEntity<?> result = registrationController.registerUser(userdata);

        // Validate results
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    void testRegisterUser_Error() throws Exception {
        // Mock input and behavior
        Map<String, Object> userdata = Map.of("email", "test@example.com");
        when(administrativeFacade.registerUser(userdata)).thenThrow(new Exception("Registration failed"));

        // Call the method
        ResponseEntity<?> result = registrationController.registerUser(userdata);

        // Validate results
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("Registration failed", result.getBody());
    }

    @Test
    void testLoginUser_Success() throws Exception {
        // Mock input and behavior
        Map<String, Object> userdata = Map.of("email", "test@example.com", "password", "password123");
        String jwt = "sampleJwtToken";
        when(administrativeFacade.loginUser(userdata)).thenReturn(jwt);

        // Call the method
        ResponseEntity<?> result = registrationController.loginUser(userdata);

        // Validate results
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(Map.of("jwt", jwt), result.getBody());
    }

    @Test
    void testLoginUser_Error() throws Exception {
        // Mock input and behavior
        Map<String, Object> userdata = Map.of("email", "test@example.com");
        when(administrativeFacade.loginUser(userdata)).thenThrow(new Exception("Login failed"));

        // Call the method
        ResponseEntity<?> result = registrationController.loginUser(userdata);

        // Validate results
        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        assertEquals("Login failed", result.getBody());
    }

    @Test
    void testDeleteUser_Success() throws Exception {
        // Mock input and behavior
        String token = "Bearer sampleJwtToken";
        doNothing().when(administrativeFacade).deleteUser(Map.of("jwt", "sampleJwtToken"));

        // Call the method
        ResponseEntity<?> result = registrationController.deleteUser(token);

        // Validate results
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("User Deleted Successfully", result.getBody());
    }

    @Test
    void testDeleteUser_Error() throws Exception {
        // Mock input and behavior
        String token = "Bearer invalidToken";
        doThrow(new Exception("Unauthorized")).when(administrativeFacade).deleteUser(Map.of("jwt", "invalidToken"));

        // Call the method
        ResponseEntity<?> result = registrationController.deleteUser(token);

        // Validate results
        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        assertEquals("Unauthorized", result.getBody());
    }

    @Test
    void testResetPassword_Success() throws Exception {
        // Mock input and behavior
        Map<String, Object> userdata = Map.of("email", "test@example.com");
        String email = "test@example.com";
        when(administrativeFacade.resetPassword(userdata)).thenReturn(email);

        // Call the method
        ResponseEntity<?> result = registrationController.resetPassword(userdata);

        // Validate results
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(Map.of("email", email), result.getBody());
    }

    @Test
    void testResetPassword_Error() throws Exception {
        // Mock input and behavior
        Map<String, Object> userdata = Map.of("email", "test@example.com");
        when(administrativeFacade.resetPassword(userdata)).thenThrow(new Exception("Unauthorized"));

        // Call the method
        ResponseEntity<?> result = registrationController.resetPassword(userdata);

        // Validate results
        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        assertEquals("Unauthorized", result.getBody());
    }

    @Test
    void testValidateEmailCode_Success() throws Exception {
        // Mock input and behavior
        Map<String, Object> userdata = Map.of("code", "123456");
        String jwt = "sampleJwtToken";
        when(administrativeFacade.validateEmailedCode(userdata)).thenReturn(jwt);

        // Call the method
        ResponseEntity<?> result = registrationController.validateEmailCode(userdata);

        // Validate results
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(Map.of("jwt", jwt), result.getBody());
    }

    @Test
    void testValidateEmailCode_Error() throws Exception {
        // Mock input and behavior
        Map<String, Object> userdata = Map.of("code", "invalidCode");
        when(administrativeFacade.validateEmailedCode(userdata)).thenThrow(new Exception("Unauthorized"));

        // Call the method
        ResponseEntity<?> result = registrationController.validateEmailCode(userdata);

        // Validate results
        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        assertEquals("Unauthorized", result.getBody());
    }
}
