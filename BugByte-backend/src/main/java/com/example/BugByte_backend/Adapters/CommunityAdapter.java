package com.example.BugByte_backend.Adapters;

import com.example.BugByte_backend.models.Community;
import com.example.BugByte_backend.models.Reply;
import com.example.BugByte_backend.models.User;
import com.google.gson.Gson;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CommunityAdapter {

        public Map<String, Object> toMap(Community community) {
            if (community == null) {
                throw new NullPointerException("null community");
            }
            return Map.of(
                    "id", community.getId(),
                    "name", community.getName(),
                    "description", community.getDescription(),
                    "creationDate", community.getCreationDate());
        }

    public Community fromMap(Map<String, Object> map) {
        Community comm = new Community((String) map.get("name")
                ,(Long) map.get("admin_id"));
        if(map.containsKey("description")) comm.setDescription((String)map.get("description"));
        return comm;
    }
    public String toJson(Community community) {
        Gson gson = new Gson();
        return gson.toJson(community);
    }
}
