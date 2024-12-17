package com.example.BugByte_backend.facades;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import com.example.BugByte_backend.models.Question;
import com.example.BugByte_backend.services.AuthenticationService;
import com.example.BugByte_backend.services.PostingService;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

public class InteractionFacadeTest {

    @InjectMocks
    InteractionFacade interactionFacade;

    @Mock
    PostingService postingService;

    @Mock
    Claims claims;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testPostQuestion_Exception() {
        // Arrange
        Map<String, Object> postData = new HashMap<>();
        postData.put("jwt", "mockJwtToken");

        // Mock static method
        try (MockedStatic<AuthenticationService> mockedAuthService = Mockito.mockStatic(AuthenticationService.class)) {
            mockedAuthService.when(() -> AuthenticationService.parseToken("mockJwtToken"))
                    .thenThrow(new RuntimeException("Invalid token"));

            // Act & Assert
            Exception exception = assertThrows(Exception.class, () -> {
                interactionFacade.postQuestion(postData);
            });

            assertEquals("Invalid token", exception.getMessage());
            //verify(postingService, never()).postQuestion(any(Question.class));
        }
    }
}
