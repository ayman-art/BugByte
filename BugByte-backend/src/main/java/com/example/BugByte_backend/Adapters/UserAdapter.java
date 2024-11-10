package com.example.BugByte_backend.Adapters;

import com.example.BugByte_backend.models.User;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class UserAdapter implements IAdapter<User> {


    @Override
    public Map<String, Object> toMap(User user) {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("user_name", user.getUsername());
        userMap.put("email", user.getEmail());
        userMap.put("password", user.getPassword());
        userMap.put("is_admin" , user.is_adminn());
        userMap.put("reputation" , user.getReputation());
        return userMap;
    }

    @Override
    public User fromMap(Map<String, Object> map) {
        String username = (String) map.get("user_name");
        String email = (String) map.get("email");
        String password = (String) map.get("password");
        User user = new User(username, email, password);
        user.setId((Long) map.get("id"));
        user.setReputation((Long) map.get("reputation"));
        return user;
    }

    @Override
    public String toJson(User user) {
        Gson gson = new Gson();
        return gson.toJson(user);
    }
}
