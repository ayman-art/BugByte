package com.example.BugByte_backend.repositories;

import com.example.BugByte_backend.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class PostingRepository implements IPostingRepository{
    private static final String SQL_INSERT_POST = """
                INSERT INTO posts
                    (op_name, md_content, posted_on)
                VALUES
                    (?, ?, ?);
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
    private static final String SQL_GET_POST_BY_USERNAME_AND_TIME = "SELECT id FROM posts " +
            "WHERE posted_on = ? AND op_name = ?;";
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

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Override
    public Long insertPost(String md_content, String op_name) {
        if(op_name == null)
            throw new NullPointerException("username is null");
        Date date = new Date();
        int rows = jdbcTemplate.update(SQL_INSERT_POST, op_name, md_content, date);

        if (rows == 0)
            throw new RuntimeException("Invalid input");
        return getPostByOpAndTime(op_name , date);
    }

    @Override
    public Long getPostByOpAndTime(String userName, Date date) {
        if(userName == null)
            throw new NullPointerException("username is null");
        return jdbcTemplate.queryForObject(SQL_GET_POST_BY_USERNAME_AND_TIME,
                new Object[]{date, userName}, Long.class);
    }

    @Override
    public Post getPostByID(Long postId) {
        if (postId == null)
            throw new NullPointerException("postId is null");
        return jdbcTemplate.queryForObject(SQL_GET_POST_BY_ID, postRowMapper,postId);
    }

    @Override
    public Boolean insertQuestion(Long questionId, Long communityId) {
        if(questionId == null || communityId == null)
            throw new NullPointerException("question id or community id is null");
        int rows = jdbcTemplate.update(SQL_INSERT_QUESTION, questionId, communityId);

        if (rows == 0)
            throw new RuntimeException("Invalid input");
        return true;
    }

    @Override
    public Boolean insertAnswer(Long answerId, Long questionId) {
        if(answerId == null || questionId == null)
            throw new NullPointerException("question id or answer id is null");
        int rows = jdbcTemplate.update(SQL_INSERT_ANSWER, answerId, questionId);

        if (rows == 0)
            throw new RuntimeException("Invalid input");
        return true;
    }

    @Override
    public Boolean insertReply(Long replyId, Long answerId) {
        if(replyId == null || answerId == null)
            throw new NullPointerException("reply id or answer id is null");
        int rows = jdbcTemplate.update(SQL_INSERT_REPLY, replyId, answerId);

        if (rows == 0)
            throw new RuntimeException("Invalid input");
        return true;
    }

    @Override
    public Boolean deleteQuestion(Long questionId) {
        if(questionId == null)
            throw new NullPointerException("question id is null");
        int rows = jdbcTemplate.update( SQL_DELETE_QUESTION_BY_ID , questionId);

        if (rows == 0)
            throw new RuntimeException("Invalid input");
        return true;
    }

    @Override
    public Boolean deleteAnswer(Long answerId) {
        if(answerId == null)
            throw new NullPointerException("answer id is null");
        int rows = jdbcTemplate.update( SQL_DELETE_ANSWER_BY_ID , answerId);

        if (rows == 0)
            throw new RuntimeException("Invalid input");
        return true;
    }

    @Override
    public Boolean deleteReply(Long replyId) {
        if(replyId == null)
            throw new NullPointerException("reply id is null");
        int rows = jdbcTemplate.update( SQL_DELETE_REPLY_BY_ID , replyId);

        if (rows == 0)
            throw new RuntimeException("Invalid input");
        return true;
    }

    @Override
    public Boolean upVoteQuestion(Long questionId) {
        if(questionId == null)
            throw new NullPointerException("question id is null");
        int rows = jdbcTemplate.update( SQL_UPDATE_UP_VOTES_QUESTIONS , questionId);

        if (rows == 0)
            throw new RuntimeException("Invalid input");
        return true;
    }

    @Override
    public Boolean downVoteQuestion(Long questionId) {
        if(questionId == null)
            throw new NullPointerException("question id is null");
        int rows = jdbcTemplate.update( SQL_UPDATE_DOWN_VOTES_QUESTIONS , questionId);

        if (rows == 0)
            throw new RuntimeException("Invalid input");
        return true;
    }

    @Override
    public Boolean upVoteAnswer(Long AnswerId) {
        if(AnswerId == null)
            throw new NullPointerException("answer id is null");
        int rows = jdbcTemplate.update( SQL_UPDATE_UP_VOTES_ANSWERS , AnswerId);

        if (rows == 0)
            throw new RuntimeException("Invalid input");
        return true;
    }

    @Override
    public Boolean removeUpVoteFromAnswer(Long AnswerId) {
        if(AnswerId == null)
            throw new NullPointerException("answer id is null");
        int rows = jdbcTemplate.update( SQL_REMOVE_UP_VOTES_ANSWERS , AnswerId);

        if (rows == 0)
            throw new RuntimeException("Invalid input");
        return true;
    }

    @Override
    public Boolean removeDownVoteFromAnswer(Long AnswerId) {
        if(AnswerId == null)
            throw new NullPointerException("answer id is null");
        int rows = jdbcTemplate.update( SQL_REMOVE_DOWN_VOTES_ANSWERS , AnswerId);

        if (rows == 0)
            throw new RuntimeException("Invalid input");
        return true;
    }

    @Override
    public Boolean removeUpVoteFromQuestion(Long questionId) {
        if(questionId == null)
            throw new NullPointerException("answer id is null");
        int rows = jdbcTemplate.update( SQL_REMOVE_UP_VOTES_QUESTIONS , questionId);

        if (rows == 0)
            throw new RuntimeException("Invalid input");
        return true;
    }

    @Override
    public Boolean removeDownVoteFromQuestion(Long questionId) {
        if(questionId == null)
            throw new NullPointerException("answer id is null");
        int rows = jdbcTemplate.update( SQL_REMOVE_DOWN_VOTES_QUESTIONS , questionId);

        if (rows == 0)
            throw new RuntimeException("Invalid input");
        return true;
    }

    @Override
    public Boolean downVoteAnswer(Long AnswerId) {
        if(AnswerId == null)
            throw new NullPointerException("answer id is null");
        int rows = jdbcTemplate.update( SQL_UPDATE_DOWN_VOTES_ANSWERS , AnswerId);

        if (rows == 0)
            throw new RuntimeException("Invalid input");
        return true;
    }

    @Override
    public Boolean verifyAnswer(Long answerId) {
        if(answerId == null)
            throw new NullPointerException("answer id is null");
        int rows = jdbcTemplate.update( SQL_VERIFY_ANSWER, answerId);

        if (rows == 0)
            throw new RuntimeException("Invalid input");
        return true;
    }

    @Override
    public Boolean editPost(Long postId, String md_content) {
        if(postId == null)
            throw new NullPointerException("post id is null");
        int rows = jdbcTemplate.update( SQL_EDIT_POST, postId);

        if (rows == 0)
            throw new RuntimeException("Invalid input");
        return true;
    }

    @Override
    public List<Question> getQuestionsByUserName(String userName,Integer limit,Integer offset) {
        return null;
    }

    @Override
    public List<Answer> getAnswersByUserName(String userName,Integer limit,Integer offset) {
        return null;
    }

    @Override
    public List<Reply> getRepliesByUserName(String userName,Integer limit,Integer offset) {
        return null;
    }

    @Override
    public List<Answer> getAnswersForQuestion(Long questionId,Integer limit,Integer offset) {
        return null;
    }

    @Override
    public List<Reply> getRepliesForAnswer(Long answerId,Integer limit,Integer offset) {
        return null;
    }

    @Override
    public List<Question> getQuestionsByCommunity(Long communityId,Integer limit,Integer offset) {
        return null;
    }
    private final RowMapper<Post> postRowMapper = ((rs, rowNum) -> new Post(
            rs.getLong("id"),
            rs.getString("op_name"),
            rs.getString("md_content"),
            rs.getDate("posted_on")
    ));
}
