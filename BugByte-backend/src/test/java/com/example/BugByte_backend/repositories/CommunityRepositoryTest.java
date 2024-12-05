package com.example.BugByte_backend.repositories;

import com.example.BugByte_backend.models.Community;
import com.example.BugByte_backend.models.CommunityMember;
import com.example.BugByte_backend.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import java.util.Date;
import java.util.List;
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
         admin = new User(1L, "admin", "admin@test.com", "password", 100L, true);
         member1 = new User(2L, "member1", "member1@test.com", "password", 100L, false);
         member2 = new User(3L, "member2", "member2@test.com", "password", 100L, false);
         member3=new User(4L, "member4", "member4@test.com", "password", 100L, false);
         comm = new Community("testComm", 1L);
         comm.setId(12L);
         comm2= new Community("test2Comm",2L);
         comm2.setId(11L);
         comm3 = new Community("testComm3", 3L);
         comm3.setId(13L);
         comMem1 = new CommunityMember(comm,admin);
         comMem2= new CommunityMember(comm,member1);
         comMem3 =new CommunityMember(comm,member2);
         comMem4 =new CommunityMember(comm2,member1);
    }

    @Test
    public void testInsertCommunity_Success() {
        String name = "testComm";
        Long adminId = 1L;
        Date creationDate = new Date();
        when(jdbcTemplate.update(eq(SQL_INSERT_COMMUNITY), eq(name), eq(adminId), eq(creationDate)))
                .thenReturn(1);
        when(jdbcTemplate.queryForObject(eq(SQL_FIND_ID_BY_NAME), eq(new Object[]{name}), eq(Long.class)))
                .thenReturn(12L);
        Long result = communityRepository.insertCommunity(name, adminId);
        assertEquals(Long.valueOf(12L), result);
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
    public void testFindCommunityById_Success() {
        Long communityId = 12L;
        when(jdbcTemplate.queryForObject(eq(SQL_FIND_BY_ID), eq(new Object[]{communityId}), eq(Community.class)))
                .thenReturn(comm);
        Community result = communityRepository.findCommunityById(communityId);
        assertEquals(comm, result);
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

    @Test
    public void testFindAllCommunities_Success() {
        List<Community> mockCommunities = List.of(
                this.comm, this.comm2, this.comm3
        );
        when(jdbcTemplate.query(eq(SQL_FIND_ALL_COMMUNITIES), any(RowMapper.class)))
                .thenAnswer(invocation -> mockCommunities);
        List<Community> result = communityRepository.findAllCommunities();
        assertEquals(mockCommunities.size(), result.size());
        assertEquals(mockCommunities, result);
    }

    @Test
    public void testFindCommunityMembers_Success() {
        Long communityId = 1L;
        List<Long> mockMemberIds = List.of(1L, 2L, 3L);

        when(jdbcTemplate.queryForList(eq(SQL_FIND_COMMUNITY_MEMBERS_ID), eq(new Object[]{communityId}), eq(Long.class)))
                .thenReturn(mockMemberIds);

        List<Long> result = communityRepository.findCommunityMembers(communityId);
        assertEquals(mockMemberIds, result);
    }

    @Test
    public void testFindCommunityMembers_Failure_NullId() {
        Long communityId = null;
        assertThrows(NullPointerException.class, () -> communityRepository.findCommunityMembers(communityId));
    }

    @Test
    public void testFindUserCommunities_Success() {
        Long userId = 1L;
        List<Long> mockCommunityIds = List.of(1L, 2L, 3L);

        when(jdbcTemplate.queryForList(eq(SQL_FIND_USER_COMMUNITIES_ID), eq(new Object[]{userId}), eq(Long.class)))
                .thenReturn(mockCommunityIds);

        List<Long> result = communityRepository.findUserCommunities(userId);
        assertEquals(mockCommunityIds, result);
    }

    @Test
    public void testFindUserCommunities_Failure_NullId() {
        Long userId = null;
        assertThrows(NullPointerException.class, () -> communityRepository.findUserCommunities(userId));
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
    @Test
    public void testDeleteCommunityById_Success() {
        Long communityId = 12L;

        when(jdbcTemplate.update(eq(SQL_DELETE_COMMUNITY_BY_ID), eq(communityId)))
                .thenReturn(1);

        boolean result = communityRepository.deleteCommunityById(communityId);

        assertTrue(result);
    }

    @Test
    public void testDeleteCommunityById_Failure_NullId() {
        Long communityId = null;
        assertThrows(NullPointerException.class, () -> communityRepository.deleteCommunityById(communityId));
    }
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

}
