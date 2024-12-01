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
    private static final String SQL_INSERT_USER = """
                INSERT INTO users
                    (user_name, email, password, reputation, is_admin)
                VALUES
                    (?, ?, ?, 0, false);
            """;
    private static final String SQL_FIND_BY_ID = "SELECT * FROM users WHERE id = ?;";
    private static final String SQL_FIND_ID_BY_EMAIL = "SELECT id FROM users WHERE email = ?;";
    private static final String SQL_FIND_BY_IDENTITY = "SELECT * FROM users WHERE email = ? OR user_name = ?;";
    private static final String SQL_CHANGE_PASSWORD = "UPDATE users SET password = ? WHERE id = ?";
    private static final String SQL_DELETE_USER_BY_ID = "DELETE FROM users WHERE id = ?;";
    private static final String SQL_MAKE_USER_ADMIN = "UPDATE users SET is_admin = true WHERE id = ?";
    private static final String SQL_INSERT_VALIDATION_CODE = """
                INSERT INTO validation_code
                    (id, code)
                VALUES
                    (?, ?);
            """;
    private static final String SQL_DELETE_VALIDATION_CODE = "DELETE FROM validation_code WHERE code = ?;";
    private static final String SQL_COUNT_VALIDATION_CODE = "SELECT COUNT(*) AS count FROM validation_code WHERE code = ?";
    private static final String SQL_COUNT_USER_IN_VALIDATION_CODE = "SELECT COUNT(*) AS count FROM validation_code WHERE id = ?";
    private static final String SQL_FIND_VALIDATION_CODE_BY_ID = "SELECT code FROM validation_code WHERE id = ?;";
    private static final String SQL_UPDATE_VALIDATION_CODE = "UPDATE validation_code SET code = ? WHERE id = ?;";

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
        String hashedPassword = "hashedPassword";

        when(jdbcTemplate.update(eq(SQL_INSERT_USER), eq(username), eq(email), eq(hashedPassword))).thenReturn(1);
        when(jdbcTemplate.queryForObject(eq(SQL_FIND_ID_BY_EMAIL), eq(new Object[]{ email }), eq(Long.class))).thenReturn(7L);

        when(passwordEncoder.encode(eq(password))).thenReturn(hashedPassword);

        Long result = userRepository.insertUser(username, email, password);
        assertEquals(7L, result);
    }

    @Test
    public void testInsertUser_duplicateEmail() {
        String username = "username";
        String email = "username@example.com";
        String password = "password";
        String hashedPassword = "hashedPassword";

        when(jdbcTemplate.update(eq(SQL_INSERT_USER), eq(username), eq(email), eq(hashedPassword))).thenReturn(0);
        when(passwordEncoder.encode(eq(password))).thenReturn(hashedPassword);

        assertThrows(RuntimeException.class, () -> userRepository.insertUser(username, email, password));
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
        String hashedPassword = "hashedPassword";
        User user = new User(id, username, email, hashedPassword);

        when(jdbcTemplate.queryForObject(eq(SQL_FIND_BY_IDENTITY), any(RowMapper.class), eq(email), eq(email))).thenReturn(user);
        when(passwordEncoder.matches(eq(password), eq(hashedPassword))).thenReturn(true);

        User result = userRepository.findByIdentityAndPassword(email, password);
        assertEquals("username", result.getUserName());
    }

    @Test
    public void testFindByIdentityAndPassword_wrongPassword() {
        String username = "username";
        String password = "password";

        when(jdbcTemplate.queryForObject(eq(SQL_FIND_BY_IDENTITY), any(RowMapper.class), eq(username), eq(username))).thenReturn(null);

        User result = userRepository.findByIdentityAndPassword(username, password);
        assertNull(result);
    }

    @Test
    public void testFindByIdentityAndPassword_nonExistentUser() {
        String username = "username";
        String password = "password";

        when(jdbcTemplate.queryForObject(eq(SQL_FIND_BY_IDENTITY), any(RowMapper.class), eq(username), eq(username))).thenThrow(new RuntimeException("User not found"));

        assertThrows(RuntimeException.class, () -> userRepository.findByIdentityAndPassword(username, password));
    }

    @Test
    public void testFindByIdentity_withUsername() {
        String identity = "username";
        User expectedUser = new User(1L, identity, "email@example.com", "password");

        when(jdbcTemplate.queryForObject(eq(SQL_FIND_BY_IDENTITY), any(RowMapper.class), eq(identity), eq(identity)))
                .thenReturn(expectedUser);

        User result = userRepository.findByIdentity(identity);

        assertNotNull(result);
        assertEquals(expectedUser, result);
    }

    @Test
    public void testFindByIdentity_withEmail() {
        String identity = "email@example.com";
        User expectedUser = new User(1L, "username", identity, "password");

        when(jdbcTemplate.queryForObject(eq(SQL_FIND_BY_IDENTITY), any(RowMapper.class), eq(identity), eq(identity)))
                .thenReturn(expectedUser);

        User result = userRepository.findByIdentity(identity);

        assertNotNull(result);
        assertEquals(expectedUser, result);
    }

    @Test
    public void testFindByIdentity_notFound() {
        String identity = "nonexistent";
        when(jdbcTemplate.queryForObject(eq(SQL_FIND_BY_IDENTITY), any(RowMapper.class), eq(identity), eq(identity)))
                .thenReturn(null);

        User result = userRepository.findByIdentity(identity);

        assertNull(result);
    }

    @Test
    public void testFindById_success() {
        Long id = 1L;
        String username = "username";
        String email = "username@example.com";
        String password = "password";

        User user = new User(id, username, email, password);

        when(jdbcTemplate.queryForObject(eq(SQL_FIND_BY_ID), any(RowMapper.class), eq(id))).thenReturn(user);

        User result = userRepository.findById(1L);
        assertEquals(1L, result.getId());
    }

    @Test
    public void testFindById_userNotFound() {
        Long id = 101L;
        when(jdbcTemplate.queryForObject(eq(SQL_FIND_BY_ID), any(RowMapper.class), eq(id))).thenReturn(null);

        User result = userRepository.findById(404L);
        assertNull(result);
    }

    @Test
    public void testFindById_invalidId() {
        assertThrows(NullPointerException.class, () -> userRepository.findById(null));
    }

    @Test
    public void testChangePassword_success() {
        Long userId = 1L;
        String password = "password";
        String hashedPassword = "hashedPassword";

        when(jdbcTemplate.update(eq(SQL_CHANGE_PASSWORD), eq(hashedPassword), eq(userId))).thenReturn(1);
        when(passwordEncoder.encode(eq(password))).thenReturn(hashedPassword);

        Boolean result = userRepository.changePassword(userId, password);
        assertTrue(result);
    }

    @Test
    public void testChangePassword_userNotFound() {
        Long userId = 404L;
        String password = "password";
        String hashedPassword = "hashedPassword";
        when(jdbcTemplate.update(eq(SQL_CHANGE_PASSWORD), eq(hashedPassword), eq(userId))).thenReturn(0);

        Boolean result = userRepository.changePassword(userId, password);
        assertFalse(result);
    }

    @Test
    public void testChangePassword_nullPassword() {
        assertThrows(NullPointerException.class, () -> userRepository.changePassword(1L, null));
    }

    @Test
    public void testDeleteUser_success() {
        Long userId = 1L;
        when(jdbcTemplate.update(eq(SQL_DELETE_USER_BY_ID), eq(userId))).thenReturn(1);
        Boolean result = userRepository.deleteUser(userId);
        assertTrue(result);
    }

    @Test
    public void testDeleteUser_userNotFound() {
        Long userId = 404L;
        when(jdbcTemplate.update(eq(SQL_DELETE_USER_BY_ID), eq(userId))).thenReturn(0);
        Boolean result = userRepository.deleteUser(userId);
        assertFalse(result);
    }

    @Test
    public void testDeleteUser_nullUserId() {
        assertThrows(NullPointerException.class, () -> userRepository.deleteUser(null));
    }

    @Test
    public void testMakeUserAdmin_success() {
        Long userId = 1L;
        when(jdbcTemplate.update(eq(SQL_MAKE_USER_ADMIN), eq(userId))).thenReturn(1);
        Boolean result = userRepository.makeUserAdmin(1L);
        assertTrue(result);
    }

    @Test
    public void testMakeUserAdmin_userNotFound() {
        Long userId = 0L;
        when(jdbcTemplate.update(eq(SQL_MAKE_USER_ADMIN), eq(userId))).thenReturn(0);
        Boolean result = userRepository.makeUserAdmin(userId);
        assertFalse(result);
    }

    @Test
    public void testMakeUserAdmin_nullUserId() {
        assertThrows(NullPointerException.class, () -> userRepository.makeUserAdmin(null));
    }

    @Test
    public void testAddResetCode_success() {
        Long userId = 1L;
        String code = "resetCode";

        when(jdbcTemplate.queryForObject(eq(SQL_COUNT_USER_IN_VALIDATION_CODE), eq(new Object[]{ userId }), eq(Integer.class)))
                .thenReturn(0);
        when(jdbcTemplate.update(eq(SQL_INSERT_VALIDATION_CODE), eq(userId), eq(code))).thenReturn(1);

        Boolean result = userRepository.addResetCode(userId, code);

        assertTrue(result);
    }

    @Test
    public void testAddResetCode_fail() {
        Long userId = 1L;
        String code = "resetCode";

        when(jdbcTemplate.queryForObject(eq(SQL_COUNT_USER_IN_VALIDATION_CODE), eq(new Object[]{ userId }), eq(Integer.class)))
                .thenReturn(0);
        when(jdbcTemplate.update(eq(SQL_INSERT_VALIDATION_CODE), eq(userId), eq(code))).thenReturn(0);

        Boolean result = userRepository.addResetCode(userId, code);

        assertFalse(result);
    }

    @Test
    public void testDeleteResetCode_success() {
        String code = "resetCode";

        when(jdbcTemplate.update(eq(SQL_DELETE_VALIDATION_CODE), eq(code))).thenReturn(1);

        Boolean result = userRepository.deleteResetCode(code);

        assertTrue(result);
    }

    @Test
    public void testDeleteResetCode_fail() {
        String code = "resetCode";

        when(jdbcTemplate.update(eq(SQL_DELETE_VALIDATION_CODE), eq(code))).thenReturn(0);

        Boolean result = userRepository.deleteResetCode(code);

        assertFalse(result);
    }

    @Test
    public void testCodeExists_true() {
        String code = "resetCode";

        when(jdbcTemplate.queryForObject(eq(SQL_COUNT_VALIDATION_CODE), eq(new Object[]{ code }), eq(Integer.class)))
                .thenReturn(1);

        Boolean result = userRepository.codeExists(code);

        assertTrue(result);
    }

    @Test
    public void testCodeExists_false() {
        String code = "resetCode";

        when(jdbcTemplate.queryForObject(eq(SQL_COUNT_VALIDATION_CODE), eq(new Object[]{ code }), eq(Integer.class)))
                .thenReturn(0);

        Boolean result = userRepository.codeExists(code);

        assertFalse(result);
    }

    @Test
    public void testGetCodeById_success() {
        Long id = 1L;
        String expectedCode = "resetCode";

        when(jdbcTemplate.queryForObject(eq(SQL_FIND_VALIDATION_CODE_BY_ID), eq(new Object[]{ id }), eq(String.class)))
                .thenReturn(expectedCode);

        String result = userRepository.getCodeById(id);

        assertNotNull(result);
        assertEquals(expectedCode, result);
    }

    @Test
    public void testGetCodeById_notFound() {
        Long id = 1L;

        when(jdbcTemplate.queryForObject(eq(SQL_FIND_VALIDATION_CODE_BY_ID), eq(new Object[]{ id }), eq(String.class)))
                .thenReturn(null);

        String result = userRepository.getCodeById(id);

        assertNull(result);
    }
}
