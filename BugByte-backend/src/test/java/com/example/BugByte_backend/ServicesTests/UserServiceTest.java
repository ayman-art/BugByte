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

    //test getProfile user exists
    @Test
    public void testGetProfile_UserExists() throws Exception {
        User Expecteduser = new User(1L,"user12" , "user@gmail.com" , "12345678@", "simple bio");

        // Mock repository methods

        when(userRepositoryMock.findByIdentity(Expecteduser.get_user_name())).thenReturn(Expecteduser);
        when(userProfileRepositoryMock.getFollowersCount(Expecteduser.getId())).thenReturn(5);
        when(userProfileRepositoryMock.getFollowingsCount(Expecteduser.getId())).thenReturn(5);

        Map<String,Object> user = userService.getProfile(Expecteduser.get_user_name());

        assertEquals(Expecteduser.get_user_name(),user.get("user_name"));

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
        User user = new User(1L , "user1" , "user1@example.com" , "12345678%");
        User following = new User(2L , "user2" , "user2@example.com" , "12345678%");

        // Mock repository methods
        when(userRepositoryMock.findById(user.getId())).thenReturn(user);
        when(userRepositoryMock.findByIdentity(following.get_user_name())).thenReturn(following);
        when(userProfileRepositoryMock.isFollowing(user.getId(), following.getId())).thenReturn(false);
        when(userProfileRepositoryMock.followUser(user.getId(), following.getId())).thenReturn(true);


        assertTrue(userService.followUser(user.getId(), following.getUserName()));
    }
    //test follow user doesn't exist
    @Test
    public void testFollow_userDoesNotExist() throws Exception{
        User user = new User(1L , "user1" , "user1@example.com" , "12345678%");
        User following = new User(2L , "user2" , "user2@example.com" , "12345678%");

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
        User user = new User(1L , "user1" , "user1@example.com" , "12345678%");
        User following = new User(2L , "user2" , "user2@example.com" , "12345678%");

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
        User user = new User(1L , "user1" , "user1@example.com" , "12345678%");
        User following = new User(2L , "user2" , "user2@example.com" , "12345678%");

        // Mock repository methods
        when(userRepositoryMock.findById(user.getId())).thenReturn(user);
        when(userRepositoryMock.findByIdentity(following.get_user_name())).thenReturn(following);
        when(userProfileRepositoryMock.isFollowing(user.getId(), following.getId())).thenReturn(true);

        // Assert that an exception is thrown
        assertThrows(Exception.class, () -> {
            userService.followUser(user.getId(), following.getUserName());
        });
    }
    //test unfollow both exist
    @Test
    public void testUnFollow_bothExist() throws Exception{
        User user = new User(1L , "user1" , "user1@example.com" , "12345678%");
        User following = new User(2L , "user2" , "user2@example.com" , "12345678%");

        // Mock repository methods
        when(userRepositoryMock.findById(user.getId())).thenReturn(user);
        when(userRepositoryMock.findByIdentity(following.get_user_name())).thenReturn(following);
        when(userProfileRepositoryMock.isFollowing(user.getId(), following.getId())).thenReturn(true);
        when(userProfileRepositoryMock.unfollowUser(user.getId(), following.getId())).thenReturn(true);


        assertTrue(userService.unfollowUser(user.getId(), following.getUserName()));
    }
    //test follow user doesn't exist
    @Test
    public void testUnFollow_userDoesNotExist() throws Exception{
        User user = new User(1L , "user1" , "user1@example.com" , "12345678%");
        User following = new User(2L , "user2" , "user2@example.com" , "12345678%");

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
        User user = new User(1L , "user1" , "user1@example.com" , "12345678%");
        User following = new User(2L , "user2" , "user2@example.com" , "12345678%");

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
        User user = new User(1L , "user1" , "user1@example.com" , "12345678%");
        User following = new User(2L , "user2" , "user2@example.com" , "12345678%");

        // Mock repository methods
        when(userRepositoryMock.findById(user.getId())).thenReturn(user);
        when(userRepositoryMock.findByIdentity(following.get_user_name())).thenReturn(following);
        when(userProfileRepositoryMock.isFollowing(user.getId(), following.getId())).thenReturn(false);


        // Assert that an exception is thrown
        assertThrows(Exception.class, () -> {
            userService.unfollowUser(user.getId(), following.getUserName());
        });
    }
    //test getFollowings user Exists
    @Test
    public void  testGetFollowings_UserExists() throws Exception {
        User user = new User(1L , "user1" , "user1@example.com" , "12345678%");
        User user1 = new User(2L , "user2" , "user2@example.com" , "12345678%");
        User user2 = new User(3L , "user3" , "user3@example.com" , "12345678%");
        List<User> followings = new ArrayList<>();
        followings.add(user1);
        followings.add(user2);

        // Mock repository methods
        when(userRepositoryMock.findByIdentity(user.get_user_name())).thenReturn(user);
        when(userProfileRepositoryMock.getFollowings(user.getId())).thenReturn(followings);

        List<User> returnedFollowings = userService.getFollowings(user.getUserName());
        assertEquals(returnedFollowings , followings);
    }
    //test getFollowings user Doesn't Exist
    @Test
    public void  testGetFollowings_UserDoesNotExist() throws Exception {
        User user = new User(1L , "user1" , "user1@example.com" , "12345678%");

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
        User user = new User(1L , "user1" , "user1@example.com" , "12345678%");
        User user1 = new User(2L , "user2" , "user2@example.com" , "12345678%");
        User user2 = new User(3L , "user3" , "user3@example.com" , "12345678%");
        List<User> followers = new ArrayList<>();
        followers.add(user1);
        followers.add(user2);

        // Mock repository methods
        when(userRepositoryMock.findByIdentity(user.get_user_name())).thenReturn(user);
        when(userProfileRepositoryMock.getFollowers(user.getId())).thenReturn(followers);

        List<User> returnedFollowings = userService.getFollowers(user.getUserName());
        assertEquals(returnedFollowings, followers);
    }
    //test getFollowers user Doesn't Exist
    @Test
    public void  testGetFollowers_UserDoesNotExist() throws Exception {
        User user = new User(1L , "user1" , "user1@example.com" , "12345678%");

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
        User user = new User(1L , "user1" , "user1@example.com" , "12345678%");
        User admin = new User(2L , "user2" , "user2@example.com" , "12345678%",1L,true);


        // Mock repository methods
        when(userRepositoryMock.findById(admin.getId())).thenReturn(admin);
        when(userRepositoryMock.findByIdentity(user.getUserName())).thenReturn(user);
        when(userRepositoryMock.makeUserAdmin(user.getId())).thenReturn(true);

        assertTrue(userService.makeAdmin(admin.getId(), user.getUserName()));
    }
    //test make admin user Doesn't exist
    @Test
    public void testMakeAdmin_UserDoesNotExist() throws Exception{
        User user = new User(1L , "user1" , "user1@example.com" , "12345678%");
        User admin = new User(2L , "user2" , "user2@example.com" , "12345678%",1L,true);


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
        User user = new User(1L , "user1" , "user1@example.com" , "12345678%");
        User admin = new User(2L , "user2" , "user2@example.com" , "12345678%",1L,true);


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
        User user = new User(1L , "user1" , "user1@example.com" , "12345678%");
        User admin = new User(2L , "user2" , "user2@example.com" , "12345678%",1L,false);


        // Mock repository methods
        when(userRepositoryMock.findById(admin.getId())).thenReturn(admin);
        when(userRepositoryMock.findByIdentity(user.getUserName())).thenReturn(user);

        // Assert that an exception is thrown when admin doesn't exist
        assertThrows(Exception.class, () -> {
            userService.makeAdmin(admin.getId(), user.getUserName());
        });
    }
}
