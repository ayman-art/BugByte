package com.example.BugByte_backend.ServicesTests;
import com.example.BugByte_backend.models.User;
import com.example.BugByte_backend.repositories.UserRepositoryImp;
import com.example.BugByte_backend.repositories.userProfileRepository;
import com.example.BugByte_backend.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserServiceTest {
    @Mock
    private UserRepositoryImp userRepositoryMock;

    @Mock
    private userProfileRepository userProfileRepositoryMock;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testUpdatePicture_Success() throws Exception {
        // Arrange
        long userId = 1L;
        String pictureURL = "https://example.com/picture.jpg";
        User mockUser = new User();
        mockUser.setId(userId);
        mockUser.setPicture(pictureURL);

        when(userRepositoryMock.findById(userId)).thenReturn(mockUser);

        // Act
        userService.updatePicture(userId, pictureURL);

        // Assert
        verify(userRepositoryMock).updateProfilePicture(userId, pictureURL);
        verify(userRepositoryMock).findById(userId);
        // Assuming cacheUser is a private method, verify its behavior indirectly
    }


    @Test
    void testUpdatePicture_RepositoryThrowsException() throws Exception {
        // Arrange
        long userId = 1L;
        String pictureURL = "https://example.com/picture.jpg";

        doThrow(new RuntimeException("Database error"))
                .when(userRepositoryMock).updateProfilePicture(userId, pictureURL);

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            userService.updatePicture(userId, pictureURL);
        });

        assertEquals("Database error", exception.getMessage());
        verify(userRepositoryMock).updateProfilePicture(userId, pictureURL);
        verify(userRepositoryMock, never()).findById(userId);
    }
    //test getProfile user exists
    @Test
    public void testGetProfile_UserExists() throws Exception {
        User Expecteduser = User.builder()
                .id(1L)
                .userName("user12")
                .email("user@gmail.com")
                .password("12345678@")
                .bio("simple bio")
                .build();

        // Mock repository methods

        when(userRepositoryMock.findByIdentity(Expecteduser.getUserName())).thenReturn(Expecteduser);
        when(userProfileRepositoryMock.getFollowersCount(Expecteduser.getId())).thenReturn(5);
        when(userProfileRepositoryMock.getFollowingsCount(Expecteduser.getId())).thenReturn(5);

        Map<String,Object> user = userService.getProfile(Expecteduser.getUserName());

        assertEquals(Expecteduser.getUserName(),user.get("userName"));

    }
    //test getProfile user doesn't exist
    @Test
    public void testGetProfile_UserDoesNotExist() throws Exception {
         String userName = "user12";
        // Mock repository methods
        when(userRepositoryMock.findByIdentity(userName)).thenReturn(null);

        // Assert that an exception is thrown when user doesn't exist
        assertThrows(Exception.class, () -> {
            userService.getProfile(userName);
        });
    }
    //test follow both exist
    @Test
    public void testFollow_bothExist() throws Exception{
        User user = User.builder()
                .id(1L)
                .userName("user1")
                .email("user1@example.com")
                .password("12345678%")
                .build();
        User following = User.builder()
                .id(2L)
                .userName("user2")
                .email("user2@example.com")
                .password("12345678%")
                .build();

        // Mock repository methods
        when(userRepositoryMock.findById(user.getId())).thenReturn(user);
        when(userRepositoryMock.findByIdentity(following.getUserName())).thenReturn(following);
        when(userProfileRepositoryMock.isFollowing(user.getId(), following.getId())).thenReturn(false);
        when(userProfileRepositoryMock.followUser(user.getId(), following.getId())).thenReturn(true);


        assertTrue(userService.followUser(user.getId(), following.getUserName()));
    }
    //test follow user doesn't exist
    @Test
    public void testFollow_userDoesNotExist() throws Exception{
        User user = User.builder()
                .id(1L)
                .userName("user1")
                .email("user1@example.com")
                .password("12345678%")
                .build();
        User following = User.builder()
                .id(2L)
                .userName("user2")
                .email("user2@example.com")
                .password("12345678%")
                .build();

        // Mock repository methods
        when(userRepositoryMock.findById(user.getId())).thenReturn(null);
        when(userRepositoryMock.findByIdentity(following.getUserName())).thenReturn(following);

        // Assert that an exception is thrown when user doesn't exist
        assertThrows(Exception.class, () -> {
            userService.followUser(user.getId() , following.getUserName());
        });
    }
    //test follow following doesn't exist
    @Test
    public void testFollow_followingDoesNotExist() throws Exception{
        User user = User.builder()
                .id(1L)
                .userName("user1")
                .email("user1@example.com")
                .password("12345678%")
                .build();
        User following = User.builder()
                .id(2L)
                .userName("user2")
                .email("user2@example.com")
                .password("12345678%")
                .build();

        // Mock repository methods
        when(userRepositoryMock.findById(user.getId())).thenReturn(user);
        when(userRepositoryMock.findByIdentity(following.getUserName())).thenReturn(null);

        // Assert that an exception is thrown when following doesn't exist
        assertThrows(Exception.class, () -> {
            userService.followUser(user.getId(), following.getUserName());
        });
    }
    //test follow user already follow this user
    @Test
    public void testFollow_AlreadyFollow() throws Exception{
        User user = User.builder()
                .id(1L)
                .userName("user1")
                .email("user1@example.com")
                .password("12345678%")
                .build();
        User following = User.builder()
                .id(2L)
                .userName("user2")
                .email("user2@example.com")
                .password("12345678%")
                .build();

        // Mock repository methods
        when(userRepositoryMock.findById(user.getId())).thenReturn(user);
        when(userRepositoryMock.findByIdentity(following.getUserName())).thenReturn(following);
        when(userProfileRepositoryMock.isFollowing(user.getId(), following.getId())).thenReturn(true);

        // Assert that an exception is thrown
        assertThrows(Exception.class, () -> {
            userService.followUser(user.getId(), following.getUserName());
        });
    }
    //test unfollow both exist
    @Test
    public void testUnFollow_bothExist() throws Exception{
        User user = User.builder()
                .id(1L)
                .userName("user1")
                .email("user1@example.com")
                .password("12345678%")
                .build();
        User following = User.builder()
                .id(2L)
                .userName("user2")
                .email("user2@example.com")
                .password("12345678%")
                .build();

        // Mock repository methods
        when(userRepositoryMock.findById(user.getId())).thenReturn(user);
        when(userRepositoryMock.findByIdentity(following.getUserName())).thenReturn(following);
        when(userProfileRepositoryMock.isFollowing(user.getId(), following.getId())).thenReturn(true);
        when(userProfileRepositoryMock.unfollowUser(user.getId(), following.getId())).thenReturn(true);


        assertTrue(userService.unfollowUser(user.getId(), following.getUserName()));
    }
    //test follow user doesn't exist
    @Test
    public void testUnFollow_userDoesNotExist() throws Exception{
        User user = User.builder()
                .id(1L)
                .userName("user1")
                .email("user1@example.com")
                .password("12345678%")
                .build();
        User following = User.builder()
                .id(2L)
                .userName("user2")
                .email("user2@example.com")
                .password("12345678%")
                .build();

        // Mock repository methods
        when(userRepositoryMock.findById(user.getId())).thenReturn(null);
        when(userRepositoryMock.findByIdentity(following.getUserName())).thenReturn(following);

        // Assert that an exception is thrown when user doesn't exist
        assertThrows(Exception.class, () -> {
            userService.unfollowUser(user.getId(), following.getUserName());
        });
    }
    //test unfollow following doesn't exist
    @Test
    public void testUnFollow_followingDoesNotExist() throws Exception{
        User user = User.builder()
                .id(1L)
                .userName("user1")
                .email("user1@example.com")
                .password("12345678%")
                .build();
        User following = User.builder()
                .id(2L)
                .userName("user2")
                .email("user2@example.com")
                .password("12345678%")
                .build();

        // Mock repository methods
        when(userRepositoryMock.findById(user.getId())).thenReturn(user);
        when(userRepositoryMock.findByIdentity(following.getUserName())).thenReturn(null);

        // Assert that an exception is thrown when following doesn't exist
        assertThrows(Exception.class, () -> {
            userService.unfollowUser(user.getId(), following.getUserName());
        });
    }
    //test follow user doesn't follow this user
    @Test
    public void testFollow_DoesNotFollow() throws Exception{
        User user = User.builder()
                .id(1L)
                .userName("user1")
                .email("user1@example.com")
                .password("12345678%")
                .build();
        User following = User.builder()
                .id(2L)
                .userName("user2")
                .email("user2@example.com")
                .password("12345678%")
                .build();

        // Mock repository methods
        when(userRepositoryMock.findById(user.getId())).thenReturn(user);
        when(userRepositoryMock.findByIdentity(following.getUserName())).thenReturn(following);
        when(userProfileRepositoryMock.isFollowing(user.getId(), following.getId())).thenReturn(false);


        // Assert that an exception is thrown
        assertThrows(Exception.class, () -> {
            userService.unfollowUser(user.getId(), following.getUserName());
        });
    }
    //test getFollowings user Exists
    @Test
    public void  testGetFollowings_UserExists() throws Exception {
        User user = User.builder()
                .id(1L)
                .userName("user1")
                .email("user1@example.com")
                .password("12345678%")
                .build();
        User user1 = User.builder()
                .id(2L)
                .userName("user2")
                .email("user2@example.com")
                .password("12345678%")
                .build();
        User user2 = User.builder()
                .id(3L)
                .userName("user2")
                .email("user2@example.com")
                .password("12345678%")
                .build();

        List<User> followings = new ArrayList<>();
        followings.add(user1);
        followings.add(user2);

        // Mock repository methods
        when(userRepositoryMock.findByIdentity(user.getUserName())).thenReturn(user);
        when(userProfileRepositoryMock.getFollowings(user.getId())).thenReturn(followings);

        List<User> returnedFollowings = userService.getFollowings(user.getUserName());
        assertEquals(returnedFollowings , followings);
    }
    //test getFollowings user Doesn't Exist
    @Test
    public void  testGetFollowings_UserDoesNotExist() throws Exception {
        User user = User.builder()
                .id(1L)
                .userName("user1")
                .email("user1@example.com")
                .password("12345678%")
                .build();

        // Mock repository methods
        when(userRepositoryMock.findByIdentity(user.getUserName())).thenReturn(null);

        // Assert that an exception is thrown when user doesn't exist
        assertThrows(Exception.class, () -> {
            userService.getFollowings(user.getUserName());
        });
    }
    //test getFollowers user Exists
    @Test
    public void  testGetFollowers_UserExists() throws Exception {
        User user = User.builder()
                .id(1L)
                .userName("user1")
                .email("user1@example.com")
                .password("12345678%")
                .build();
        User user1 = User.builder()
                .id(2L)
                .userName("user2")
                .email("user2@example.com")
                .password("12345678%")
                .build();
        User user2 = User.builder()
                .id(3L)
                .userName("user2")
                .email("user2@example.com")
                .password("12345678%")
                .build();
        List<User> followers = new ArrayList<>();
        followers.add(user1);
        followers.add(user2);

        // Mock repository methods
        when(userRepositoryMock.findByIdentity(user.getUserName())).thenReturn(user);
        when(userProfileRepositoryMock.getFollowers(user.getId())).thenReturn(followers);

        List<User> returnedFollowings = userService.getFollowers(user.getUserName());
        assertEquals(returnedFollowings, followers);
    }
    //test getFollowers user Doesn't Exist
    @Test
    public void  testGetFollowers_UserDoesNotExist() throws Exception {
        User user = User.builder()
                .id(1L)
                .userName("user1")
                .email("user1@example.com")
                .password("12345678%")
                .build();

        // Mock repository methods
        when(userRepositoryMock.findByIdentity(user.getUserName())).thenReturn(null);

        // Assert that an exception is thrown when user doesn't exist
        assertThrows(Exception.class, () -> {
            userService.getFollowers(user.getUserName());
        });
    }
    //test make admin both exist
    @Test
    public void testMakeAdmin_bothExist() throws Exception{
        User user = User.builder()
                .id(1L)
                .userName("user1")
                .email("user1@example.com")
                .password("12345678%")
                .build();
        User admin = User.builder()
                .id(2L)
                .userName("user1")
                .email("user2@example.com")
                .password("12345678%")
                .reputation(1L)
                .isAdmin(true)
                .build();

        // Mock repository methods
        when(userRepositoryMock.findById(admin.getId())).thenReturn(admin);
        when(userRepositoryMock.findByIdentity(user.getUserName())).thenReturn(user);
        when(userRepositoryMock.makeUserAdmin(user.getId())).thenReturn(true);

        assertTrue(userService.makeAdmin(admin.getId(), user.getUserName()));
    }
    //test make admin user Doesn't exist
    @Test
    public void testMakeAdmin_UserDoesNotExist() throws Exception{
        User user = User.builder()
                .id(1L)
                .userName("user1")
                .email("user1@example.com")
                .password("12345678%")
                .build();
        User admin = User.builder()
                .id(2L)
                .userName("user1")
                .email("user2@example.com")
                .password("12345678%")
                .reputation(1L)
                .isAdmin(true)
                .build();

        // Mock repository methods
        when(userRepositoryMock.findById(admin.getId())).thenReturn(admin);
        when(userRepositoryMock.findByIdentity(user.getUserName())).thenReturn(null);

        // Assert that an exception is thrown when user doesn't exist
        assertThrows(Exception.class, () -> {
            userService.makeAdmin(admin.getId(), user.getUserName());
        });
    }
    //test make admin , admin Doesn't exist
    @Test
    public void testMakeAdmin_AdminDoesNotExist() throws Exception{
        User user = User.builder()
                .id(1L)
                .userName("user1")
                .email("user1@example.com")
                .password("12345678%")
                .build();
        User admin = User.builder()
                .id(2L)
                .userName("user1")
                .email("user2@example.com")
                .password("12345678%")
                .reputation(1L)
                .isAdmin(true)
                .build();

        // Mock repository methods
        when(userRepositoryMock.findById(admin.getId())).thenReturn(null);
        when(userRepositoryMock.findByIdentity(user.getUserName())).thenReturn(user);

        // Assert that an exception is thrown when admin doesn't exist
        assertThrows(Exception.class, () -> {
            userService.makeAdmin(admin.getId(), user.getUserName());
        });
    }
    //test make admin , admin doesn't have the authority
    @Test
    public void testMakeAdmin_AdminDoesNotHaveTheAuthority() throws Exception{
        User user = User.builder()
                .id(1L)
                .userName("user1")
                .email("user1@example.com")
                .password("12345678%")
                .build();
        User admin = User.builder()
                .id(2L)
                .userName("user2")
                .email("user2@example.com")
                .password("12345678%")
                .reputation(1L)
                .isAdmin(false)
                .build();

        // Mock repository methods
        when(userRepositoryMock.findById(admin.getId())).thenReturn(admin);
        when(userRepositoryMock.findByIdentity(user.getUserName())).thenReturn(user);

        // Assert that an exception is thrown when admin doesn't exist
        assertThrows(Exception.class, () -> {
            userService.makeAdmin(admin.getId(), user.getUserName());
        });
    }
    //test updateprofile user exists
    @Test
    public void updateProfile_userExists() throws Exception{
        User user = User.builder()
                .id(1L)
                .userName("user1")
                .email("user1@example.com")
                .password("12345678%")
                .build();
        String newPio = "newPio";


        // Mock repository methods
        when(userRepositoryMock.findById(user.getId())).thenReturn(user);
        when(userProfileRepositoryMock.updateBio(newPio , user.getId())).thenReturn(true);

        assertTrue(userService.updateProfile(newPio , user.getId()));
    }
    //test updateprofile Doesn't user exist
    @Test
    public void updateProfile_userDoesNotExist() throws Exception{
        User user = User.builder()
                .id(1L)
                .userName("user1")
                .email("user1@example.com")
                .password("12345678%")
                .build();
        String newPio = "newPio";


        // Mock repository methods
        when(userRepositoryMock.findById(user.getId())).thenReturn(null);
        // Assert that an exception is thrown when user doesn't exist
        assertThrows(Exception.class, () -> {
            userService.updateProfile(newPio , user.getId());
        });
    }
}
