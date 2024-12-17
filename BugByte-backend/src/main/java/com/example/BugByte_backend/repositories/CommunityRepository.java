package com.example.BugByte_backend.repositories;
import com.example.BugByte_backend.models.Community;
import com.example.BugByte_backend.models.User;
import org.apache.lucene.analysis.miscellaneous.ConcatenateGraphFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class CommunityRepository implements CommunityRepositoryInterface{
    private static final String SQL_INSERT_COMMUNITY = """
                INSERT INTO communities
                    (name, description, admin_id, creation_date)
                VALUES
                    (?, "", ?, ?);
            """;
    private static final String SQL_INSERT_MEMBER = """
                INSERT INTO community_members
                    (member_id,community_id,join_date)
                VALUES
                    (?, ?, ?);
            """;
    private static final String SQL_COUNT_MEMBERS_IN_COMMUNITY = """
                SELECT COUNT(*)
                FROM community_members
                WHERE community_id = ?;
            """;
    private static final String SQL_COUNT_USER_COMMUNITIES = """
                SELECT COUNT(*)
                FROM community_members
                WHERE member_id = ?;
            """;

    private static final String SQL_UPDATE_COMMUNITY_NAME_AND_DESCRIPTION = """
    UPDATE communities
    SET name = ?, description = ?
    WHERE community_id = ?;
""";

    private static final String SQL_FIND_BY_ID = "SELECT * FROM communities WHERE id = ?;";
    private static final String SQL_FIND_ID_BY_NAME = "SELECT id FROM communities WHERE name = ?;";
    private static final String SQL_FIND_BY_NAME = "SELECT * FROM communities WHERE name = ?;";
    private static final String SQL_FIND_COMMUNITY_MEMBERS_ID = "SELECT member_id FROM community_members WHERE community_id =?;";
    private static final String SQL_FIND_USER_COMMUNITIES_ID = "SELECT community_id FROM community_members WHERE member_id =?;";
    private static final String SQL_FIND_ALL_COMMUNITIES = "SELECT * FROM communities;";
    private static final String SQL_UPDATE_DESCRIPTION = "UPDATE communities SET description = ? WHERE id = ?;";
    private static final String SQL_UPDATE_COMMUNITY_NAME = "UPDATE communities SET name = ? WHERE id = ?;";
    private static final String SQL_DELETE_COMMUNITY_BY_ID = "DELETE FROM communities WHERE id = ?;";
    private static final String SQL_DELETE_MEMBER_BY_ID = "DELETE FROM community_members WHERE member_id = ? AND community_id=?;";

    private static final String SQL_FIND_COMMUNITIES_BY_USER_ID = """
    SELECT *
    FROM communities c
    INNER JOIN community_members cm ON c.id = cm.community_id
    WHERE cm.member_id = ?;
""";
    private static final String SQL_FIND_USERS_BY_COMMUNITY_ID = """
    SELECT *
    FROM users u
    INNER JOIN community_members cm ON u.id = cm.member_id
    WHERE cm.community_id = ?;
""";
    private static final String SQL_FIND_COMMUNITIES_NAMES_BY_USER_ID = """
    SELECT name
    FROM communities c
    INNER JOIN community_members cm ON c.id = cm.community_id
    WHERE cm.member_id = ?;
""";
    private static final String SQL_FIND_MEMBERS_NAMES_BY_COMMUNITY_ID = """
    SELECT user_name
    FROM users u
    INNER JOIN community_members cm ON u.id = cm.member_id
    WHERE cm.community_id = ?;
""";
    private static final String SQL_DELETE_COMMUNITY_MEMBERS = """
    DELETE FROM community_members
    WHERE community_id = ?;
""";

    private  static final String SQL_REMOVE_COMMUNITY_MODERATORS = """
    DELETE FROM moderators 
    WHERE community_id = ?;
""";

    private static final String SQL_FIND_MODERATORS_BY_COMMUNITY = """
    SELECT * 
    FROM users u 
    INNER JOIN moderators m ON m.id = u.id
    WHERE m.community_id = ?;
""";


    @Autowired
    private JdbcTemplate jdbcTemplate;
    private UserRepositoryImp userRepositoryImp = new UserRepositoryImp();

    @Override
    public Long insertCommunity(String name, Long adminId) {
        if (name == null || adminId == null)
            throw new NullPointerException("name or adminId is null");
        int rows = jdbcTemplate.update(SQL_INSERT_COMMUNITY, name, adminId, new Date());
        if (rows != 0) {
            Long n=findIdByName(name);
            boolean k=insertMember(adminId,n);
            return n;
        }
        throw new RuntimeException("Community with this name already exists.");
    }


    @Override
    public Boolean insertMember(Long memberId, Long communityId) {
        if (memberId == null || communityId == null)
            throw new NullPointerException("memberId or communityId is null");

        int rows = jdbcTemplate.update(SQL_INSERT_MEMBER, memberId, communityId, new Date());
        if (rows == 0)
            throw new RuntimeException("Invalid input");
        return rows==1;
    }

    @Override
    public Long numberOfCommunityMembers(Long id) {
        if(id==null)
            throw new NullPointerException("ID is Null");
        Long count = jdbcTemplate.queryForObject(SQL_COUNT_MEMBERS_IN_COMMUNITY, new Object[]{ id }, Long.class);
        if (count == null)
            throw new RuntimeException("Invalid input: COUNT query returned null for community ID: " + id);
        return count;
    }

    @Override
    public Long numberOfUserCommunities(Long id) {
        if(id==null)
            throw new NullPointerException("ID is Null");

        Long count = jdbcTemplate.queryForObject(SQL_COUNT_USER_COMMUNITIES, new Object[]{ id }, Long.class);
        if (count == null)
            throw new RuntimeException("Invalid input: COUNT query returned null for member ID: " + id);
        return count;
    }

    @Override
    public Long findIdByName(String name) {
        if(name==null)
            throw new NullPointerException("Name is Null");

        Long id =jdbcTemplate.queryForObject(SQL_FIND_ID_BY_NAME, new Object[]{name}, Long.class);
        if(id == null)
            throw new RuntimeException("No community with this name: " + name);
        return id;
    }

    @Override
    public Community findCommunityById(Long id) {
        if( id == null)
            throw new NullPointerException("id is Null");
        Community com = jdbcTemplate.queryForObject(SQL_FIND_BY_ID, communityRowMapper,id);
        if(com == null)
            throw new RuntimeException("No community with this id: " + id);
        return com;
    }

    @Override
    public Community findCommunityByName(String name) {
        if (name == null)
            throw new NullPointerException("name is Null");
        Community com = jdbcTemplate.queryForObject(SQL_FIND_BY_NAME, new Object[]{name},Community.class);
        if(com == null)
            throw new NullPointerException("No community with this name:");
        return com;
    }

    @Override
    public List<Community> findAllCommunities() {
        return jdbcTemplate.query(SQL_FIND_ALL_COMMUNITIES,
                (rs, rowNum) -> Community.builder()
                        .id(rs.getLong("id"))
                        .name(rs.getString("name"))
                        .description(rs.getString("description"))
                        .adminId(rs.getLong("admin_id"))
                        .creationDate(rs.getDate("creation_date"))
                        .build()
                );
    }

    @Override
    public List<Long> findCommunityMembersIds(Long communityId) {
        if(communityId==null)
            throw new NullPointerException("communityId is null");
        return jdbcTemplate.queryForList(SQL_FIND_COMMUNITY_MEMBERS_ID, new Object[]{communityId}, Long.class);
    }

    @Override
    public List<Long> findUserCommunitiesIds(Long memberId) {
        if(memberId==null)
            throw new NullPointerException("memberId is null");
        return jdbcTemplate.queryForList(SQL_FIND_USER_COMMUNITIES_ID, new Object[]{memberId}, Long.class);
    }

    @Override
    public boolean updateCommunityDescription(Long communityId, String description) {
        if(description==null || communityId==null)
            throw new NullPointerException("description or communityId is null");
        int rows = jdbcTemplate.update(SQL_UPDATE_DESCRIPTION, description, communityId);
        return rows == 1;
    }

    @Override
    public boolean updateCommunityName(Long communityId, String newName) {
        if(newName==null || communityId==null)
            throw new NullPointerException("newName or communityId is null");
        int rows = jdbcTemplate.update(SQL_UPDATE_COMMUNITY_NAME, newName, communityId);
        return rows == 1;
    }
//
    @Override
    public  boolean deleteCommunityById(Long communityId) {
        if(communityId==null)
            throw new NullPointerException("communityId is null");
        boolean i = deleteCommunityMembers(communityId);
        removeCommunityModerators(communityId);
        int rows = jdbcTemplate.update(SQL_DELETE_COMMUNITY_BY_ID, communityId);
        return rows == 1;
    }

    private void removeCommunityModerators(Long communityId) {
        if (communityId == null) {
            throw new IllegalArgumentException("communityId is null");
        }
        int rows = jdbcTemplate.update(SQL_REMOVE_COMMUNITY_MODERATORS, communityId);
    }

    @Override
    public boolean deleteMemberById(Long memberId, Long communityId) {
        if(memberId==null || communityId==null)
            throw new NullPointerException("memberId or communityId is null");
        int rows = jdbcTemplate.update(SQL_DELETE_MEMBER_BY_ID, memberId, communityId);
        return rows == 1;
    }

    @Override
    public List<User> getCommunityMembers(Long communityId) {
        if (communityId == null)
            throw new NullPointerException("communityId is null");

        List<User> users = jdbcTemplate.query(SQL_FIND_USERS_BY_COMMUNITY_ID,
                new Object[]{communityId}, (rs, rowNum) -> new User(
                        rs.getLong("id"),
                        rs.getString("user_name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("bio"),
                        rs.getLong("reputation"),
                        rs.getBoolean("is_admin"),
                        rs.getString("picture")
                ));

        if (users.isEmpty())
            throw new RuntimeException("No users found in this community.");

        return users;
    }

    @Override
    public List<Community> getUserCommunities(Long userId) {
        if (userId == null)
            throw new NullPointerException("userId is null");

        List<Community> communities = jdbcTemplate.query(SQL_FIND_COMMUNITIES_BY_USER_ID,
                new Object[] {userId}, (rs, rowNum) -> Community.builder()
                        .id(rs.getLong("id"))
                        .name(rs.getString("name"))
                        .description(rs.getString("description"))
                        .adminId(rs.getLong("admin_id"))
                        .creationDate(rs.getDate("creation_date"))
                        .build()
        );

        if (communities.isEmpty())
            return new ArrayList<Community>();

        return communities;
    }

    @Override
    public List<String> getCommunityMembersNames(Long communityId) {
        if(communityId==null)
            throw new NullPointerException("memberId is null");

        List<String> result =jdbcTemplate.queryForList(SQL_FIND_MEMBERS_NAMES_BY_COMMUNITY_ID, new Object[]{communityId}, String.class);
        if(result.isEmpty())
            throw new RuntimeException("No users found in this community.");
        return result;
    }

    @Override
    public List<String> getUserCommunitiesNames(Long userId) {
        if(userId==null)
            throw new NullPointerException("userId is null");

        List<String> result =jdbcTemplate.queryForList(SQL_FIND_COMMUNITIES_NAMES_BY_USER_ID, new Object[]{userId}, String.class);
        if(result.isEmpty())
            throw new RuntimeException("User is not in any community");
        return result;
    }

    @Override
    public boolean deleteCommunityMembers(Long communityId) {
        if (communityId == null)
            throw new NullPointerException("communityId or memberId is null");

        int rows = jdbcTemplate.update(SQL_DELETE_COMMUNITY_MEMBERS, communityId);
        return rows == 1;
    }
    @Override
    public boolean updateCommunityNameAndDescription(Community community) {
        if (community == null) {
            throw new IllegalArgumentException("Community object is null");
        }
        int rows = jdbcTemplate.update(SQL_UPDATE_COMMUNITY_NAME_AND_DESCRIPTION, community.getName(), community.getDescription(), community.getId());
        return rows == 1;
    }
    public boolean deleteMemberByUsername(Long communityId , String Username)
    {
        Long userId = userRepositoryImp.getIdByUserName(Username);
        return  deleteMemberById(userId , communityId);
    }

    public List<User> findModeratorsByCommunityId(Long communityId) {
        if (communityId == null) {
            throw new NullPointerException("communityId is null");
        }
        System.out.println(communityId);
        List<User> moderators = jdbcTemplate.query(SQL_FIND_MODERATORS_BY_COMMUNITY, new Long[]{communityId},new UserRowMapper());
        return moderators;
    }
    private class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getLong("id"));
            user.setUserName(rs.getString("user_name"));
            user.setEmail(rs.getString("email"));
            user.setPassword(rs.getString("password"));
            user.setBio(rs.getString("bio"));
            user.setReputation(rs.getLong("reputation"));
            user.setIsAdmin(rs.getBoolean("is_admin"));
            return user;
        }
    }

    private final RowMapper<Community> communityRowMapper = ((rs, rowNum) -> Community.builder()
            .id(rs.getLong("id"))
            .name(rs.getString("name"))
            .description(rs.getString("description"))
            .adminId(rs.getLong("admin_id"))
            .creationDate(rs.getDate("creation_date"))
            .build()
    );


}

