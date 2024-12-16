package com.example.BugByte_backend.services;

import com.example.BugByte_backend.models.Community;
import com.example.BugByte_backend.repositories.SearchingFilteringCommunityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchingFilteringCommunityService {
    @Autowired
    private SearchingFilteringCommunityRepository communityRepository;

    public Community saveCommunity(Community community) {
        if (community == null)
            throw new NullPointerException("Community can't be null");
        return communityRepository.save(community);
    }

    public Page<Community> searchCommunity(String query, int page, int size) {
        if (query == null)
            throw new NullPointerException("Query can't be null");

        Pageable pageable = PageRequest.of(page, size);
        return communityRepository.findByMdContentAndTags(query, pageable);
    }

    public Page<Community> getCommunityByTags(List<String> tags, int page, int size) {
        if (tags == null || tags.isEmpty())
            throw new NullPointerException("tags can't be null or empty");

        Pageable pageable = PageRequest.of(page, size);
        return communityRepository.findByTags(tags, pageable);
    }

    public void deleteCommunity(Community community) {
        if (community == null)
            throw new NullPointerException("Community is null");

        communityRepository.delete(community);
    }
}
