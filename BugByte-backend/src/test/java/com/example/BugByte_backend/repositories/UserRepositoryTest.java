package com.example.BugByte_backend.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserRepositoryTest {
//    @Mock
//    private JdbcTemplate jdbcTemplate;
//
//    @Mock
//    private PasswordEncoder passwordEncoder;
//
//    @InjectMocks
//    private UserRepositoryImp userRepository;
//
//    @Mock
//    private KeyHolder keyHolder;
//
//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    public void testInsertUser() {
//        // Given
//        String username = "john_doe";
//        String email = "john@example.com";
//        String password = "securePassword";
//        String encodedPassword = "encodedPassword";
//
//        // Mock password encoding
//        when(passwordEncoder.encode(password)).thenReturn(encodedPassword);
//
//        // Mock SQL execution
//        when(jdbcTemplate.update(any(), eq(username), eq(email), eq(encodedPassword))).thenReturn(1);
//
//        // When
//        int rows = userRepository.insertUser(username, email, password);
//
////        // Then
//        assertNotNull(rows);
//        assertEquals(1, rows);  // Check if the returned ID is as expected
//        verify(passwordEncoder).encode(password);  // Verify that the password was encoded
//        verify(jdbcTemplate).update(any(), eq(username), eq(email), eq(encodedPassword));  // Verify the insert operation
//    }
}
