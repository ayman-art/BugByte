package com.example.BugByte_backend.services;

import com.example.BugByte_backend.models.Answer;
import com.example.BugByte_backend.models.Post;
import com.example.BugByte_backend.models.Question;
import com.example.BugByte_backend.models.Reply;
import com.example.BugByte_backend.repositories.PostingRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class PostingService {
    @Autowired
    PostingRepository postingRepository;
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
}
