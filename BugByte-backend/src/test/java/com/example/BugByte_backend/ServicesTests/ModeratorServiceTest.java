package com.example.BugByte_backend.ServicesTests;

import com.example.BugByte_backend.repositories.ModeratorRepository;
import com.example.BugByte_backend.repositories.UserRepositoryImp;
import com.example.BugByte_backend.services.ModeratorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ModeratorServiceTest {

    @Mock
    private ModeratorRepository modRepo;

    @Mock
    private UserRepositoryImp userRepositoryImp;

    @InjectMocks
    private ModeratorService moderatorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSetModerator_Success() {
        // Arrange
        String userName = "testUser";
        Long communityId = 1L;
        Long userId = 100L;

        when(userRepositoryImp.getIdByUserName(userName)).thenReturn(userId);
        when(modRepo.makeModerator(userId, communityId)).thenReturn(true);

        // Act
        boolean result = moderatorService.setModerator(userName, communityId);

        // Assert
        assertTrue(result);
        verify(userRepositoryImp).getIdByUserName(userName);
        verify(modRepo).makeModerator(userId, communityId);
    }

    @Test
    void testSetModerator_Failure() {
        // Arrange
        String userName = "testUser";
        Long communityId = 1L;

        when(userRepositoryImp.getIdByUserName(userName)).thenThrow(new RuntimeException());

        // Act
        boolean result = moderatorService.setModerator(userName, communityId);

        // Assert
        assertFalse(result);
    }

    @Test
    void testRemoveModerator_Bug() {
        // Arrange
        String userName = "testUser";
        Long communityId = 1L;
        Long userId = 100L;

        when(userRepositoryImp.getIdByUserName(userName)).thenReturn(userId);
        when(modRepo.removeModerator(userId , communityId)).thenReturn(false);

        // Act & Assert
        // This test demonstrates the current bug in removeModerator method
        boolean res = moderatorService.removeModerator(userName, communityId);

        assertFalse(res);
        // Verify that makeModerator is incorrectly called instead of removeModerator
        verify(modRepo).removeModerator(userId, communityId);
    }

    @Test
    void testRemoveModerator_Corrected() {
        // This is how the method should work after fixing the bug
        String userName = "testUser";
        Long communityId = 1L;
        Long userId = 100L;

        // Mock a removeModerator method (which doesn't exist in the current implementation)
        when(userRepositoryImp.getIdByUserName(userName)).thenReturn(userId);
        when(modRepo.removeModerator(userId, communityId)).thenReturn(true);

        // Act
        boolean result = moderatorService.removeModerator(userName, communityId);

        // Assert
        assertTrue(result);
        verify(modRepo).removeModerator(userId, communityId);
    }
}