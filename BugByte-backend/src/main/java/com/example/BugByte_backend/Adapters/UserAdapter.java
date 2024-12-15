package com.example.BugByte_backend.Adapters;

import com.example.BugByte_backend.models.User;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class UserAdapter implements IAdapter<User> {

    @Override
    public Map<String, Object> toMap(User user) {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("userName", user.getUserName());
        userMap.put("email", user.getEmail());
        userMap.put("password", user.getPassword());
        userMap.put("bio", user.getBio());
        userMap.put("isAdmin", user.getIsAdmin());
        userMap.put("reputation", user.getReputation());
        userMap.put("id", user.getId());
        userMap.put("picture", user.getPicture());
        return userMap;
    }

    @Override
    public User fromMap(Map<String, Object> map) {
        String username = (String) map.get("userName");
        String email = (String) map.get("email");
        String password = (String) map.get("password");
        String bio = (String) map.get("bio");
        String picture = (String) map.get("picture");
        long id = (long) map.get("id");
        boolean is_admin = (boolean) map.get("isAdmin");
        long reputation = (long) map.get("reputation");
        return new User(id, username, email, password, bio, reputation, is_admin, picture);
    }

    @Override
    public String toJson(User user) {
        Gson gson = new Gson();
        return gson.toJson(user);
    }
}
