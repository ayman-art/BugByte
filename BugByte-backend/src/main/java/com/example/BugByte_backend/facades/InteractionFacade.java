package com.example.BugByte_backend.facades;


import com.example.BugByte_backend.Adapters.AnswerAdapter;
import com.example.BugByte_backend.Adapters.QuestionAdapter;
import com.example.BugByte_backend.Adapters.ReplyAdapter;
import com.example.BugByte_backend.models.Answer;
import com.example.BugByte_backend.models.Question;
import com.example.BugByte_backend.models.Reply;
import com.example.BugByte_backend.services.PostingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InteractionFacade {
    @Autowired
    PostingService postingService;
    public Map<String , Object> postQuestion(Map<String , Object> postData) throws Exception {
        try {
            Question question = new Question();
            question.setCreatorUserName((String) postData.get("opName"));
            question.setCommunityId((Long) postData.get("communityId"));
            question.setMdContent((String) postData.get("mdContent"));
            long questionId = postingService.postQuestion(question);
            Map<String , Object> questionData = new HashMap<>();
            questionData.put("questionId" , questionId);
            return questionData;
        }
        catch (Exception e){
         throw new Exception(e.getMessage());
        }
    }
    public Map<String , Object> postAnswer(Map<String , Object> postData) throws Exception {
        try {
            Answer answer = new Answer();
            answer.setCreatorUserName((String) postData.get("opName"));
            answer.setQuestionId((Long) postData.get("questionId"));
            answer.setMdContent((String) postData.get("mdContent"));
            long answerId = postingService.postAnswer(answer);
            Map<String , Object> answerData = new HashMap<>();
            answerData.put("answerId" , answerId);
            return answerData;
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    public Map<String , Object> postReply(Map<String , Object> postData) throws Exception {
        try {
            Reply reply = new Reply();
            reply.setCreatorUserName((String) postData.get("opName"));
            reply.setAnswerId((Long) postData.get("answerId"));
            reply.setMdContent((String) postData.get("mdContent"));
            long replyId = postingService.postReply(reply);
            Map<String , Object> replyData = new HashMap<>();
            replyData.put("replyId" , replyId);
            return replyData;
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    public boolean deleteQuestion(Map<String , Object> postData) throws Exception {
        try {
            long questionId = (long) postData.get("questionId");
            return postingService.deleteQuestion(questionId);
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    public boolean deleteAnswer(Map<String , Object> postData) throws Exception {
        try {
            long answerId = (long) postData.get("answerId");
            return postingService.deleteAnswer(answerId);
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    public boolean deleteReply(Map<String , Object> postData) throws Exception {
        try {
            long replyId = (long) postData.get("replyId");
            return postingService.deleteReply(replyId);
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    public boolean upVoteQuestion(Map<String , Object> postData) throws Exception {
        try {
            long questionId = (long) postData.get("questionId");
            return postingService.upVoteQuestion(questionId);
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    public boolean removeUpVoteQuestion(Map<String , Object> postData) throws Exception {
        try {
            long questionId = (long) postData.get("questionId");
            return postingService.removeUpVoteFromQuestion(questionId);
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    public boolean downVoteQuestion(Map<String , Object> postData) throws Exception {
        try {
            long questionId = (long) postData.get("questionId");
            return postingService.downVoteQuestion(questionId);
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    public boolean removeDownVoteQuestion(Map<String , Object> postData) throws Exception {
        try {
            long questionId = (long) postData.get("questionId");
            return postingService.removeDownVoteFromQuestion(questionId);
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    public boolean upVoteAnswer(Map<String , Object> postData) throws Exception {
        try {
            long answerId = (long) postData.get("answerId");
            return postingService.upVoteAnswer(answerId);
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    public boolean removeUpVoteAnswer(Map<String , Object> postData) throws Exception {
        try {
            long answerId = (long) postData.get("answerId");
            return postingService.removeUpFromVoteAnswer(answerId);
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    public boolean downVoteAnswer(Map<String , Object> postData) throws Exception {
        try {
            long answerId = (long) postData.get("answerId");
            return postingService.downVoteAnswer(answerId);
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    public boolean removeDownVoteAnswer(Map<String , Object> postData) throws Exception {
        try {
            long answerId = (long) postData.get("answerId");
            return postingService.removeDownVoteAnswer(answerId);
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    public boolean verifyAnswer(Map<String , Object> postData) throws Exception {
        try {
            long answerId = (long) postData.get("answerId");
            return postingService.verifyAnswer(answerId);
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    public boolean editPost(Map<String , Object> postData) throws Exception {
        try {
            long postId = (long) postData.get("postId");
            String content = (String) postData.get("mdContent");
            return postingService.editPost(postId , content);
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    public List<Map<String , Object>> getUserQuestions(Map<String, Object> userdata) throws Exception {
        try {
            QuestionAdapter questionAdapter = new QuestionAdapter();
            List<Question> questions = postingService.getUserQuestions((String) userdata.get("userName")
            , (Integer) userdata.get("limit"), (Integer) userdata.get("offset"));
            return questions.stream().map(questionAdapter::toMap).toList();
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    public List<Map<String , Object>> getCommunityQuestions(Map<String, Object> communityData) throws Exception {
        try {
            QuestionAdapter questionAdapter = new QuestionAdapter();
            List<Question> questions = postingService.getCommunityQuestions((Long) communityData.get("communityId")
                    , (Integer) communityData.get("limit"), (Integer) communityData.get("offset"));
            return questions.stream().map(questionAdapter::toMap).toList();
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    public List<Map<String , Object>> getUserAnswers(Map<String, Object> userdata) throws Exception {
        try {
            AnswerAdapter answerAdapter = new AnswerAdapter();
            List<Answer> answers = postingService.getUserAnswers((String) userdata.get("userName")
                    , (Integer) userdata.get("limit"), (Integer) userdata.get("offset"));
            return answers.stream().map(answerAdapter::toMap).toList();
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    public List<Map<String , Object>> getUserReplies(Map<String, Object> userdata) throws Exception {
        try {
            ReplyAdapter replyAdapter = new ReplyAdapter();
            List<Reply> replies = postingService.getUserReplies((String) userdata.get("userName")
                    , (Integer) userdata.get("limit"), (Integer) userdata.get("offset"));
            return replies.stream().map(replyAdapter::toMap).toList();
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    public List<Map<String , Object>> getAnswerForQuestion(Map<String, Object> questionData) throws Exception {
        try {
            AnswerAdapter answerAdapter = new AnswerAdapter();
            List<Answer> answers = postingService.getAnswersForQuestion((Long) questionData.get("questionId")
                    , (Integer) questionData.get("limit"), (Integer) questionData.get("offset"));
            return answers.stream().map(answerAdapter::toMap).toList();
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    public List<Map<String , Object>> getRepliesForAnswer(Map<String, Object> answerData) throws Exception {
        try {
            ReplyAdapter replyAdapter = new ReplyAdapter();
            List<Reply> replies = postingService.getRepliesForAnswer((Long) answerData.get("answerId")
                    , (Integer) answerData.get("limit"), (Integer) answerData.get("offset"));
            return replies.stream().map(replyAdapter::toMap).toList();
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
}
