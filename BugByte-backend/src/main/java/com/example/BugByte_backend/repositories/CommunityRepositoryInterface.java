package com.example.BugByte_backend.repositories;

import com.example.BugByte_backend.models.Community;

import java.util.List;

public interface CommunityRepositoryInterface {

    Long insertCommunity(String name, Long adminId);

    Boolean insertMember(Long memberId, Long communityId);

    Long numberOfCommunityMembers(Long id);

    Long numberOfUserCommunities(Long id);

    Long findIdByName(String name);

    Community findIdById(Long id);

    Community findCommunityByName(String name);

    List<Community> findAllCommunities();

    List<Long> findCommunityMembers(Long communityId);

    List<Long> findUserCommunities(Long memberId);

    boolean updateCommunityDescription(Long communityId, String description);

    boolean updateCommunityName(Long communityId, String newName);

    boolean deleteCommunityById(Long communityId);

    boolean deleteMemberById(Long memberId, Long communityId);
}
