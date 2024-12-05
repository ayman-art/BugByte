package com.example.BugByte_backend.repositories;

import com.example.BugByte_backend.models.Answer;
import com.example.BugByte_backend.models.Post;
import com.example.BugByte_backend.models.Question;
import com.example.BugByte_backend.models.Reply;

import java.util.Date;
import java.util.List;

public interface IPostingRepository {
     Long insertPost(String md_content , String op_name , Date posted_on);

     Post getPostByID(Long postId);

     Boolean insertQuestion(Long questionId , Long communityId);

     Boolean insertAnswer(Long answerId , Long questionId);

     Boolean insertReply(Long replyId , Long answerId);

     Boolean deleteQuestion(Long questionId);

     Boolean deleteAnswer(Long answerId);

     Boolean deleteReply(Long replyId);

     Boolean upVoteQuestion(Long questionId);

     Boolean downVoteQuestion(Long questionId);

     Boolean upVoteAnswer(Long AnswerId);

     Boolean downVoteAnswer(Long AnswerId);

     Boolean verifyAnswer(Long answerId);

     Boolean editPost(Long postId , String md_content);

     List<Question> getQuestionsByUserName(String userName);

     List<Answer> getAnswersByUserName(String userName);

     List<Reply> getRepliesByUserName(String userName);

     List<Answer> getAnswersForQuestion(Long questionId);

     List<Reply> getRepliesForAnswer(Long answerId);
     
}
