package com.example.BugByte_backend.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.example.BugByte_backend.repositories.CommunityRepository.*;
@Repository
public class ModeratorRepository implements IModeratorRepository{

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


    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Boolean makeModerator(Long userId, Long communityId) {
        if (userId == null || communityId == null) {
            throw new NullPointerException("member Id or community Id is null");
        }

        int rows = jdbcTemplate.update(SQL_INSERT_MODERATOR, userId, communityId);

        if (rows == 0)
            throw new RuntimeException("Invalid input");

        return true;

    }

    public Boolean removeModerator(Long userId, Long communityId) {
        if (userId == null || communityId == null) {
            throw new NullPointerException("member Id or community Id is null");
        }

        int rows = jdbcTemplate.update(SQL_DELETE_MODERATOR, userId, communityId);

        if (rows == 0)
            throw new RuntimeException("Invalid input");

        return true;
    }

}
