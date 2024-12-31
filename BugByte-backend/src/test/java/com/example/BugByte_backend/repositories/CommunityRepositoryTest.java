package com.example.BugByte_backend.repositories;
import com.example.BugByte_backend.models.Community;
import com.example.BugByte_backend.models.CommunityMember;
import com.example.BugByte_backend.models.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import java.util.Date;
import java.util.List;

import static graphql.Assert.assertFalse;
import static graphql.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CommunityRepositoryTest {
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


    private User admin;
    private User member1;
    private User member2;
    private User member3;
    private CommunityMember comMem1;
    private CommunityMember comMem2;
    private CommunityMember comMem3;
    private CommunityMember comMem4;
    Community comm ;
    Community comm2 ;
    Community comm3 ;

    @Mock
    private JdbcTemplate jdbcTemplate;
    @InjectMocks
    private CommunityRepository communityRepository;
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
         admin = User.builder()
                 .id(1L)
                 .userName("admin")
                 .email("admin@test.com")
                 .password("password")
                 .reputation(100L)
                 .isAdmin(true)
                 .build();
        member1 = User.builder()
                .id(2L)
                .userName("member1")
                .email("member1@test.com")
                .password("password")
                .reputation(100L)
                .isAdmin(false)
                .build();
        member2 = User.builder()
                .id(3L)
                .userName("member2")
                .email("member2@test.com")
                .password("password")
                .reputation(100L)
                .isAdmin(false)
                .build();
        member3 = User.builder()
                .id(4L)
                .userName("member3")
                .email("member3@test.com")
                .password("password")
                .reputation(100L)
                .isAdmin(false)
                .build();
        comm = Community.builder()
                .name("testComm")
                .id(12L)
                .adminId(1L)
                .build();

        comm2 = Community.builder()
                .name("test2Comm")
                .id(11L)
                .adminId(2L)
                .build();

        comm3 = Community.builder()
                .name("testComm3")
                .id(13L)
                .adminId(3L)
                .build();

         comMem1 = new CommunityMember(comm.getId(),admin.getId());
         comMem2= new CommunityMember(comm.getId(),member1.getId());
         comMem3 =new CommunityMember(comm.getId(),member2.getId());
         comMem4 =new CommunityMember(comm2.getId(),member1.getId());
    }

    @Test
    public void testInsertCommunity_Success() {
        String name = "habob";
        Long adminId = 1L;
        when(jdbcTemplate.update(eq(SQL_INSERT_COMMUNITY), eq(name), eq(adminId), any(Date.class)))
                .thenReturn(1);
        when(jdbcTemplate.update(eq(SQL_INSERT_MEMBER), eq(1L),eq(15L),any(Date.class)))
                .thenReturn(1);

        when(jdbcTemplate.queryForObject(eq(SQL_FIND_ID_BY_NAME), any(Object[].class), eq(Long.class)))
                .thenReturn(15L);

        Long result = communityRepository.insertCommunity(name, adminId);

        assertEquals(Long.valueOf(15L), result);
    }

    @Test
    public void testInsertCommunity_Failure_DuplicateName() {
        String name= "comname";
        Long adminId = 1L;
        Date creationDate =new Date();
        when(jdbcTemplate.update(eq(SQL_INSERT_COMMUNITY), eq(name), eq(adminId), eq(creationDate))).thenReturn(0);
        assertThrows(RuntimeException.class, () -> communityRepository.insertCommunity(name, adminId));
    }
    @Test
    public void testInsertCommunity_nullName() {
        String name= null;
        Long adminId = 1L;
        assertThrows(NullPointerException.class, () -> communityRepository.insertCommunity(name, adminId));
    }
    @Test
    public void testInsertCommunity_nullAdminId() {
        String name= "jj";
        Long adminId = null;
        assertThrows(NullPointerException.class, () -> communityRepository.insertCommunity(name, adminId));
    }

    @Test
    public void testInsertMember_Success() {
        Long memberId = 1L;
        Long communityId = 2L;
        when(jdbcTemplate.update(eq(SQL_INSERT_MEMBER), eq(memberId), eq(communityId), any(Date.class))).thenReturn(1);
        boolean result = communityRepository.insertMember(memberId, communityId);
        assertTrue(result);
        verify(jdbcTemplate).update(eq(SQL_INSERT_MEMBER), eq(memberId), eq(communityId), any(Date.class));
    }
    @Test
    public void testInsertMember_Failure_NullMemberId() {
        Long memberId = null;
        Long communityId = 2L;
        assertThrows(NullPointerException.class, () -> communityRepository.insertMember(memberId, communityId));
    }
    @Test
    public void testInsertMember_Failure_NullCommId() {
        Long memberId = 1L;
        Long communityId = null;
        assertThrows(NullPointerException.class, () -> communityRepository.insertMember(memberId, communityId));
    }
    @Test
    public void testNumberOfCommunityMembers_Success()
    {
        Long communityId = 1L;
        when(jdbcTemplate.queryForObject(SQL_COUNT_MEMBERS_IN_COMMUNITY, new Object[]{communityId}, Long.class))
                .thenReturn(3L);
        Long result =  communityRepository.numberOfCommunityMembers(communityId);
        assertEquals(result,3L);
    }
    @Test
    public void testNumberOfCommunityMembers_Failure_NullId()
    {
        Long communityId = null;
        assertThrows(NullPointerException.class, () -> communityRepository.numberOfCommunityMembers(communityId));
    }
    @Test
    public void testNumberOfCommunityMembers_Failure_noMembers()
    {
        Long communityId = 13L;
        assertThrows(RuntimeException.class, () -> communityRepository.numberOfCommunityMembers(communityId));
    }

    @Test
    public void testNumberOfMemberCommunities_Success()
    {
        Long memberId = 2L;
        when(jdbcTemplate.queryForObject(SQL_COUNT_USER_COMMUNITIES, new Object[]{memberId}, Long.class))
                .thenReturn(2L);
        Long result =  communityRepository.numberOfUserCommunities(memberId);
        assertEquals(result,2L);
    }

    @Test
    public void testNumberOfMemberCommunities_Failure_NullId()
    {
        Long userId = null;
        assertThrows(NullPointerException.class, () -> communityRepository.numberOfUserCommunities(userId));
    }
    @Test
    public void testNumberOfMemberCommunities_Failure_noCommunities()
    {
        Long userId =4L ;
        assertThrows(RuntimeException.class, () -> communityRepository.numberOfUserCommunities(userId));
    }

    @Test
    public void findCommunityIdByName_Success()
    {
        String name = "testComm";
        when(jdbcTemplate.queryForObject(SQL_FIND_ID_BY_NAME, new Object[]{name}, Long.class))
                .thenReturn(1L);
        Long result =  communityRepository.findIdByName(name);
        assertEquals(result,1L);
    }

    @Test
    public void findCommunityIdByName_failure_NullName()
    {
        String name = null;
        assertThrows(NullPointerException.class, () -> communityRepository.findIdByName(name));
    }
    @Test
    public void findCommunityIdByName_failure_WrongName()
    {
        String name = "myComm";
        assertThrows(RuntimeException.class, () -> communityRepository.findIdByName(name));
    }

    @Test
    public void testFindCommunityById_Failure_NullId() {
        Long communityId = null;
        assertThrows(NullPointerException.class, () -> communityRepository.findCommunityById(communityId));
    }

    @Test
    public void testFindCommunityById_Failure_NoCommunity() {
        Long communityId = 999L;
        when(jdbcTemplate.queryForObject(eq(SQL_FIND_BY_ID), eq(new Object[]{communityId}), eq(Community.class)))
                .thenReturn(null);
        assertThrows(RuntimeException.class, () -> communityRepository.findCommunityById(communityId));
    }
    @Test
    public void testFindCommunityByName_Success() {
        String name = "testComm";
        when(jdbcTemplate.queryForObject(eq(SQL_FIND_BY_NAME), eq(new Object[]{name}), eq(Community.class)))
                .thenReturn(comm);
        Community result = communityRepository.findCommunityByName(name);
        assertEquals(comm, result);
    }

    @Test
    public void testFindCommunityByName_Failure_NullName() {
        String name = null;
        assertThrows(NullPointerException.class, () -> communityRepository.findCommunityByName(name));
    }

    @Test
    public void testFindCommunityByName_Failure_NoCommunity() {
        String name = "nonExistentComm";
        when(jdbcTemplate.queryForObject(eq(SQL_FIND_BY_NAME), eq(new Object[]{name}), eq(Community.class)))
                .thenReturn(null);
        assertThrows(RuntimeException.class, () -> communityRepository.findCommunityByName(name));
    }

