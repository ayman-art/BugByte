package com.example.BugByte_backend.services;

import com.example.BugByte_backend.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;
@Service
public class UserService {
    // Applying singleton design pattern
    private static UserService instance = null;

    public static UserService getInstance(){
        if(instance==null) instance = new UserService();
        return instance;
    }
    // private constructor to avoid using it outside the class
    private UserService(){}

    // setting up user pool initial and final capacity ( Cache size )
    private final int poolCapacity = 1000;
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
    protected void cacheUser(User user){
        this.userPool.put(user.getId(), user);
    }
    protected User getCachedUser(long id) throws NullPointerException {
        User user = this.userPool.get(id);
        if ( user == null ) throw new NullPointerException("User Not Cached");
        return user;
    }

}
