package com.example.BugByte_backend.Adapters;

import com.example.BugByte_backend.models.User;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class UserAdapter implements IAdapter<User> {


    @Override
    public Map<String, Object> toMap(User user) {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("user_name", user.get_user_name());
        userMap.put("email", user.getEmail());
        userMap.put("password", user.getPassword());
        userMap.put("is_admin" , user.get_is_admin());
        userMap.put("reputation" , user.getReputation());
        userMap.put("id" , user.getId());
        return userMap;
    }

    @Override
    public User fromMap(Map<String, Object> map) {
        String username = (String) map.get("user_name");
        String email = (String) map.get("email");
        String password = (String) map.get("password");
        long id = (long) map.get("id");
        boolean is_admin = (boolean) map.get("is_admin");
        long reputation = (long) map.get("reputation");
        User user = new User(id , username, email, password, reputation, is_admin);
        return user;
    }

    @Override
    public String toJson(User user) {
        Gson gson = new Gson();
        return gson.toJson(user);
    }
}
