package com.example.BugByte_backend.services;

import com.example.BugByte_backend.models.User;
import com.example.BugByte_backend.repositories.CommunityRepository;
import com.example.BugByte_backend.repositories.ModeratorRepository;
import com.example.BugByte_backend.repositories.UserRepositoryImp;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.BugByte_backend.repositories.ModeratorRepository;
import org.springframework.stereotype.Service;

@Service
public class ModeratorService {

    @Autowired
    ModeratorRepository modRepo;

    @Autowired
    UserRepositoryImp userRepositoryImp;

    public boolean setModerator(String userName, Long communityId) {
        try {
           Long userId = userRepositoryImp.getIdByUserName(userName);
            return modRepo.makeModerator(userId, communityId);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean removeModerator(String userName, Long communityId) {
        try {
            Long userId = userRepositoryImp.getIdByUserName(userName);
            return modRepo.removeModerator(userId, communityId);
        } catch (Exception e) {
            return false;
        }
    }
    public boolean isModerator(Long userId, Long communityId) {
        try {
            return modRepo.isModerator(userId, communityId);
        } catch (Exception e) {
            return false;
        }
    }
    public boolean isModeratorByName(String userName, Long communityId) {
        try {
            return modRepo.isModeratorByName(userName, communityId);
        } catch (Exception e) {
            return false;
        }
    }
}
