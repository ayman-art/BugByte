package com.example.BugByte_backend.services;

import com.example.BugByte_backend.models.*;
import com.example.BugByte_backend.repositories.CommunityRepository;
import com.example.BugByte_backend.repositories.PostingRepository;
import com.example.BugByte_backend.repositories.TagsRepository;
import com.example.BugByte_backend.repositories.UserRepositoryImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostingService {
    @Autowired
    PostingRepository postingRepository;

    @Autowired
    CommunityRepository communityRepository;
    @Autowired
    UserRepositoryImp userRepositoryImp;

    @Autowired
    TagsRepository tagsRepository;

    @Autowired
    SearchingFilteringQuestionService filteringQuestionService;

    @Autowired
    UserService userService;

    @Autowired
    RecommendationSystemService recommendationSystemService;

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
    public Question getQuestion(long questionId) throws Exception{
        try {
            Post post = postingRepository.getPostByID(questionId);
            if (post == null)
                throw new Exception("post is null");
            return postingRepository.getQuestionById(questionId);
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    public Answer getAnswer(long answerId) throws Exception{
        try {
            Post post = postingRepository.getPostByID(answerId);
            if (post == null)
                throw new Exception("post is null");
            return postingRepository.getAnswerById(answerId);
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    public Reply getReply(long replyId) throws Exception{
        try {
            Post post = postingRepository.getPostByID(replyId);
            if (post == null)
                throw new Exception("post is null");
            return postingRepository.getReplyById(replyId);
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    public long postQuestion(Question q) throws Exception {
        try {
            Long postId = postingRepository.insertPost(q.getMdContent(), q.getCreatorUserName());
            if (postId == null)
                throw new Exception("post id is null");
            if (postingRepository.insertQuestion(postId, q.getTitle(), q.getCommunityId())) {
                if (q.getTags() != null && !q.getTags().isEmpty())
                    tagsRepository.bulkAddTagsToQuestion(postId, q.getTags());
                q.setId(postId);

                filteringQuestionService.saveQuestion(q);
                List<User> followers = userService.getFollowers(q.getCreatorUserName());
                List<User> joinedMembers = communityRepository.getCommunityMembers(q.getCommunityId());

                recommendationSystemService.updateUsersFeed(followers, q);
                recommendationSystemService.updateUsersFeed(joinedMembers, q);

                return postId;
            }
            throw new Exception("error inserting a question");
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    public long postAnswer(Answer a) throws Exception{
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
    public long postReply(Reply r) throws Exception{
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
    public boolean deleteQuestion(long questionId , String userName) throws Exception{
        try {
            Post post = postingRepository.getPostByID(questionId);
            if (! post.getCreatorUserName().equals(userName)) {
                throw new Exception("this user can't delete this post");
            }
            Question question = postingRepository.getQuestionById(questionId);
            boolean res = postingRepository.deleteQuestion(questionId);
            filteringQuestionService.deleteQuestion(question);
            return res;
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    public boolean deleteAnswer(long answerId , String userName) throws Exception{
        try {
            Post post = postingRepository.getPostByID(answerId);
            if (! post.getCreatorUserName().equals(userName))
                throw new Exception("this user can't delete this post");
            return postingRepository.deleteAnswer(answerId);
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    public boolean deleteReply(long replyId , String userName) throws Exception{
        try {
            Post post = postingRepository.getPostByID(replyId);
            if (! post.getCreatorUserName().equals(userName))
                throw new Exception("this user can't delete this post");
            return postingRepository.deleteReply(replyId);
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    public boolean upVoteQuestion(long questionId , String userName) throws Exception{
        try {
            boolean res = postingRepository.upVoteQuestion(questionId , 1 , userName);
            Question question = postingRepository.getQuestionById(questionId);
            filteringQuestionService.saveQuestion(question);
            return res;
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    public boolean removeUpVoteFromQuestion(long questionId ,  String userName) throws Exception{
        try {
            boolean res = postingRepository.upVoteQuestion(questionId , -1 , userName);
            Question question = postingRepository.getQuestionById(questionId);
            filteringQuestionService.saveQuestion(question);
            return res;
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    public boolean downVoteQuestion(long questionId ,  String userName) throws Exception{
        try {
            boolean res = postingRepository.downVoteQuestion(questionId , 1 , userName);
            Question question = postingRepository.getQuestionById(questionId);
            filteringQuestionService.saveQuestion(question);
            return res;
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    public boolean removeDownVoteFromQuestion(long questionId ,  String userName) throws Exception{
        try {
            boolean res = postingRepository.downVoteQuestion(questionId , -1 , userName);
            Question question = postingRepository.getQuestionById(questionId);
            filteringQuestionService.saveQuestion(question);
            return res;
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    public boolean upVoteAnswer(long answerId ,  String userName) throws Exception{
        try {
            return postingRepository.upVoteAnswer(answerId , 1 , userName);
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    public boolean removeUpVoteFromAnswer(long answerId ,  String userName) throws Exception{
        try {
            return postingRepository.upVoteAnswer(answerId , -1 , userName);
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    public boolean downVoteAnswer(long answerId ,  String userName) throws Exception{
        try {
            return postingRepository.downVoteAnswer(answerId , 1 , userName);
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    public boolean removeDownVoteAnswer(long answerId ,  String userName) throws Exception{
        try {
            return postingRepository.downVoteAnswer(answerId , -1 , userName);
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    public boolean verifyAnswer(long answerId , String userName) throws Exception{
        try {
            Answer answer = postingRepository.getAnswerById(answerId);
            Post post = postingRepository.getPostByID(answer.getQuestionId());
            if (! post.getCreatorUserName().equals(userName))
                throw new Exception("this user can't delete this post");
            return postingRepository.verifyAnswer(answerId, answer.getQuestionId());
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    public boolean editPost(long postId , String mdContent) throws Exception{
        try {
            boolean res = postingRepository.editPost(postId , mdContent);
            try {
                Question question = postingRepository.getQuestionById(postId);
                filteringQuestionService.saveQuestion(question);
            } catch (Exception ignored) {}

            return res;
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    public List<Question> getCommunityQuestions(long communityId, int limit, int offset) throws Exception {
        try {
            Community community = communityRepository.findCommunityById(communityId);
            if (community == null)
                throw new Exception("Community is null");

            List<Question> questions = postingRepository.getQuestionsByCommunity(communityId, limit , offset);
            if (questions == null)
                throw new Exception("there is no questions for this community");

            addTagsToQuestions(questions);

            return questions;
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    public List<Question> getUserQuestions(String userName, int limit, int offset) throws Exception {
        try {
           User user  = userRepositoryImp.findByIdentity(userName);
            if (user == null)
                throw new Exception("user is null");

            List<Question> questions = postingRepository.getQuestionsByUserName(userName, limit, offset);
            if (questions == null)
                throw new Exception("there is no questions for this user");

            addTagsToQuestions(questions);

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
    public List<Reply> getUserReplies(String userName, int limit, int offset) throws Exception{
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
    public boolean isUpVoted(String userName , long postId) throws Exception {
        User user  = userRepositoryImp.findByIdentity(userName);
        if (user == null){
            throw new Exception("user is null");
        }
        Post post = postingRepository.getPostByID(postId);
        if (post == null){
            throw new Exception("post is null");
        }
       return postingRepository.is_UpVoted(userName , postId);
    }
    public boolean isDownVoted(String userName , long postId) throws Exception {
        User user  = userRepositoryImp.findByIdentity(userName);
        if (user == null){
            throw new Exception("user is null");
        }
        Post post = postingRepository.getPostByID(postId);
        if (post == null){
            throw new Exception("post is null");
        }
        return postingRepository.is_DownVoted(userName , postId);
    }
    public String getQuestionCommunity(long communityId) throws Exception {
        Community community = communityRepository.findCommunityById(communityId);
        if (community == null)
            throw new Exception("community is null");
        return community.getName();
    }


    private void addTagsToQuestions(List<Question> questions) {
        for (Question question : questions) {
            List<String> questionTags = tagsRepository.findTagsByQuestion(question.getId());
            question.setTags(questionTags);
        }
    }
}
