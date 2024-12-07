package com.example.BugByte_backend.services;

import com.example.BugByte_backend.models.Community;
import com.example.BugByte_backend.repositories.CommunityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
@Scope("singleton")
public class CommunityService {

    private final int poolCapacity = 100;

    private static class CachedCommunity {
        Community community;
        boolean dirty;

        CachedCommunity(Community community) {
            this.community = community;
            this.dirty = false;
        }
    }

    private final LinkedHashMap<String, CachedCommunity> communityPool =
            new LinkedHashMap<>(poolCapacity, 0.75f, true) {
                protected boolean removeEldestEntry(Map.Entry<String, CachedCommunity> eldest) {
                    if (this.size() > poolCapacity) {
                        if (eldest.getValue().dirty) {
                            persistCommunity(eldest.getValue().community);
                        }
                        return true;
                    }
                    return false;
                }
            };

    @Autowired
    private CommunityRepository communityRepository;

    private void cacheCommunity(Community community) {
        this.communityPool.put(community.getName(), new CachedCommunity(community));
    }

    private Community getCachedCommunity(String communityName) {
        CachedCommunity cached = this.communityPool.get(communityName);
        if (cached == null) {
            throw new IllegalArgumentException("Community not cached: " + communityName);
        }
        return cached.community;
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

    public Long createCommunity(String name, Long adminId) {
        try {
            Long communityId = communityRepository.insertCommunity(name, adminId);
            Community community = communityRepository.findCommunityByName(name);
            cacheCommunity(community);
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
            return communityRepository.deleteCommunityById(communityId);
        } catch (Exception e) {
            System.out.println("Error deleting community: " + e.getMessage());
            return false;
        }
    }

    private String removeFromCache(Long communityId) {
        for (Map.Entry<String, CachedCommunity> entry : communityPool.entrySet()) {
            if (entry.getValue().community.getId().equals(communityId)) {
                communityPool.remove(entry.getKey());
                return entry.getKey();
            }
        }
        return null;
    }

    public boolean updateCommunity(Community existingCommunity, Community updatedCommunity) {
        if (existingCommunity == null || updatedCommunity == null) {
            throw new IllegalArgumentException("Community and updatedCommunity must not be null.");
        }

        try {
            CachedCommunity cachedCommunity = communityPool.get(existingCommunity.getName());
            if (cachedCommunity != null) {
                updateCacheCommunity(cachedCommunity, updatedCommunity);
            } else {
                if (!persistCommunity(updatedCommunity)) {
                    return false;
                }
            }
            return persistCommunity(updatedCommunity);
        } catch (Exception e) {
            System.out.println("Error updating community: " + e.getMessage());
            return false;
        }
    }

    private void updateCacheCommunity(CachedCommunity cachedCommunity, Community updatedCommunity) {
        cachedCommunity.community.setName(updatedCommunity.getName());
        cachedCommunity.community.setDescription(updatedCommunity.getDescription());
        cachedCommunity.dirty = true;

        if (!cachedCommunity.community.getName().equals(updatedCommunity.getName())) {
            communityPool.remove(cachedCommunity.community.getName());
            communityPool.put(updatedCommunity.getName(), cachedCommunity);
        }
    }
}
