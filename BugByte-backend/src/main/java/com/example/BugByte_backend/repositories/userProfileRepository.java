package com.example.BugByte_backend.repositories;

import com.example.BugByte_backend.models.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class userProfileRepository {
    public boolean followUser(long userId , long followingId){
        return true;
    }
    public boolean isFollowing(long userId , long followingId){
        return false;
    }
    public boolean unfollowUser(long userId , long followingId){
        return true;
    }
    public List<User> getFollowings(long userId){
        return null;
    }
    public List<User> getFollowers(long userId){
        return null;
    }

    public int getFollowersCount(long userId){
        return 0;
    }
    public int getFollowingsCount(long userId){
        return 0;
    }
}
