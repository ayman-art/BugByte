package com.example.BugByte_backend.Adapters;

import com.example.BugByte_backend.models.Answer;
import com.google.gson.Gson;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AnswerAdapter implements IAdapter<Answer> {

    @Override
    public Map<String, Object> toMap(Answer answer) {
        Map<String, Object> answerMap = new HashMap<>();
        answerMap.put("answerId", answer.getId());
        answerMap.put("opName", answer.getCreatorUserName());
        answerMap.put("mdContent", answer.getMdContent());
        answerMap.put("postedOn", answer.getPostedOn());
        answerMap.put("questionId", answer.getQuestionId());
        answerMap.put("upVotes", answer.getUpVotes());
        answerMap.put("downVotes", answer.getDownVotes());

        return answerMap;
    }

    @Override
    public Answer fromMap(Map<String, Object> map) {
        Long id = (Long) map.get("answerId");
        String creatorUserName = (String) map.get("opName");
        String mdContent = (String) map.get("mdContent");
        Date postedOn = (Date) map.get("postedOn");
        Long questionId = (Long) map.get("questionId");
        Long upVotes = (Long) map.get("upVotes");
        Long downVotes = (Long) map.get("downVotes");

        return Answer.builder()
                .id(id)
                .creatorUserName(creatorUserName)
                .mdContent(mdContent)
                .postedOn(postedOn)
                .questionId(questionId)
                .upVotes(upVotes)
                .downVotes(downVotes)
                .build();
    }

    @Override
    public String toJson(Answer answer) {
        Gson gson = new Gson();
        return gson.toJson(answer);
    }
}

