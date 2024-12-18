package com.example.BugByte_backend.services;

import com.example.BugByte_backend.models.Community;
import com.example.BugByte_backend.models.User;
import com.example.BugByte_backend.repositories.CommunityRepository;
import com.example.BugByte_backend.repositories.TagsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Scope("singleton")
public class CommunityService {

    private final int poolCapacity = 100;

    @Autowired
    private CommunityRepository communityRepository;

    @Autowired
    private SearchingFilteringCommunityService searchingFilteringCommunityService;

    @Autowired
    private TagsRepository tagsRepository;

    private final LinkedHashMap<String, Community> communityPool =
            new LinkedHashMap<>(poolCapacity, 0.75f, true) {
                protected boolean removeEldestEntry(Map.Entry<String, Community> eldest) {
                    return this.size() > CommunityService.this.poolCapacity;
                }
            };

    private void cacheCommunity(Community community) {
        this.communityPool.put(community.getName(), community);
    }

    private Community getCachedCommunity(String communityName) {
        Community community = this.communityPool.get(communityName);
        if (community == null) {
            throw new IllegalArgumentException("Community not cached: " + communityName);
        }
        return community;
    }

    private boolean persistCommunity(Community community) {
        try {
            return communityRepository.updateCommunityNameAndDescription(community);
        } catch (Exception e) {
            System.out.println("Failed to persist community: " + e.getMessage());
            return false;
        }
    }

    public Community getCommunity(String name) {
        try {
            return getCachedCommunity(name);
        } catch (IllegalArgumentException e) {
            System.out.println("Cache miss for community: " + name);
            return fetchCommunityFromDatabase(name);
        }
    }

    private Community fetchCommunityFromDatabase(String name) {
        try {
            Community community = communityRepository.findCommunityByName(name);
            if (community != null) {
                cacheCommunity(community);
                return community;
            } else {
                throw new IllegalArgumentException("Community not found in database: " + name);
            }
        } catch (Exception e) {
            System.out.println("Error fetching community from database: " + e.getMessage());
            throw e;
        }
    }

    public Long createCommunity(Community inCommunity) {
        try {
            Long communityId = communityRepository.insertCommunity(inCommunity.getName(), inCommunity.getAdminId());
            if(!(inCommunity.getDescription().equals("") || inCommunity.getDescription()==null))
                this.updateCommunityDescription(communityId,inCommunity.getDescription());
            cacheCommunity(inCommunity);

            inCommunity.setId(communityId);
            if (!inCommunity.getTags().isEmpty())
                tagsRepository.bulkAddTagsToCommunity(communityId, inCommunity.getTags());
            
            searchingFilteringCommunityService.saveCommunity(inCommunity);

            return communityId;
        } catch (Exception e) {
            System.out.println("Error creating community: " + e.getMessage());
            throw e;
        }
    }

    public boolean deleteCommunity(Long communityId) {
        try {
            String communityNameToRemove = removeFromCache(communityId);
            if (communityNameToRemove != null) {
                System.out.println("Community removed from cache: " + communityNameToRemove);
            }

            boolean res = communityRepository.deleteCommunityById(communityId);
            if (res) {
                Community community = communityRepository.findCommunityById(communityId);
                searchingFilteringCommunityService.deleteCommunity(community);
            }

            return res;
        } catch (Exception e) {
            System.out.println("Error deleting community: " + e.getMessage());
            return false;
        }
    }

    private String removeFromCache(Long communityId) {
        for (Map.Entry<String, Community> entry : communityPool.entrySet()) {
            if (entry.getValue().getId().equals(communityId)) {
                communityPool.remove(entry.getKey());
                return entry.getKey();
            }
        }
        return null;
    }

    public boolean updateCommunity(Community updatedCommunity) {
        if (updatedCommunity == null) {
            throw new IllegalArgumentException("updatedCommunity must not be null.");
        }
        try {
            cacheCommunity(updatedCommunity);
            persistCommunity(updatedCommunity);
            searchingFilteringCommunityService.saveCommunity(updatedCommunity);
            return true;
        } catch (Exception e) {
            System.out.println("Error updating community: " + e.getMessage());
            return false;
        }
    }

    public List<Community> getUserCommunities(Long userId) {
        try {
            List<Community> communities = communityRepository.getUserCommunities(userId);
            for (Community community : communities)
                community.setTags(tagsRepository.findTagsByCommunity(community.getId()));
            return communities;
        } catch (Exception e) {
            throw e;
        }
    }

    public List<User> getCommunityUsers(Long communityId) {
        try {
            return communityRepository.getCommunityMembers(communityId);
        } catch (Exception e) {
            throw e;
        }
    }
    public List<Community> getAllCommunities(int pageSize ,int pageNumber ) {
        try {
            List<Community> communities = communityRepository.findAllCommunities(pageSize,pageNumber);
            for (Community community : communities)
                community.setTags(tagsRepository.findTagsByCommunity(community.getId()));
            return communities;
        } catch (Exception e) {
            throw e;
        }
    }

    public List<String> getCommunityMembersNames(Long communityId) {
        try {
            return communityRepository.getCommunityMembersNames(communityId);
        } catch (Exception e) {
            throw e;
        }
    }

    public List<String> getUserCommunitiesNames(Long userId) {
        try {
            return communityRepository.getUserCommunitiesNames(userId);
        } catch (Exception e) {
            throw e;
        }
    }


    public Community getCommunityById(Long communityId) {
        try {
            System.out.println(communityId);
            Community community = communityRepository.findCommunityById(communityId);

            community.setTags(tagsRepository.findTagsByCommunity(communityId));
            return community;
        } catch (IllegalArgumentException e) {
            throw  e;
        }
    }

    public boolean updateCommunityDescription(Long id, String description) {
        try {
            return communityRepository.updateCommunityDescription(id,description);
        } catch (IllegalArgumentException e) {
            throw  e;
        }
    }
    public boolean deleteMember(Long communityId , String Username)
    {
        return  communityRepository.deleteMemberByUsername(communityId,Username);
    }
    public boolean joinCommunity(Long communityId , Long userId)
    {
        return communityRepository.insertMember(userId,communityId);
    }
    public List<User> getCommunityAdmins(Long communityId)
    {
        try {
        return communityRepository.findModeratorsByCommunityId(communityId);
        }
        catch (Exception e)
        {
           throw (e);
        }
    }
    public  boolean leaveCommunity(String communityName ,Long memberId)
    {
        return communityRepository.leaveCommunity(communityName,memberId);
    }
}
