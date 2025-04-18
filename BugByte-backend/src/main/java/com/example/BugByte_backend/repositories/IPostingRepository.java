package com.example.BugByte_backend.repositories;

import com.example.BugByte_backend.models.Answer;
import com.example.BugByte_backend.models.Post;
import com.example.BugByte_backend.models.Question;
import com.example.BugByte_backend.models.Reply;

import java.util.Date;
import java.util.List;

public interface IPostingRepository {
     Long insertPost(String md_content, String op_name);

     Long getPostByOpAndTime(String userName, Date date);

     Post getPostByID(Long postId);

     Boolean insertQuestion(Long questionId, String title, Long communityId);

     Boolean insertAnswer(Long answerId, Long questionId);

     Boolean insertReply(Long replyId, Long answerId);

     Boolean deleteQuestion(Long questionId);

     Boolean deleteAnswer(Long answerId);

     Boolean deleteReply(Long replyId);

     Boolean upVoteQuestion(Long questionId, Integer value, String userName) throws Exception;

     Boolean downVoteQuestion(Long questionId, Integer value , String userName) throws Exception;

     Boolean upVoteAnswer(Long AnswerId, Integer value , String userName) throws Exception;

     Boolean downVoteAnswer(Long AnswerId, Integer value , String userName) throws Exception;

    Boolean verifyAnswer(Long answerId, Long questionId);

    Boolean editPost(Long postId, String md_content);

     List<Question> getQuestionsByUserName(String userName, Integer limit, Integer offset);

     List<Answer> getAnswersByUserName(String userName, Integer limit, Integer offset);

     List<Reply> getRepliesByUserName(String userName, Integer limit, Integer offset);

     List<Answer> getAnswersForQuestion(Long questionId, Integer limit, Integer offset);

     List<Reply> getRepliesForAnswer(Long answerId, Integer limit, Integer offset);

     List<Question> getQuestionsByCommunity(Long communityId, Integer limit, Integer offset);

     Question getQuestionById(Long questionId);

     Answer getAnswerById(Long answerId);

     Reply getReplyById(Long replyId);
}
