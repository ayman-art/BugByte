package com.example.BugByte_backend.repositories;

import com.example.BugByte_backend.models.Answer;
import com.example.BugByte_backend.models.Post;
import com.example.BugByte_backend.models.Question;
import com.example.BugByte_backend.models.Reply;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class PostingRepository implements IPostingRepository{
    private static final String SQL_INSERT_POST = """
                INSERT INTO posts
                    (op_name, md_content, posted_on)
                VALUES
                    (?, ?, now());
            """;
    private static final String SQL_INSERT_QUESTION = """
                INSERT INTO questions
                    (id, community_id, up_votes, down_votes)
                VALUES
                    (?, ?, 0, 0);
            """;
    private static final String SQL_INSERT_ANSWER = """
                INSERT INTO answers
                    (id, question_id, up_votes, down_votes)
                VALUES
                    (?, ?, 0, 0);
            """;
    private static final String SQL_INSERT_REPLY = """
                INSERT INTO replies
                    (id, answer_id)
                VALUES
                    (?, ?);
            """;
    private static final String SQL_GET_POST_BY_ID = "SELECT * FROM posts WHERE id = ?;";
    private static final String SQL_DELETE_QUESTION_BY_ID = "DELETE FROM questions WHERE id = ?;";
    private static final String SQL_DELETE_ANSWER_BY_ID = "DELETE FROM answers WHERE id = ?;";
    private static final String SQL_DELETE_REPLY_BY_ID = "DELETE FROM replies WHERE id = ?;";
    private static final String SQL_UPDATE_UP_VOTES_ANSWERS = "UPDATE answers SET up_votes = up_votes + 1" +
            "WHERE id = ?";
    private static final String SQL_REMOVE_UP_VOTES_ANSWERS = "UPDATE answers SET up_votes = up_votes - 1" +
            "WHERE id = ?";
    private static final String SQL_REMOVE_DOWN_VOTES_ANSWERS = "UPDATE answers SET down_votes = down_votes - 1" +
            "WHERE id = ?";
    private static final String SQL_UPDATE_DOWN_VOTES_ANSWERS = "UPDATE answers SET down_votes = down_votes + 1" +
            "WHERE id = ?";
    private static final String SQL_UPDATE_UP_VOTES_QUESTIONS = "UPDATE questions SET up_votes = up_votes + 1" +
            "WHERE id = ?";
    private static final String SQL_REMOVE_UP_VOTES_QUESTIONS = "UPDATE questions SET up_votes = up_votes - 1" +
            "WHERE id = ?";
    private static final String SQL_REMOVE_DOWN_VOTES_QUESTIONS = "UPDATE questions SET down_votes = down_votes - 1" +
            "WHERE id = ?";
    private static final String SQL_UPDATE_DOWN_VOTES_QUESTIONS = "UPDATE questions SET down_votes = down_votes + 1" +
            "WHERE id = ?";
    private static final String SQL_VERIFY_ANSWER = "UPDATE questions SET validated_answer = ?" +
            "WHERE id = ?";
    private static final String SQL_EDIT_POST = "UPDATE posts SET md_content = ? WHERE ID = ?;";

    private static final String SQL_GET_QUESTIONS_BY_USERNAME = "SELECT * FROM questions q " +
            "LEFT JOIN posts p on p.id = q.id WHERE p.op_name = ? ORDER BY p.posted_on DESC" +
            "LIMIT ? OFFSET ?;";
    private static final String SQL_GET_ANSWERS_BY_USERNAME = "SELECT * FROM answers a " +
            "LEFT JOIN posts p on p.id = a.id WHERE p.op_name = ? ORDER BY p.posted_on DESC" +
            "LIMIT ? OFFSET ?;";
    private static final String SQL_GET_REPLIES_BY_USERNAME = "SELECT * FROM replies r " +
            "LEFT JOIN posts p on p.id = r.id WHERE p.op_name = ? ORDER BY p.posted_on DESC" +
            "LIMIT ? OFFSET ?;";
    private static final String SQL_GET_ANSWERS_FOR_QUESTION = "SELECT * FROM answers a " +
            "LEFT JOIN posts p on p.id = a.id WHERE a.question_id = ? ORDER BY p.posted_on DESC" +
            "LIMIT ? OFFSET ?;";
    private static final String SQL_GET_REPLIES_FOR_ANSWER = "SELECT * FROM replies r " +
            "LEFT JOIN posts p on p.id = r.id WHERE r.answer_id = ? ORDER BY p.posted_on DESC" +
            "LIMIT ? OFFSET ?;";
    private static final String SQL_GET_QUESTIONS_BY_COMMUNITY = "SELECT * FROM questions q " +
            "LEFT JOIN posts p on p.id = q.id WHERE q.community_id = ? ORDER BY p.posted_on DESC" +
            "LIMIT ? OFFSET ?;";
    @Override
    public Long insertPost(String md_content, String op_name, Date posted_on) {
        return null;
    }

    @Override
    public Post getPostByID(Long postId) {
        return null;
    }

    @Override
    public Boolean insertQuestion(Long questionId, Long communityId) {
        return null;
    }

    @Override
    public Boolean insertAnswer(Long answerId, Long questionId) {
        return null;
    }

    @Override
    public Boolean insertReply(Long replyId, Long answerId) {
        return null;
    }

    @Override
    public Boolean deleteQuestion(Long questionId) {
        return null;
    }

    @Override
    public Boolean deleteAnswer(Long answerId) {
        return null;
    }

    @Override
    public Boolean deleteReply(Long replyId) {
        return null;
    }

    @Override
    public Boolean upVoteQuestion(Long questionId) {
        return null;
    }

    @Override
    public Boolean downVoteQuestion(Long questionId) {
        return null;
    }

    @Override
    public Boolean upVoteAnswer(Long AnswerId) {
        return null;
    }

    @Override
    public Boolean removeUpVoteFromAnswer(Long AnswerId) {
        return null;
    }

    @Override
    public Boolean removeDownVoteFromAnswer(Long AnswerId) {
        return null;
    }

    @Override
    public Boolean removeUpVoteFromQuestion(Long questionId) {
        return null;
    }

    @Override
    public Boolean removeDownVoteFromQuestion(Long questionId) {
        return null;
    }

    @Override
    public Boolean downVoteAnswer(Long AnswerId) {
        return null;
    }

    @Override
    public Boolean verifyAnswer(Long answerId) {
        return null;
    }

    @Override
    public Boolean editPost(Long postId, String md_content) {
        return null;
    }

    @Override
    public List<Question> getQuestionsByUserName(String userName) {
        return null;
    }

    @Override
    public List<Answer> getAnswersByUserName(String userName) {
        return null;
    }

    @Override
    public List<Reply> getRepliesByUserName(String userName) {
        return null;
    }

    @Override
    public List<Answer> getAnswersForQuestion(Long questionId) {
        return null;
    }

    @Override
    public List<Reply> getRepliesForAnswer(Long answerId) {
        return null;
    }

    @Override
    public List<Question> getQuestionsByCommunity(Long communityId) {
        return null;
    }
}
