package com.example.BugByte_backend.repositories;

import com.example.BugByte_backend.models.Post;

import java.util.Date;

public interface IPostingRepository {
     Long insertPost(String md_content , String op_name , Date posted_on);

     Post getPostByID(Long postId);

     void insertQuestion(Long questionId , Long communityId);

     void insertAnswer(Long answerId);



}
