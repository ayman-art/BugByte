package com.example.BugByte_backend.services;

import com.example.BugByte_backend.repositories.ModeratorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.BugByte_backend.repositories.ModeratorRepository;
import org.springframework.stereotype.Service;

@Service
public class ModeratorService {

    @Autowired
    ModeratorRepository modRepo = new ModeratorRepository();

    public boolean setModerator(Long modratorId, Long communityId) {
        try {
            return modRepo.makeModerator(modratorId, communityId);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean removeModerator(Long modratorId, Long communityId) {
        try {
            return modRepo.makeModerator(modratorId, communityId);
        } catch (Exception e) {
            return false;
        }
    }
}