//    @Test
//    public void testFindAllCommunities_Success() {
//        List<Community> mockCommunities = List.of(
//                this.comm, this.comm2, this.comm3
//        );
//        when(jdbcTemplate.query(eq(SQL_FIND_ALL_COMMUNITIES), any(RowMapper.class)))
//                .thenAnswer(invocation -> mockCommunities);
//        List<Community> result = communityRepository.findAllCommunities();
//        assertEquals(mockCommunities.size(), result.size());
//        assertEquals(mockCommunities, result);
//    }

    @Test
    public void testFindCommunityMembers_Ids_Success() {
        Long communityId = 1L;
        List<Long> mockMemberIds = List.of(1L, 2L, 3L);

        when(jdbcTemplate.queryForList(eq(SQL_FIND_COMMUNITY_MEMBERS_ID), eq(new Object[]{communityId}), eq(Long.class)))
                .thenReturn(mockMemberIds);

        List<Long> result = communityRepository.findCommunityMembersIds(communityId);
        assertEquals(mockMemberIds, result);
    }

    @Test
    public void testFindCommunityMembers_Ids_Failure_NullId() {
        Long communityId = null;
        assertThrows(NullPointerException.class, () -> communityRepository.findCommunityMembersIds(communityId));
    }

    @Test
    public void testFindUserCommunities_Ids_Success() {
        Long userId = 1L;
        List<Long> mockCommunityIds = List.of(1L, 2L, 3L);

        when(jdbcTemplate.queryForList(eq(SQL_FIND_USER_COMMUNITIES_ID), eq(new Object[]{userId}), eq(Long.class)))
                .thenReturn(mockCommunityIds);

        List<Long> result = communityRepository.findUserCommunitiesIds(userId);
        assertEquals(mockCommunityIds, result);
    }

    @Test
    public void testFindUserCommunities_Ids_Failure_NullId() {
        Long userId = null;
        assertThrows(NullPointerException.class, () -> communityRepository.findUserCommunitiesIds(userId));
    }
    @Test
    public void testUpdateCommunityDescription_Success() {
        Long communityId = 1L;
        String description = "Updated description";

        when(jdbcTemplate.update(eq(SQL_UPDATE_DESCRIPTION), eq(description), eq(communityId)))
                .thenReturn(1);

        boolean result = communityRepository.updateCommunityDescription(communityId, description);
        assertTrue(result);
    }

    @Test
    public void testUpdateCommunityDescription_Failure_NullDescription() {
        Long communityId = 1L;
        String description = null;
        assertThrows(NullPointerException.class, () -> communityRepository.updateCommunityDescription(communityId, description));
    }
    @Test
    public void testUpdateCommunityName_Success() {
        Long communityId = 12L;
        String newName = "UpdatedName";

        when(jdbcTemplate.update(eq(SQL_UPDATE_COMMUNITY_NAME), eq(newName), eq(communityId)))
                .thenReturn(1);

        boolean result = communityRepository.updateCommunityName(communityId, newName);
        assertTrue(result);
    }

    @Test
    public void testUpdateCommunityName_Failure_NullName() {
        Long communityId = 12L;
        String newName = null;
        assertThrows(NullPointerException.class, () -> communityRepository.updateCommunityName(communityId, newName));
    }
