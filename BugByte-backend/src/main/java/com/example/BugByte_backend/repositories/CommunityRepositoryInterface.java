package com.example.BugByte_backend.repositories;

import com.example.BugByte_backend.models.Community;
import com.example.BugByte_backend.models.User;

import java.util.List;

public interface CommunityRepositoryInterface {

    Long insertCommunity(String name, Long adminId);

    Boolean insertMember(Long memberId, Long communityId);

    Long numberOfCommunityMembers(Long id);

    Long numberOfUserCommunities(Long id);

    Long findIdByName(String name);

    Community findCommunityById(Long id);

    Community findCommunityByName(String name);

    List<Community> findAllCommunities();

    List<Long> findCommunityMembersIds(Long communityId);

    List<Long> findUserCommunitiesIds(Long memberId);

    boolean updateCommunityDescription(Long communityId, String description);

    boolean updateCommunityName(Long communityId, String newName);

    boolean deleteCommunityById(Long communityId);

    boolean deleteMemberById(Long memberId, Long communityId);

    List<Community> getUserCommunities(Long userId);

    List<User> getCommunityMembers(Long communityId);

    List<String> getCommunityMembersNames(Long communityId);

    List<String> getUserCommunitiesNames(Long userId);

    boolean deleteCommunityMembers(Long communityId);

    boolean updateCommunityNameAndDescription(Community community);

}
