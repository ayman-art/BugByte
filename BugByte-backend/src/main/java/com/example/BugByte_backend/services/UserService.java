package com.example.BugByte_backend.services;

import com.example.BugByte_backend.models.User;
import com.example.BugByte_backend.repositories.UserRepository;
import com.example.BugByte_backend.repositories.UserRepositoryImp;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Scope("singleton")
public class UserService {
    // setting up user pool initial and final capacity ( Cache size )
    private final int poolCapacity = 1000;

    @Autowired
    private UserRepositoryImp userRepository;
    // The user pool map for caching users
    /*
    The reason behind using a linked hash map is that it maintains order of access provided in constructor
    This enables us to order it by access in the Least recently used manner
    I Override the remove eldest entry function to trigger removal of the eldest element whenever the size exceeds
        the provided pool capacity
     */
    private final LinkedHashMap<Long, User> userPool = new LinkedHashMap<Long, User>(poolCapacity, 0.75f, true){
        @Override
        protected boolean removeEldestEntry(Map.Entry<Long, User> eldest) {
            return this.size() > UserService.this.poolCapacity;
        }
    };

    /*
    I made two functions to abstract caching and getting from cache functions for the rest of the service to use
    This will help avoiding dealing with null pointers
    when using getCachedUser function it is required to surround it with try catch blocks
     */
    public void cacheUser(User user){
        this.userPool.put(user.getId(), user);
    }

    public User getCachedUser(long id) throws NullPointerException {
        User user = this.userPool.get(id);
        if ( user == null ) throw new NullPointerException("User Not Cached");
        return user;
    }

    public User getProfile(String userName) throws Exception{
        try {
            User user = userRepository.findByIdentity(userName);
            if(user == null){
                throw new Exception("User doesn't Exist");
            }
            return user;
        }
        catch (Exception e){
            throw new Exception("Couldn't get the user's profile:  " + e.getMessage());
        }
    }

    public boolean followUser(long userId , String followingName) throws Exception{
        try {
            User follower = userRepository.findById(userId);
            User following = userRepository.findByIdentity(followingName);
            if(follower == null){
                throw new Exception("follower doesn't Exist");
            }
            else if (following == null){
                throw new Exception("following doesn't Exist");
            }
            else if (userRepository.isFollowing(userId , following.getId())){
                throw new Exception("User is Already following this user");
            }
            return userRepository.follow(userId , following.getId());
        }
        catch (Exception e){
            throw new Exception("Error occurred while following user:  " + e.getMessage());
        }
    }
    public boolean unfollowUser(long userId , String followingName) throws Exception{
        try {
            User follower = userRepository.findById(userId);
            User following = userRepository.findByIdentity(followingName);
            if(follower == null){
                throw new Exception("follower doesn't Exist");
            }
            else if (following == null){
                throw new Exception("following doesn't Exist");
            }
            else if (!userRepository.isFollowing(userId , following.getId())){
                throw new Exception("User isn't following this user");
            }
            return userRepository.unfollow(userId , following.getId());
        }
        catch (Exception e){
            throw new Exception("Error occurred while unfollowing user:  " + e.getMessage());
        }
    }

    public List<User> getFollowings(String userName) throws Exception{
        try {
            User user = userRepository.findByIdentity(userName);
            if(user == null){
                throw new Exception("User doesn't Exist");
            }
            return userRepository.getFollowings(user.getId());
        }
        catch (Exception e) {
            throw new Exception("Couldn't get the following users:  " + e.getMessage());
        }
    }

    public List<User> getFollowers(String userName) throws Exception{
        try {
            User user = userRepository.findByIdentity(userName);
            if(user == null){
                throw new Exception("User doesn't Exist");
            }
            return userRepository.getFollowers(user.getId());
        }
        catch (Exception e) {
            throw new Exception("Couldn't get the followers:  " + e.getMessage());
        }
    }

    public boolean makeAdmin(long adminId , String userName) throws Exception{
        try {
            User admin = userRepository.findById(adminId);
            User user = userRepository.findByIdentity(userName);
            if(user == null){
                throw new Exception("User doesn't exist");
            }
            if (admin == null){
                throw new Exception("Admin doesn't exist");
            }
            if (!admin.get_is_admin()){
                throw new Exception("The user does not have the authority to assign admins");
            }
            return userRepository.makeUserAdmin(user.getId());
        }
        catch (Exception e) {
            throw new Exception("Couldn't make this user admin:  " + e.getMessage());
        }
    }
}
