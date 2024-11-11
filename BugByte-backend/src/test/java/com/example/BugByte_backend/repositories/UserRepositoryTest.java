package com.example.BugByte_backend.repositories;

import com.example.BugByte_backend.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserRepositoryTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserRepositoryImp userRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testInsertUser_success() {
        String username = "username";
        String email = "username@example.com";
        String password = "password";

        when(jdbcTemplate.update(anyString(), anyString(), anyString(), anyString())).thenReturn(1);
        when(passwordEncoder.encode(anyString())).thenReturn(anyString());

        Integer result = userRepository.insertUser(username, email, password);
        assertEquals(1, result);
    }

    @Test
    public void testInsertUser_duplicateEmail() {
        String username = "username";
        String email = "username@example.com";
        String password = "password";

        when(jdbcTemplate.update(anyString(), anyString(), anyString(), anyString())).thenReturn(0);
        Integer result = userRepository.insertUser(username, email, password);
        assertEquals(0, result);
    }

    @Test
    public void testInsertUser_nullValues() {
        String username = null;
        String email = "username@example.com";
        String password = "password";

        assertThrows(NullPointerException.class, () -> userRepository.insertUser(username, email, password));
    }

    @Test
    public void testFindByIdentityAndPassword_success() {
        Long id = 1L;
        String username = "username";
        String email = "username@example.com";
        String password = "password";
        User user = new User(id, username, email, password);

        when(jdbcTemplate.queryForObject(anyString(), any(RowMapper.class), anyString(), anyString())).thenReturn(user);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        User result = userRepository.findByIdentityAndPassword(email, password);
        assertEquals("username", result.get_user_name());
    }

    @Test
    public void testFindByIdentityAndPassword_wrongPassword() {
        String username = "username";
        String password = "password";

        when(jdbcTemplate.queryForObject(anyString(), any(RowMapper.class), anyString(), anyString())).thenReturn(null);

        User result = userRepository.findByIdentityAndPassword(username, password);
        assertNull(result);
    }

    @Test
    public void testFindByIdentityAndPassword_nonExistentUser() {
        String username = "username";
        String password = "password";

        when(jdbcTemplate.queryForObject(anyString(), any(RowMapper.class), anyString(), anyString())).thenReturn(new RuntimeException("User not found"));

        assertThrows(RuntimeException.class, () -> userRepository.findByIdentityAndPassword(username, password));
    }

    @Test
    public void testFindById_success() {
        Long id = 1L;
        String username = "username";
        String email = "username@example.com";
        String password = "password";

        User user = new User(id, username, email, password);

        when(jdbcTemplate.queryForObject(anyString(), any(RowMapper.class), anyLong())).thenReturn(user);

        User result = userRepository.findById(1L);
        assertEquals(1L, result.getId());
    }

    @Test
    public void testFindById_userNotFound() {
        when(jdbcTemplate.queryForObject(anyString(), any(RowMapper.class), anyLong())).thenReturn(null);

        User result = userRepository.findById(404L);
        assertNull(result);
    }

    @Test
    public void testFindById_invalidId() {
        assertThrows(NullPointerException.class, () -> userRepository.findById(null));
    }

    @Test
    public void testChangePassword_success() {
        when(jdbcTemplate.update(anyString(), anyString(), anyLong())).thenReturn(1);
        when(passwordEncoder.encode(anyString())).thenReturn(anyString());

        Boolean result = userRepository.changePassword(1L, "newPassword");
        assertTrue(result);
    }

    @Test
    public void testChangePassword_userNotFound() {
        when(jdbcTemplate.update(anyString(), anyString(), anyLong())).thenReturn(0);

        Boolean result = userRepository.changePassword(404L, "newPassword");
        assertFalse(result);
    }

    @Test
    public void testChangePassword_nullPassword() {
        assertThrows(NullPointerException.class, () -> userRepository.changePassword(1L, null));
    }

    @Test
    public void testDeleteUser_success() {
        when(jdbcTemplate.update(anyString(), any(Long.class))).thenReturn(1);
        Boolean result = userRepository.deleteUser(1L);
        assertTrue(result);
    }

    @Test
    public void testDeleteUser_userNotFound() {
        when(jdbcTemplate.update(anyString(), any(Long.class))).thenReturn(0);
        Boolean result = userRepository.deleteUser(404L);
        assertFalse(result);
    }

    @Test
    public void testDeleteUser_nullUserId() {
        assertThrows(NullPointerException.class, () -> userRepository.deleteUser(null));
    }

    @Test
    public void testMakeUserAdmin_success() {
        when(jdbcTemplate.update(anyString(), any(Long.class))).thenReturn(1);
        Boolean result = userRepository.makeUserAdmin(1L);
        assertTrue(result);
    }

    @Test
    public void testMakeUserAdmin_userNotFound() {
        when(jdbcTemplate.update(anyString(), any(Long.class))).thenReturn(0);
        Boolean result = userRepository.makeUserAdmin(404L);
        assertFalse(result);
    }

    @Test
    public void testMakeUserAdmin_nullUserId() {
        assertThrows(NullPointerException.class, () -> userRepository.makeUserAdmin(null));
    }
}
