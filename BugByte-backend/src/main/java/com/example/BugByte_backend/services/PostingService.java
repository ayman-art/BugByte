package com.example.BugByte_backend.services;

import com.example.BugByte_backend.models.*;
import com.example.BugByte_backend.repositories.CommunityRepository;
import com.example.BugByte_backend.repositories.PostingRepository;
import com.example.BugByte_backend.repositories.UserRepositoryImp;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class PostingService {
    @Autowired
    PostingRepository postingRepository;

    @Autowired
    CommunityRepository communityRepository;
    @Autowired
    UserRepositoryImp userRepositoryImp;
    public Post getPost(long postId) throws Exception{
        try {
            Post post = postingRepository.getPostByID(postId);
            if (post == null)
                throw new Exception("post is null");
            return post;
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    public long PostQuestion(Question q) throws Exception{
        try {
            Long postId = postingRepository.insertPost(q.getMdContent() , q.getCreatorUserName());
            if (postId == null)
                throw new Exception("post id is null");
            if(postingRepository.insertQuestion(postId , q.getCommunityId())){
                return postId;
            }
            throw new Exception("error inserting a question");
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    public long PostAnswer(Answer a) throws Exception{
        try {
            Long postId = postingRepository.insertPost(a.getMdContent() , a.getCreatorUserName());
            if (postId == null)
                throw new Exception("post id is null");
            if(postingRepository.insertAnswer(postId , a.getQuestionId())){
                return postId;
            }
            throw new Exception("error inserting an answer");
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    public long PostReply(Reply r) throws Exception{
        try {
            Long postId = postingRepository.insertPost(r.getMdContent() , r.getCreatorUserName());
            if (postId == null)
                throw new Exception("post id is null");
            if(postingRepository.insertReply(postId , r.getAnswerId())){
                return postId;
            }
            throw new Exception("error inserting a reply");
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    public boolean deleteQuestion(long questionId) throws Exception{
        try {
             return postingRepository.deleteQuestion(questionId);
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    public boolean deleteAnswer(long answerId) throws Exception{
        try {
            return postingRepository.deleteAnswer(answerId);
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    public boolean deleteReply(long replyId) throws Exception{
        try {
            return postingRepository.deleteReply(replyId);
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    public boolean upVoteQuestion(long questionId) throws Exception{
        try {
            return postingRepository.upVoteQuestion(questionId , 1);
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    public boolean removeUpVoteFromQuestion(long questionId) throws Exception{
        try {
            return postingRepository.upVoteQuestion(questionId , -1);
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    public boolean downVoteQuestion(long questionId) throws Exception{
        try {
            return postingRepository.downVoteQuestion(questionId , 1);
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    public boolean removeDownVoteFromQuestion(long questionId) throws Exception{
        try {
            return postingRepository.downVoteQuestion(questionId , -1);
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    public boolean upVoteAnswer(long answerId) throws Exception{
        try {
            return postingRepository.upVoteAnswer(answerId , 1);
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    public boolean removeUpFromVoteAnswer(long answerId) throws Exception{
        try {
            return postingRepository.upVoteAnswer(answerId , -1);
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    public boolean downVoteAnswer(long answerId) throws Exception{
        try {
            return postingRepository.downVoteAnswer(answerId , 1);
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    public boolean removeDownVoteAnswer(long answerId) throws Exception{
        try {
            return postingRepository.downVoteAnswer(answerId , -1);
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    public boolean verifyAnswer(long answerId) throws Exception{
        try {
            return postingRepository.verifyAnswer(answerId);
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    public boolean editPost(long postId , String mdContent) throws Exception{
        try {
            return postingRepository.editPost(postId , mdContent);
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    public List<Question> getCommunityQuestions(long communityId , int limit , int offset) throws Exception{
        try {
            Community community = communityRepository.findCommunityById(communityId);
            if (community == null){
                throw new Exception("Community is null");
            }
            List<Question> questions = postingRepository.getQuestionsByCommunity(communityId, limit , offset);
            if (questions == null){
                throw new Exception("there is no questions for this community");
            }
            return questions;
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    public List<Question> getUserQuestions(String userName , int limit , int offset) throws Exception{
        try {
           User user  = userRepositoryImp.findByIdentity(userName);
            if (user == null){
                throw new Exception("user is null");
            }
            List<Question> questions = postingRepository.getQuestionsByUserName(userName, limit , offset);
            if (questions == null){
                throw new Exception("there is no questions for this user");
            }
            return questions;
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    public List<Answer> getUserAnswers(String userName , int limit , int offset) throws Exception{
        try {
            User user  = userRepositoryImp.findByIdentity(userName);
            if (user == null){
                throw new Exception("user is null");
            }
            List<Answer> answers = postingRepository.getAnswersByUserName(userName, limit , offset);
            if (answers == null){
                throw new Exception("there is no answers for this user");
            }
            return answers;
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    public List<Reply> getUserReplies(String userName , int limit , int offset) throws Exception{
        try {
            User user  = userRepositoryImp.findByIdentity(userName);
            if (user == null){
                throw new Exception("user is null");
            }
            List<Reply> replies = postingRepository.getRepliesByUserName(userName, limit , offset);
            if (replies == null){
                throw new Exception("there is no replies for this user");
            }
            return replies;
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    public List<Answer> getAnswersForQuestion(long questionId , int limit , int offset) throws Exception{
        try {
            Question question = postingRepository.getQuestionById(questionId);
            if (question == null){
                throw new Exception("question is null");
            }
            List<Answer> answers = postingRepository.getAnswersForQuestion(questionId, limit , offset);
            if (answers == null){
                throw new Exception("there is no answers for this question");
            }
            return answers;
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    public List<Reply> getRepliesForAnswer(long answerId , int limit , int offset) throws Exception{
        try {
            Answer answer = postingRepository.getAnswerById(answerId);
            if (answer == null){
                throw new Exception("answer is null");
            }
            List<Reply> replies = postingRepository.getRepliesForAnswer(answerId, limit , offset);
            if (replies == null){
                throw new Exception("there is no replies for this answer");
            }
            return replies;
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
}
