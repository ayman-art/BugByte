package com.example.BugByte_backend.Adapters;

import com.example.BugByte_backend.models.Reply;
import com.google.gson.Gson;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ReplyAdapter implements IAdapter<Reply> {

    @Override
    public Map<String, Object> toMap(Reply reply) {
        Map<String, Object> replyMap = new HashMap<>();
        replyMap.put("replyId", reply.getId());
        replyMap.put("opName", reply.getCreatorUserName());
        replyMap.put("mdContent", reply.getMdContent());
        replyMap.put("postedOn", reply.getPostedOn());
        replyMap.put("answerId", reply.getAnswerId());

        return replyMap;
    }

    @Override
    public Reply fromMap(Map<String, Object> map) {
        Long id = (Long) map.get("replyId");
        String creatorUserName = (String) map.get("opName");
        String mdContent = (String) map.get("mdContent");
        Date postedOn = (Date) map.get("postedOn");
        Long answerId = (Long) map.get("answerId");
        return Reply.builder()
                .id(id)
                .creatorUserName(creatorUserName)
                .mdContent(mdContent)
                .postedOn(postedOn)
                .answerId(answerId)
                .build();
    }

    @Override
    public String toJson(Reply reply) {
        Gson gson = new Gson();
        return gson.toJson(reply);
    }
}