//    @Test
//    public void testDeleteCommunityById_Success() {
//        Long communityId = 12L;
//
//        when(jdbcTemplate.update(eq(SQL_DELETE_COMMUNITY_BY_ID), eq(communityId)))
//                .thenReturn(1);
//
//        boolean result = communityRepository.deleteCommunityById(communityId);
//
//        assertTrue(result);
//    }
//
//    @Test
//    public void testDeleteCommunityById_Failure_NullId() {
//        Long communityId = null;
//        assertThrows(NullPointerException.class, () -> communityRepository.deleteCommunityById(communityId));
//    }
    @Test
    public void testDeleteMemberById_Success() {
        Long memberId = 1L;
        Long communityId = 12L;

        when(jdbcTemplate.update(eq(SQL_DELETE_MEMBER_BY_ID), eq(memberId), eq(communityId)))
                .thenReturn(1);

        boolean result = communityRepository.deleteMemberById(memberId, communityId);
        assertTrue(result);
    }

    @Test
    public void testDeleteMemberById_Failure_NullMemberId() {
        Long memberId = null;
        Long communityId = 12L;
        assertThrows(NullPointerException.class, () -> communityRepository.deleteMemberById(memberId, communityId));
    }
    @Test
    public void testGetCommunityMembers_Success() {
        Long communityId = 12L;
        List<User> mockUsers = List.of(member1, member2);

        when(jdbcTemplate.query(eq(SQL_FIND_USERS_BY_COMMUNITY_ID), eq(new Object[]{communityId}), any(RowMapper.class)))
                .thenReturn(mockUsers);
        List<User> result = communityRepository.getCommunityMembers(communityId);
        assertEquals(mockUsers.size(), result.size());
        assertEquals(mockUsers, result);
    }

    @Test
    public void testGetCommunityMembers_Failure_NoMembersFound() {
        Long communityId = 14L;

        when(jdbcTemplate.query(eq(SQL_FIND_USERS_BY_COMMUNITY_ID), eq(new Object[]{communityId}), any(RowMapper.class)))
                .thenReturn(List.of());
        RuntimeException exception = assertThrows(RuntimeException.class, () -> communityRepository.getCommunityMembers(communityId));
        assertEquals("No users found in this community.", exception.getMessage());
    }

    @Test
    public void testGetCommunityMembers_Failure_NullCommunityId() {
        Long communityId = null;
        assertThrows(NullPointerException.class, () -> communityRepository.getCommunityMembers(communityId));
    }
    @Test
    public void testGetUserCommunities_Success() {
        Long userId = 2L;
        List<Community> mockCommunities = List.of(comm, comm2);
        when(jdbcTemplate.query(eq(SQL_FIND_COMMUNITIES_BY_USER_ID), eq(new Object[]{userId}), any(RowMapper.class)))
                .thenReturn(mockCommunities);
        List<Community> result = communityRepository.getUserCommunities(userId);
        assertEquals(mockCommunities.size(), result.size());
        assertEquals(mockCommunities, result);
    }


    @Test
    public void testGetUserCommunities_Failure_NullUserId() {
        Long userId = null;
        assertThrows(NullPointerException.class, () -> communityRepository.getUserCommunities(userId));
    }

    @Test
    public void testDeleteCommunityMembers_Success() {
        Long communityId = 12L;

        when(jdbcTemplate.update(eq(SQL_DELETE_COMMUNITY_MEMBERS), eq(communityId)))
                .thenReturn(1);

        boolean result = communityRepository.deleteCommunityMembers(communityId);
        assertTrue(result);
        verify(jdbcTemplate).update(eq(SQL_DELETE_COMMUNITY_MEMBERS), eq(communityId));
    }

    @Test
    public void testDeleteCommunityMembers_Failure_NoRowsAffected() {
        Long communityId = 12L;

        when(jdbcTemplate.update(eq(SQL_DELETE_COMMUNITY_MEMBERS), eq(communityId)))
                .thenReturn(0);

        boolean result = communityRepository.deleteCommunityMembers(communityId);
        assertFalse(result);
    }

    @Test
    public void testDeleteCommunityMembers_Failure_NullCommunityId() {
        Long communityId = null;

        NullPointerException exception = assertThrows(NullPointerException.class,
                () -> communityRepository.deleteCommunityMembers(communityId));

        assertEquals("communityId or memberId is null", exception.getMessage());
    }
    @Test
    public void testGetUserCommunitiesNames_Success() {
        Long userId = 2L;
        List<String> mockCommunities = List.of("testComm", "test2Comm");
        when(jdbcTemplate.queryForList(eq(SQL_FIND_COMMUNITIES_NAMES_BY_USER_ID), eq(new Object[]{userId}), eq(String.class)))
                .thenReturn(mockCommunities);
        List<String> result = communityRepository.getUserCommunitiesNames(userId);
        assertEquals(mockCommunities.size(), result.size());
        assertEquals(mockCommunities, result);
    }
    @Test
    public void testGetCommunityMembersNames_Success() {
        Long communityId = 12L;
        List<String> mockUsers = List.of("admin", "member1","member2");
        when(jdbcTemplate.queryForList(eq(SQL_FIND_MEMBERS_NAMES_BY_COMMUNITY_ID), eq(new Object[]{communityId}), eq(String.class)))
                .thenReturn(mockUsers);
        List<String> result = communityRepository.getCommunityMembersNames(communityId);
        assertEquals(mockUsers.size(), result.size());
        assertEquals(mockUsers, result);
    }

    @Test
    public void testUpdateCommunityNameAndDescription_success() {
        Community community = new Community("New Name","New Description" ,1L );
        when(jdbcTemplate.update(SQL_UPDATE_COMMUNITY_NAME_AND_DESCRIPTION, community.getName(), community.getDescription(), community.getId())).thenReturn(1);
        boolean result = communityRepository.updateCommunityNameAndDescription(community);
        assertTrue(result);
    }

    @Test
    public void testUpdateCommunityNameAndDescription_failure() {
        Community community = new Community("New Name","New Description" ,1L );
        when(jdbcTemplate.update(SQL_UPDATE_COMMUNITY_NAME_AND_DESCRIPTION, community.getName(), community.getDescription(), community.getId())).thenReturn(0);
        boolean result = communityRepository.updateCommunityNameAndDescription(community);
        assertFalse(result);
    }

  }
