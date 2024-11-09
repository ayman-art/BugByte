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
        User user = new User((String) map.get("user_name"), (String) map.get("email"),
                (String) map.get("password"), (Boolean) map.get("is_admin"));
        user.setId((Long) map.get("id"));
        user.setReputation((Integer) map.get("reputation"));
        return user;
    }

    @Override
    public String toJson(User user) {
        Gson gson = new Gson();
        return gson.toJson(user);
    }
}
