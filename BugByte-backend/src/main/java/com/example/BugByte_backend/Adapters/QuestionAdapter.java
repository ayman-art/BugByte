package com.example.BugByte_backend.Adapters;

import com.example.BugByte_backend.models.Question;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class QuestionAdapter implements IAdapter<Question> {

    @Override
    public Map<String, Object> toMap(Question question) {
        Map<String, Object> questionMap = new HashMap<>();
        questionMap.put("questionId", question.getId());
        questionMap.put("opName", question.getCreatorUserName());
        questionMap.put("mdContent", question.getMdContent());
        questionMap.put("postedOn", question.getPostedOn());
        questionMap.put("communityId", question.getCommunityId());
        questionMap.put("upVotes", question.getUpVotes());
        questionMap.put("downVotes", question.getDownVotes());
        questionMap.put("validatedAnswerId", question.getValidatedAnswerId());
        questionMap.put("title", question.getTitle());
        questionMap.put("tags", question.getTags());

        return questionMap;
    }

    @Override
    public Question fromMap(Map<String, Object> map) {
        Long id = (Long) map.get("questionId");
        String creatorUserName = (String) map.get("opName");
        String mdContent = (String) map.get("mdContent");
        Date postedOn = (Date) map.get("postedOn");
        Long communityId = (Long) map.get("communityId");
        Long upVotes = (Long) map.get("upVotes");
        Long downVotes = (Long) map.get("downVotes");
        Long validatedAnswerId = (Long) map.get("validatedAnswerId");
        String title = (String) map.get("title");
        ArrayList<String> tags = (ArrayList<String>) map.get("tags");
        return Question.builder()
                .id(id)
                .creatorUserName(creatorUserName)
                .mdContent(mdContent)
                .postedOn(postedOn)
                .communityId(communityId)
                .upVotes(upVotes)
                .downVotes(downVotes)
                .title(title)
                .tags(tags)
                .validatedAnswerId(validatedAnswerId)
                .build();
    }

    @Override
    public String toJson(Question question) {
        Gson gson = new Gson();
        return gson.toJson(question);
    }
}

