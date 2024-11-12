package com.example.BugByte_backend.ServicesTests;
import com.example.BugByte_backend.models.User;
import com.example.BugByte_backend.repositories.UserRepositoryImp;
import com.example.BugByte_backend.services.RegistrationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class RegistrationServiceTest {

    @Mock
    private UserRepositoryImp userRepositoryMock;

    @InjectMocks
    private RegistrationService registrationService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    //test registration if user doesn't exist
    @Test
    public void testRegisterUser_UserDoesNotExist() throws Exception {
        // create user data
        String email = "user@example.com";
        String userName = "user12";
        String password = "12345678@";
        long expectedUserId = 1L;

        // Mock repository methods

        when(userRepositoryMock.insertUser(eq(userName), eq(email), eq(password))).thenReturn(expectedUserId);

        long actualUserId = registrationService.registerUser(email, userName, password);

        assertEquals(expectedUserId, actualUserId);
    }
    //test register user if user exists
    @Test
    public void testRegisterUser_UserAlreadyExists() {
        // create user data
        String email = "user@example.com";
        String userName = "user12";
        String password = "12345678@";

        // Mock repository methods
        when(userRepositoryMock.insertUser(eq(userName), eq(email), eq(password)))
                .thenThrow(new RuntimeException("User already exists"));

        // Assert that an exception is thrown when the username already exists
        assertThrows(Exception.class, () -> {
            registrationService.registerUser(email, userName, password);
        });
    }
    //test registration if invalid email
    @Test
    public void testRegisterUser_EmailInvalid() throws Exception {
        // create user data
        String email = "user.com";
        String userName = "user12";
        String password = "12345678@";
        long expectedUserId = 1L;

        assertThrows(Exception.class, () -> {
            registrationService.registerUser(email, userName, password);
        });
    }
    //test registration if invalid email
    @Test
    public void testRegisterUser_EmailInvalid1() throws Exception {
        // create user data
        String email = "user@example@example.com";
        String userName = "user12";
        String password = "12345678@";
        long expectedUserId = 1L;

        assertThrows(Exception.class, () -> {
            registrationService.registerUser(email, userName, password);
        });
    }
    //test registration if invalid username
    @Test
    public void testRegisterUser_UserNameInvalid() throws Exception {
        // create user data
        String email = "user@example.com";
        String userName = "123";
        String password = "12345678@";
        long expectedUserId = 1L;

        assertThrows(Exception.class, () -> {
            registrationService.registerUser(email, userName, password);
        });
    }
    //test registration if invalid username
    @Test
    public void testRegisterUser_WeekPassword() throws Exception {
        // create user data
        String email = "user@example.com";
        String userName = "user12";
        String password = "12345678";
        long expectedUserId = 1L;

        assertThrows(Exception.class, () -> {
            registrationService.registerUser(email, userName, password);
        });
    }
    //test login if user doesn't exist
    @Test
    public void testLoginUser_UserDoesNotExist() throws Exception {
        // create user data
        String identity = "user@example.com";
        String password = "12345678@";
        // Mock repository methods

        when(userRepositoryMock.findByIdentityAndPassword(eq(identity) , eq(password))).thenReturn(null);


        // Assert that an exception is thrown when the user doesn't exist
        assertThrows(Exception.class, () -> {
            registrationService.loginUser(identity, password);
        });
    }
    //test login if user exists
    @Test
    public void testLoginUser_UserExists() throws Exception {
        // create user data
        String identity = "user@example.com";
        String password = "12345678@";
        User user = new User("user12" , "user@example.com" , "12345678@");
        // Mock repository methods
        when(userRepositoryMock.findByIdentityAndPassword(identity , password)).thenReturn(user);

        User newUser = registrationService.loginUser(identity , password);

        // Assert the user is returned
        assertEquals(newUser.get_user_name() , user.get_user_name());
        assertEquals(newUser.getEmail() , user.getEmail());
        assertEquals(newUser.getPassword() , user.getPassword());

    }
    //test logout if user exists
    @Test
    public void logoutUser_UserExists() throws Exception {
        long id = 1L;
        // Mock repository methods
        when(userRepositoryMock.findById(id)).thenReturn(new User("user12" , "user@example.com" , "12345678@"));

        User newUser = registrationService.logoutUser(id);

        // Assert the user is returned
        assertEquals(newUser.get_user_name() , "user12");
        assertEquals(newUser.getEmail() , "user@example.com");
        assertEquals(newUser.getPassword() ,"12345678@");

    }
    //test logout if user doesn't exist
    @Test
    public void logoutUser_UserDoesNotExist() throws Exception {
        long id = 1L;
        // Mock repository methods
        when(userRepositoryMock.findById(id)).thenReturn(null);

        // Assert that an exception is thrown when the user doesn't exist
        assertThrows(Exception.class, () -> {
            registrationService.logoutUser(id);
        });
    }
    //test delete user if user exists
    @Test
    public void deleteUser_UserExists() throws Exception {
        long id = 1L;
        // Mock repository methods
        when(userRepositoryMock.findById(id)).thenReturn(new User("user12" , "user@example.com" , "12345678@"));

        when(userRepositoryMock.deleteUser(id)).thenReturn(true);

        // Assert the user is deleted
        assertTrue(registrationService.deleteUser(id));
    }
    //test delete user if user doesn't exist
    @Test
    public void deleteUser_UserDoesNotExist() throws Exception {
        long id = 1L;
        // Mock repository methods
        when(userRepositoryMock.findById(id)).thenReturn(null);

        // Assert that an exception is thrown when the user doesn't exist
        assertThrows(Exception.class, () -> {
            registrationService.deleteUser(id);
        });
    }
    //test change password if user exists
    @Test
    public void changePassword_UserDoesExists() throws Exception {
        long id = 1L;
        String newPassword = "13579";

        // Mock repository methods
        when(userRepositoryMock.findById(id)).thenReturn(new User("user12" , "user@example.com" , "12345678@"));
        when(userRepositoryMock.changePassword(id , newPassword)).thenReturn(true);

        // Assert the user is deleted
        assertTrue(registrationService.changePassword(id , newPassword));
    }
    //test change password if user DoesNot exist
    @Test
    public void changePassword_UserDoesNotExist() throws Exception {
        long id = 1L;
        String newPassword = "13579";

        // Mock repository methods
        when(userRepositoryMock.findById(id)).thenReturn(null);

        // Assert that an exception is thrown when the user doesn't exist
        assertThrows(Exception.class, () -> {
            registrationService.deleteUser(id);
        });
    }
}
