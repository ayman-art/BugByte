package com.example.BugByte_backend.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import org.springframework.jdbc.core.JdbcTemplate;

public class ModeratorRepositoryTest {

    private static final String SQL_INSERT_MODERATOR = """
                INSERT INTO moderators
                    (id, community_id)
                VALUES
                    (?, ?);
            """;

    private static final String SQL_DELETE_MODERATOR = """
                DELETE FROM moderators
                WHERE id = ? AND community_id = ?;
            """;


    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private ModeratorRepository moderatorRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void insertModerator_successfully() {

        when(jdbcTemplate.update(eq(SQL_INSERT_MODERATOR), eq(1L), eq(1L)))
                .thenReturn(1);

        boolean result = moderatorRepository.makeModerator(1L, 1L);

        assertTrue(result);

    }

    @Test
    public void insertModerator_shouldFail_nullMemberId() {

        when(jdbcTemplate.update(eq(SQL_INSERT_MODERATOR), eq(null), eq(1L)))
                .thenReturn(1);


        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            moderatorRepository.makeModerator(null, 1L);
        });

    }

    @Test
    public void insertModerator_shouldFail_nullCommunityId() {

        when(jdbcTemplate.update(eq(SQL_INSERT_MODERATOR), eq(null), eq(1L)))
                .thenReturn(1);


        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            moderatorRepository.makeModerator(1L, null);
        });

    }


    @Test
    public void removeModerator_successfully() {

        when(jdbcTemplate.update(eq(SQL_DELETE_MODERATOR), eq(2L), eq(2L)))
                .thenReturn(1);


        boolean result = moderatorRepository.removeModerator(2L, 2L);
        assertTrue(result);
    }

    @Test
    public void removeModerator_shouldFail_nullMemberId() {

        when(jdbcTemplate.update(eq(SQL_DELETE_MODERATOR), eq(null), eq(1L)))
                .thenReturn(1);


        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            moderatorRepository.removeModerator(null, 1L);
        });

    }

    @Test
    public void removeModerator_shouldFail_nullCommunityId() {

        when(jdbcTemplate.update(eq(SQL_DELETE_MODERATOR), eq(1L), eq(null)))
                .thenReturn(1);


        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            moderatorRepository.makeModerator(1L, null);
        });

    }

}
