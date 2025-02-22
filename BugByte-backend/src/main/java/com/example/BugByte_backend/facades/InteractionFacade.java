package com.example.BugByte_backend.facades;


import com.example.BugByte_backend.Adapters.AnswerAdapter;
import com.example.BugByte_backend.Adapters.QuestionAdapter;
import com.example.BugByte_backend.Adapters.ReplyAdapter;
import com.example.BugByte_backend.models.Answer;
import com.example.BugByte_backend.models.Question;
import com.example.BugByte_backend.models.Reply;
import com.example.BugByte_backend.models.User;
import com.example.BugByte_backend.services.AuthenticationService;
import com.example.BugByte_backend.services.NotificationService.NotificationProducer;
import com.example.BugByte_backend.services.PostingService;
import com.example.BugByte_backend.services.UserService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InteractionFacade {
    @Autowired
    PostingService postingService;
    @Autowired
    AuthenticationService authenticationService;
    @Autowired
    NotificationProducer notifier;
    public Map<String , Object> postQuestion(Map<String , Object> postData) throws Exception {
        try {
            Question question = new Question();
            String token = (String) postData.get("jwt");
            String opName = authenticationService.getUserNameFromJwt(token);
            question.setCreatorUserName(opName);
            Integer communityId = (Integer) postData.get("communityId");
            question.setCommunityId(Long.valueOf(communityId));
            question.setMdContent((String) postData.get("mdContent"));
            question.setTitle((String) postData.get("title"));
            question.setTags((List<String>) postData.get("tags"));
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
            String token = (String) postData.get("jwt");
            String opName = authenticationService.getUserNameFromJwt(token);
            answer.setCreatorUserName(opName);
            answer.setQuestionId(Long.valueOf((Integer) postData.get("questionId")));
            answer.setMdContent((String) postData.get("mdContent"));
            long answerId = postingService.postAnswer(answer);
            Map<String , Object> answerData = new HashMap<>();
            answerData.put("answerId" , answerId);
            notifier.sendAnswerNotification(answer);
            return answerData;
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    public Map<String , Object> postReply(Map<String , Object> postData) throws Exception {
        try {
            Reply reply = new Reply();
            String token = (String) postData.get("jwt");
            String opName = authenticationService.getUserNameFromJwt(token);
            reply.setCreatorUserName(opName);
            reply.setAnswerId(Long.valueOf((Integer) postData.get("answerId")));
            reply.setMdContent((String) postData.get("mdContent"));
            long replyId = postingService.postReply(reply);
            Map<String , Object> replyData = new HashMap<>();
            replyData.put("replyId" , replyId);
            notifier.sendReplyNotification(reply);
            return replyData;
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    public boolean deleteQuestion(Map<String , Object> postData) throws Exception {
        try {
            String token = (String) postData.get("jwt");
            String opName = authenticationService.getUserNameFromJwt(token);
            Long questionId = Long.valueOf((Integer) postData.get("questionId"));
            return postingService.deleteQuestion(questionId , opName);
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    public boolean deleteAnswer(Map<String , Object> postData) throws Exception {
        try {
            String token = (String) postData.get("jwt");
            String opName = authenticationService.getUserNameFromJwt(token);
            Long answerId = Long.valueOf((Integer) postData.get("answerId"));
            return postingService.deleteAnswer(answerId , opName);
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    public boolean deleteReply(Map<String , Object> postData) throws Exception {
        try {
            String token = (String) postData.get("jwt");
            String opName = authenticationService.getUserNameFromJwt(token);
            Long replyId = Long.valueOf((Integer) postData.get("replyId"));
            return postingService.deleteReply(replyId , opName);
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    public boolean upVoteQuestion(Map<String , Object> postData) throws Exception {
        try {
            String token = (String) postData.get("jwt");
            String userName = authenticationService.getUserNameFromJwt(token);
            Long questionId = Long.valueOf((Integer) postData.get("questionId"));
            return postingService.upVoteQuestion(questionId , userName);
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    public boolean removeUpVoteQuestion(Map<String , Object> postData) throws Exception {
        try {
            String token = (String) postData.get("jwt");
            String userName = authenticationService.getUserNameFromJwt(token);
            Long questionId = Long.valueOf((Integer) postData.get("questionId"));
            return postingService.removeUpVoteFromQuestion(questionId , userName);
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    public boolean downVoteQuestion(Map<String , Object> postData) throws Exception {
        try {
            String token = (String) postData.get("jwt");
            String userName = authenticationService.getUserNameFromJwt(token);
            Long questionId = Long.valueOf((Integer) postData.get("questionId"));
            return postingService.downVoteQuestion(questionId , userName);
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    public boolean removeDownVoteQuestion(Map<String , Object> postData) throws Exception {
        try {
            String token = (String) postData.get("jwt");
            String userName = authenticationService.getUserNameFromJwt(token);
            Long questionId = Long.valueOf((Integer) postData.get("questionId"));
            return postingService.removeDownVoteFromQuestion(questionId , userName);
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    public boolean upVoteAnswer(Map<String , Object> postData) throws Exception {
        try {
            String token = (String) postData.get("jwt");
            String userName = authenticationService.getUserNameFromJwt(token);
            Long answerId = Long.valueOf((Integer) postData.get("answerId"));
            return postingService.upVoteAnswer(answerId , userName);
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    public boolean removeUpVoteAnswer(Map<String , Object> postData) throws Exception {
        try {
            String token = (String) postData.get("jwt");
            String userName = authenticationService.getUserNameFromJwt(token);
            Long answerId = Long.valueOf((Integer) postData.get("answerId"));
            return postingService.removeUpVoteFromAnswer(answerId , userName);
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    public boolean downVoteAnswer(Map<String , Object> postData) throws Exception {
        try {
            String token = (String) postData.get("jwt");
            String userName = authenticationService.getUserNameFromJwt(token);
            Long answerId = Long.valueOf((Integer) postData.get("answerId"));
            return postingService.downVoteAnswer(answerId , userName);
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    public boolean removeDownVoteAnswer(Map<String , Object> postData) throws Exception {
        try {
            String token = (String) postData.get("jwt");
            String userName = authenticationService.getUserNameFromJwt(token);
            Long answerId = Long.valueOf((Integer) postData.get("answerId"));
            return postingService.removeDownVoteAnswer(answerId , userName);
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    public boolean verifyAnswer(Map<String , Object> postData) throws Exception {
        try {
            String token = (String) postData.get("jwt");
            String opName = authenticationService.getUserNameFromJwt(token);
            Long answerId = Long.valueOf((Integer) postData.get("answerId"));
            System.out.println("answerId: " + answerId);
            return postingService.verifyAnswer(answerId , opName);
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    public boolean editPost(Map<String , Object> postData) throws Exception {
        try {
            Long postId = Long.valueOf((Integer) postData.get("postId"));
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
            String token = (String) userdata.get("jwt");
            AuthenticationService.parseToken(token);
            String userName = (String) userdata.get("name");
            List<Question> questions = postingService.getUserQuestions(userName
            , (Integer) userdata.get("limit"), (Integer) userdata.get("offset"));
            return questions.stream().map(question -> {
                Map<String, Object> questionMap = questionAdapter.toMap(question);
                try {
                    questionMap.put("communityName" , postingService.getQuestionCommunity(question.getCommunityId()));
                    questionMap.put("isUpVoted", postingService.isUpVoted(userName , question.getId()));
                    questionMap.put("isDownVoted", postingService.isDownVoted(userName , question.getId()));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return questionMap;
            }).toList();
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    public List<Map<String , Object>> getCommunityQuestions(Map<String, Object> communityData) throws Exception {
        try {
            QuestionAdapter questionAdapter = new QuestionAdapter();
            String token = (String) communityData.get("jwt");
            Claims claim = AuthenticationService.parseToken(token);
            String userName = claim.getSubject();
            List<Question> questions = postingService.getCommunityQuestions(Long.valueOf((Integer) communityData.get("communityId"))
                    , (Integer) communityData.get("limit"), (Integer) communityData.get("offset"));
            return questions.stream().map(question -> {
                Map<String, Object> questionMap = questionAdapter.toMap(question);
                try {
                    questionMap.put("communityName" , postingService.getQuestionCommunity(question.getCommunityId()));
                    questionMap.put("isUpVoted", postingService.isUpVoted(userName , question.getId()));
                    questionMap.put("isDownVoted", postingService.isDownVoted(userName , question.getId()));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return questionMap;
            }).toList();
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
//    public List<Map<String , Object>> getUserAnswers(Map<String, Object> userdata) throws Exception {
//        try {
//            AnswerAdapter answerAdapter = new AnswerAdapter();
//            String token = (String) userdata.get("jwt");
//            Claims claim = AuthenticationService.parseToken(token);
//            String userName = claim.getSubject();
//            List<Answer> answers = postingService.getUserAnswers((String) userdata.get("userName")
//                    , (Integer) userdata.get("limit"), (Integer) userdata.get("offset"));
//            return answers.stream().map(answer -> {
//                Map<String, Object> answerMap = answerAdapter.toMap(answer);
//                try {
//                    answerMap.put("isUpVoted", postingService.isUpVoted(userName , answer.getId()));
//                    answerMap.put("isDownVoted", postingService.isDownVoted(userName , answer.getId()));
//                } catch (Exception e) {
//                    throw new RuntimeException(e);
//                }
//                return answerMap;
//            }).toList();
//        }
//        catch (Exception e){
//            throw new Exception(e.getMessage());
//        }
//    }
//    public List<Map<String , Object>> getUserReplies(Map<String, Object> userdata) throws Exception {
//        try {
//            String token = (String) userdata.get("jwt");
//            Claims claim = AuthenticationService.parseToken(token);
//            String userName = claim.getSubject();
//            ReplyAdapter replyAdapter = new ReplyAdapter();
//            List<Reply> replies = postingService.getUserReplies((String) userdata.get("userName")
//                    , (Integer) userdata.get("limit"), (Integer) userdata.get("offset"));
//            return replies.stream().map(replyAdapter::toMap).toList();
//        }
//        catch (Exception e){
//            throw new Exception(e.getMessage());
//        }
//    }
    public List<Map<String , Object>> getAnswersForQuestion(Map<String, Object> questionData) throws Exception {
        try {
            String token = (String) questionData.get("jwt");
            Claims claim = AuthenticationService.parseToken(token);
            String userName = authenticationService.getUserNameFromJwt(token);
            AnswerAdapter answerAdapter = new AnswerAdapter();
            List<Answer> answers = postingService.getAnswersForQuestion(Long.valueOf((Integer) questionData.get("questionId"))
                    , (Integer) questionData.get("limit"), (Integer) questionData.get("offset"));
            return answers.stream().map(answer -> {
                Map<String, Object> answerMap = answerAdapter.toMap(answer);
                try {
                    answerMap.put("isUpVoted", postingService.isUpVoted(userName , answer.getId()));
                    answerMap.put("isDownVoted", postingService.isDownVoted(userName , answer.getId()));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return answerMap;
            }).toList();
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    public List<Map<String , Object>> getRepliesForAnswer(Map<String, Object> answerData) throws Exception {
        try {
            ReplyAdapter replyAdapter = new ReplyAdapter();
            List<Reply> replies = postingService.getRepliesForAnswer(Long.valueOf((Integer) answerData.get("answerId"))
                    , (Integer) answerData.get("limit"), (Integer) answerData.get("offset"));
            return replies.stream().map(replyAdapter::toMap).toList();
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    public Map<String , Object> getQuestion(Map<String, Object> questionData) throws Exception{
        try {
            QuestionAdapter questionAdapter = new QuestionAdapter();
            String token = (String) questionData.get("jwt");
            String userName = authenticationService.getUserNameFromJwt(token);
            Question question = postingService.getQuestion(Long.valueOf((Integer) questionData.get("questionId")));
            Map<String , Object> questionMap = questionAdapter.toMap(question);
            if (question.getValidatedAnswerId() != 0){
                Answer answer = postingService.getAnswer(question.getValidatedAnswerId());
                questionMap.put("answerMdContent" , answer.getMdContent());
                questionMap.put("answerOp" , answer.getCreatorUserName());
                questionMap.put("answerUpVotes" , answer.getUpVotes());
                questionMap.put("answerDownVotes" , answer.getDownVotes());
                questionMap.put("answerIsUpVoted" , postingService.isUpVoted(userName , answer.getId()));
                questionMap.put("answerIsDownVoted" , postingService.isDownVoted(userName , answer.getId()));
                questionMap.put("answerPostedOn" ,answer.getPostedOn());
            }
            try {
                questionMap.put("communityName" , postingService.getQuestionCommunity(question.getCommunityId()));
                questionMap.put("isUpVoted", postingService.isUpVoted(userName , question.getId()));
                questionMap.put("isDownVoted", postingService.isDownVoted(userName , question.getId()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return questionMap;
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    public Map<String , Object> getAnswer(Map<String, Object> answerData) throws Exception{
        try {
            AnswerAdapter answerAdapter = new AnswerAdapter();
            String token = (String) answerData.get("jwt");
            Claims claim = AuthenticationService.parseToken(token);
            String userName = authenticationService.getUserNameFromJwt(token);
            Answer answer = postingService.getAnswer(Long.valueOf((Integer) answerData.get("answerId")));
            Map<String , Object> answerMap = answerAdapter.toMap(answer);
            System.out.println(answerMap);
            try {
                answerMap.put("isUpVoted", postingService.isUpVoted(userName , answer.getId()));
                answerMap.put("isDownVoted", postingService.isDownVoted(userName , answer.getId()));
            } catch (Exception e) {
                System.out.println("where");
                throw new RuntimeException(e);
            }
            return answerMap;
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    public Map<String , Object> getReply(Map<String, Object> replyData) throws Exception{
        try {
            ReplyAdapter replyAdapter = new ReplyAdapter();
            Reply reply = postingService.getReply(Long.valueOf((Integer) replyData.get("replyId")));
            Map<String , Object> replyMap = replyAdapter.toMap(reply);
            return replyMap;
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

}
