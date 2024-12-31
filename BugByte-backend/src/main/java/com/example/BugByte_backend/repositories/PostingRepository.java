package com.example.BugByte_backend.repositories;

import com.example.BugByte_backend.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.IllegalFormatCodePointException;
import java.util.List;

@Repository
public class PostingRepository implements IPostingRepository{
    private static final String SQL_INSERT_POST = """
                INSERT INTO posts
                    (op_name, md_content, posted_on)
                VALUES
                    (?, ?, ?);
            """;
    private static final String SQL_INSERT_UPVOTE = """
                INSERT INTO upVotes
                    (userName, post_id)
                VALUES
                    (?, ?);
            """;
    private static final String SQL_INSERT_DOWNVOTE = """
                INSERT INTO downVotes
                    (userName, post_id)
                VALUES
                    (?, ?);
            """;
    private static final String SQL_INSERT_QUESTION = """
                INSERT INTO questions
                    (id, title, community_id, up_votes, down_votes)
                VALUES
                    (?, ?, ?, 0, 0);
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
    private static final String SQL_GET_POST_BY_USERNAME_AND_TIME = """
                SELECT id FROM posts
                WHERE posted_on = ? AND op_name = ?;
            """;
    private static final String SQL_DELETE_QUESTION_BY_ID = "DELETE FROM questions WHERE id = ?;";
    private static final String SQL_DELETE_ANSWER_BY_ID = "DELETE FROM answers WHERE id = ?;";
    private static final String SQL_DELETE_REPLY_BY_ID = "DELETE FROM replies WHERE id = ?;";
    private static final String SQL_UPDATE_UP_VOTES_ANSWERS = """
                UPDATE answers
                SET up_votes = up_votes + ?
                WHERE id = ?;
            """;
    private static final String SQL_UPDATE_DOWN_VOTES_ANSWERS = """
                UPDATE answers
                SET down_votes = down_votes + ?
                WHERE id = ?;
            """;
    private static final String SQL_UPDATE_UP_VOTES_QUESTIONS = """
                UPDATE questions
                SET up_votes = up_votes + ?
                WHERE id = ?;
            """;
    private static final String SQL_UPDATE_DOWN_VOTES_QUESTIONS = """
                UPDATE questions
                SET down_votes = down_votes + ?
                WHERE id = ?;
            """;
    private static final String SQL_VERIFY_ANSWER = """
                UPDATE questions
                SET validated_answer_id = ?
                WHERE id = ?;
            """;
    private static final String SQL_EDIT_POST = "UPDATE posts SET md_content = ? WHERE id = ?;";

    private static final String SQL_GET_QUESTIONS_BY_USERNAME = """
                SELECT *
                FROM questions q
                JOIN posts p on p.id = q.id
                WHERE p.op_name = ?
                ORDER BY p.posted_on DESC
                LIMIT ?
                OFFSET ?;
            """;
    private static final String SQL_GET_ANSWERS_BY_USERNAME = """
                SELECT *
                FROM answers a
                JOIN posts p on p.id = a.id
                WHERE p.op_name = ?
                ORDER BY p.posted_on DESC
                LIMIT ?
                OFFSET ?;
            """;
    private static final String SQL_GET_REPLIES_BY_USERNAME = """
                SELECT *
                FROM replies r
                JOIN posts p on p.id = r.id
                WHERE p.op_name = ?
                ORDER BY p.posted_on DESC
                LIMIT ?
                OFFSET ?;
            """;
    private static final String SQL_GET_ANSWERS_FOR_QUESTION = """
                SELECT *
                FROM answers a
                JOIN posts p on p.id = a.id
                WHERE a.question_id = ?
                ORDER BY p.posted_on DESC
                LIMIT ? OFFSET ?;
            """;
    private static final String SQL_GET_REPLIES_FOR_ANSWER = """
                SELECT *
                FROM replies r
                JOIN posts p on p.id = r.id
                WHERE r.answer_id = ?
                ORDER BY p.posted_on DESC
                LIMIT ?
                OFFSET ?;
            """;
    private static final String SQL_GET_QUESTIONS_BY_COMMUNITY = """
                SELECT *
                FROM questions q
                JOIN posts p on p.id = q.id
                WHERE q.community_id = ?
                ORDER BY p.posted_on DESC
                LIMIT ?
                OFFSET ?;
            """;
    private static final String SQL_GET_QUESTION_BY_ID = """
                SELECT *
                FROM questions q
                JOIN posts p on p.id = q.id
                WHERE q.id = ?;
            """;
    private static final String SQL_GET_ANSWER_BY_ID = """
                SELECT *
                FROM answers a
                JOIN posts p on p.id = a.id
                WHERE a.id = ?;
            """;
    private static final String SQL_GET_REPLY_BY_ID = """
                SELECT *
                FROM replies r
                JOIN posts p on p.id = r.id
                WHERE r.id = ?;
            """;
    private static final String SQL_GET_UP_VOTE = "SELECT COUNT(*) FROM upVotes WHERE userName = ? AND post_id = ?;";

    private static final String SQL_GET_DOWN_VOTE = "SELECT COUNT(*) FROM downVotes WHERE userName = ? AND post_id = ?;";

    private static final String SQL_DELETE_UP_VOTE = "DELETE FROM upVotes WHERE userName = ? AND post_id = ?;";
    private static final String SQL_DELETE_DOWN_VOTE = "DELETE FROM downVotes WHERE userName = ? AND post_id = ?;";
    private static final String SQL_DELETE_ANSWERS_BY_QUESTION_ID = "DELETE FROM answers WHERE question_id = ?;";
    private static final String SQL_DELETE_REPLIES_BY_ANSWER_ID = "DELETE FROM replies WHERE answer_id = ?;";
    private static final String SQL_DELETE_POST_BY_ID = "DELETE FROM posts WHERE id = ?;";


    @Autowired
    private JdbcTemplate jdbcTemplate ;

    @Override
    public Long insertPost(String mdContent, String op_name) {
        if (mdContent == null || op_name == null)
            throw new NullPointerException("md content or username is null");

        Date date = new Date();
        Timestamp sqlTimestamp = new Timestamp(date.getTime());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = formatter.format(sqlTimestamp);
        int rows = jdbcTemplate.update(SQL_INSERT_POST, op_name, mdContent, formattedDate);

        if (rows == 0)
            throw new RuntimeException("Invalid input");
        return getPostByOpAndTime(op_name , date);
    }

    @Override
    public Long getPostByOpAndTime(String userName, Date date) {
        if(userName == null || date == null)
            throw new NullPointerException("username or date is null");
        Timestamp sqlTimestamp = new Timestamp(date.getTime());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = formatter.format(sqlTimestamp);
        System.out.println(formattedDate);
        return jdbcTemplate.queryForObject(SQL_GET_POST_BY_USERNAME_AND_TIME,
                new Object[]{formattedDate, userName}, Long.class);
    }

    @Override
    public Post getPostByID(Long postId) {
        if (postId == null)
            throw new NullPointerException("postId is null");
        return jdbcTemplate.queryForObject(SQL_GET_POST_BY_ID, new Object[]{postId},postRowMapper);
    }

    @Override
    public Boolean insertQuestion(Long questionId, String title, Long communityId) {
        if(questionId == null || title == null || communityId == null)
            throw new NullPointerException("question id or title or community id is null");
        int rows = jdbcTemplate.update(SQL_INSERT_QUESTION, questionId, title, communityId);

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
        System.out.println("errr");
        jdbcTemplate.update(SQL_DELETE_ANSWERS_BY_QUESTION_ID, questionId);
        System.out.println("errr2");
        int rows = jdbcTemplate.update( SQL_DELETE_QUESTION_BY_ID , questionId);
        System.out.println("errr3");

        if (rows == 0)
            throw new RuntimeException("Invalid input");
        jdbcTemplate.update(SQL_DELETE_POST_BY_ID, questionId);
        System.out.println("errr4");
        return true;
    }

    @Override
    public Boolean deleteAnswer(Long answerId) {
        if(answerId == null)
            throw new NullPointerException("answer id is null");
        jdbcTemplate.update(SQL_DELETE_REPLIES_BY_ANSWER_ID, answerId);
        int rows = jdbcTemplate.update( SQL_DELETE_ANSWER_BY_ID , answerId);

        if (rows == 0)
            throw new RuntimeException("Invalid input");
        jdbcTemplate.update(SQL_DELETE_POST_BY_ID, answerId);
        return true;
    }

    @Override
    public Boolean deleteReply(Long replyId) {
        if(replyId == null)
            throw new NullPointerException("reply id is null");
        int rows = jdbcTemplate.update( SQL_DELETE_REPLY_BY_ID , replyId);

        if (rows == 0)
            throw new RuntimeException("Invalid input");
        jdbcTemplate.update(SQL_DELETE_POST_BY_ID, replyId);
        return true;
    }

    @Override
    public Boolean upVoteQuestion(Long questionId,Integer value , String userName) throws Exception {
        if (questionId == null || value == null || userName == null)
            throw new NullPointerException("question id or value is null");
        Integer count = jdbcTemplate.queryForObject(SQL_GET_UP_VOTE,
                new Object[]{ userName, questionId }, Integer.class);
        count = (count == null) ? 1 : count;
        if(value == 1) {
            if (count == 1)
                throw new Exception("user already up voted this question");
            if (is_DownVoted(userName , questionId)) {
                jdbcTemplate.update(SQL_UPDATE_DOWN_VOTES_QUESTIONS,  -1, questionId);
            }
            jdbcTemplate.update(SQL_DELETE_DOWN_VOTE ,userName , questionId);
            jdbcTemplate.update(SQL_INSERT_UPVOTE, userName, questionId);
        }else{
            if (count == 0)
                throw new Exception("user didn't up vote this question before");

            jdbcTemplate.update(SQL_DELETE_UP_VOTE, userName, questionId);
        }
        int rows = jdbcTemplate.update( SQL_UPDATE_UP_VOTES_QUESTIONS ,value, questionId);

        if (rows == 0)
            throw new RuntimeException("Invalid input");
        return true;
    }

    @Override
    public Boolean downVoteQuestion(Long questionId, Integer value , String userName) throws Exception {
        if(questionId == null || value == null)
            throw new NullPointerException("question id or value is null");

        Integer count = jdbcTemplate.queryForObject(SQL_GET_DOWN_VOTE,
                new Object[]{ userName, questionId }, Integer.class);
        count = (count == null) ? 1 : count;
        if(value == 1) {
            if (count == 1)
                throw new Exception("user already down voted this question");
            if (is_UpVoted(userName , questionId)) {
                jdbcTemplate.update(SQL_UPDATE_UP_VOTES_QUESTIONS,  -1, questionId);
            }
            jdbcTemplate.update(SQL_DELETE_UP_VOTE, userName, questionId);
            jdbcTemplate.update(SQL_INSERT_DOWNVOTE, userName, questionId);
        }else{
            if (count == 0)
                throw new Exception("user didn't down vote this question before");

            jdbcTemplate.update(SQL_DELETE_DOWN_VOTE, userName, questionId);
        }
        int rows = jdbcTemplate.update( SQL_UPDATE_DOWN_VOTES_QUESTIONS ,value ,questionId);

        if (rows == 0)
            throw new RuntimeException("Invalid input");
        return true;
    }

    @Override
    public Boolean upVoteAnswer(Long answerId, Integer value , String userName) throws Exception {
        if (answerId == null)
            throw new NullPointerException("answer id or value is null");
        Integer count = jdbcTemplate.queryForObject(SQL_GET_UP_VOTE,
                new Object[]{ userName, answerId}, Integer.class);
        if(value == 1) {
            if (count == 1)
                throw new Exception("user already up voted this answer");
            if (is_DownVoted(userName , answerId)) {
                jdbcTemplate.update(SQL_UPDATE_DOWN_VOTES_ANSWERS,  -1, answerId);
            }

            jdbcTemplate.update(SQL_DELETE_DOWN_VOTE, userName, answerId);
            jdbcTemplate.update(SQL_INSERT_UPVOTE, userName, answerId);
        }else{
            if (count == 0)
                throw new Exception("user didn't up vote this answer before");

            jdbcTemplate.update(SQL_DELETE_UP_VOTE, userName, answerId);
        }

        int rows = jdbcTemplate.update( SQL_UPDATE_UP_VOTES_ANSWERS ,value ,answerId);

        if (rows == 0)
            throw new RuntimeException("Invalid input");
        return true;
    }

    @Override
    public Boolean downVoteAnswer(Long answerId, Integer value , String userName) throws Exception {
        if (answerId == null)
            throw new NullPointerException("answer id or value is null");
        Integer count = jdbcTemplate.queryForObject(SQL_GET_DOWN_VOTE,
                new Object[]{ userName, answerId }, Integer.class);
        if(value == 1) {
            if (count == 1)
                throw new Exception("user already down voted this answer");
            if (is_UpVoted(userName , answerId)) {
                jdbcTemplate.update(SQL_UPDATE_UP_VOTES_ANSWERS,  -1, answerId);
            }

            jdbcTemplate.update(SQL_DELETE_UP_VOTE, userName, answerId);
            jdbcTemplate.update(SQL_INSERT_DOWNVOTE, userName, answerId);
        }else{
            if (count == 0)
                throw new Exception("user didn't up vote this answer before");

            jdbcTemplate.update(SQL_DELETE_DOWN_VOTE, userName, answerId);
        }

        int rows = jdbcTemplate.update( SQL_UPDATE_DOWN_VOTES_ANSWERS ,value ,answerId);

        if (rows == 0)
            throw new RuntimeException("Invalid input");
        return true;
    }

    @Override
    public Boolean verifyAnswer(Long answerId, Long questionId) {
        if(answerId == null)
            throw new NullPointerException("answer id is null");

        int rows = jdbcTemplate.update( SQL_VERIFY_ANSWER, answerId, questionId);

        if (rows == 0)
            throw new RuntimeException("Invalid input");
        return true;
    }

    @Override
    public Boolean editPost(Long postId, String mdContent) {
        if (postId == null || mdContent == null)
            throw new NullPointerException("post id or md content is null");
        int rows = jdbcTemplate.update( SQL_EDIT_POST, mdContent, postId);

        if (rows == 0)
            throw new RuntimeException("Invalid input");
        return true;
    }

    @Override
    public List<Question> getQuestionsByUserName(String userName, Integer limit, Integer offset) {
        if(userName == null)
            throw new NullPointerException("username is null");
        return jdbcTemplate.query(SQL_GET_QUESTIONS_BY_USERNAME,new Object[]{userName, limit, offset},questionRowMapper);
    }

    @Override
    public List<Answer> getAnswersByUserName(String userName, Integer limit, Integer offset) {
        if(userName == null)
            throw new NullPointerException("username is null");
        return jdbcTemplate.query(SQL_GET_ANSWERS_BY_USERNAME,new Object[]{userName, limit, offset},answerRowMapper);
    }

    @Override
    public List<Reply> getRepliesByUserName(String userName, Integer limit, Integer offset) {
        if(userName == null)
            throw new NullPointerException("username is null");
        return jdbcTemplate.query(SQL_GET_REPLIES_BY_USERNAME,new Object[]{userName, limit, offset},replyRowMapper);
    }

    @Override
    public List<Answer> getAnswersForQuestion(Long questionId, Integer limit, Integer offset) {
        if(questionId == null)
            throw new NullPointerException("username is null");
        return jdbcTemplate.query(SQL_GET_ANSWERS_FOR_QUESTION,new Object[]{questionId, limit, offset},answerRowMapper);
    }

    @Override
    public List<Reply> getRepliesForAnswer(Long answerId, Integer limit, Integer offset) {
        if(answerId == null)
            throw new NullPointerException("username is null");
        return jdbcTemplate.query(SQL_GET_REPLIES_FOR_ANSWER,new Object[]{answerId, limit, offset},replyRowMapper);
    }

    @Override
    public List<Question> getQuestionsByCommunity(Long communityId, Integer limit, Integer offset) {
        if(communityId == null)
            throw new NullPointerException("username is null");
        return jdbcTemplate.query(SQL_GET_QUESTIONS_BY_COMMUNITY,new Object[]{communityId, limit, offset},questionRowMapper);
    }

    @Override
    public Question getQuestionById(Long questionId) {
        if (questionId == null)
            throw new NullPointerException("questionId is null");
        return jdbcTemplate.queryForObject(SQL_GET_QUESTION_BY_ID, new Object[]{questionId}, questionRowMapper);
    }

    @Override
    public Answer getAnswerById(Long answerId) {
        if (answerId == null)
            throw new NullPointerException("questionId is null");
        return jdbcTemplate.queryForObject(SQL_GET_ANSWER_BY_ID, new Object[]{answerId}, answerRowMapper);
    }

    @Override
    public Reply getReplyById(Long replyId) {
        if (replyId == null)
            throw new NullPointerException("questionId is null");
        return jdbcTemplate.queryForObject(SQL_GET_REPLY_BY_ID, new Object[]{replyId}, replyRowMapper);
    }
    public boolean is_UpVoted(String userName , long questionId){
        Integer count = jdbcTemplate.queryForObject(SQL_GET_UP_VOTE,
                new Object[]{ userName, questionId }, Integer.class);
        return (count == 1);
    }
    public boolean is_DownVoted(String userName , long questionId){
        Integer count = jdbcTemplate.queryForObject(SQL_GET_DOWN_VOTE,
                new Object[]{ userName, questionId }, Integer.class);
        return (count == 1);
    }

    private final RowMapper<Question> questionRowMapper = ((rs, rowNum) ->
            Question.builder()
                    .id(rs.getLong("id"))
                    .creatorUserName(rs.getString("op_name"))
                    .mdContent(rs.getString("md_content"))
                    .postedOn(new Date(rs.getTimestamp("posted_on").getTime()))
                    .title(rs.getString("title"))
                    .communityId(rs.getLong("community_id"))
                    .upVotes(rs.getLong("up_votes"))
                    .downVotes(rs.getLong("down_votes"))
                    .validatedAnswerId(rs.getLong("validated_answer_id"))
                    .build()
    );

    private final RowMapper<Answer> answerRowMapper = ((rs, rowNum) ->
            Answer.builder()
                    .id(rs.getLong("id"))
                    .creatorUserName(rs.getString("op_name"))
                    .mdContent(rs.getString("md_content"))
                    .postedOn(new Date(rs.getTimestamp("posted_on").getTime()))
                    .questionId(rs.getLong("question_id"))
                    .upVotes(rs.getLong("up_votes"))
                    .downVotes(rs.getLong("down_votes"))
                    .build()
    );

    private final RowMapper<Reply> replyRowMapper = ((rs, rowNum) ->
            Reply.builder()
                    .id(rs.getLong("id"))
                    .creatorUserName(rs.getString("op_name"))
                    .mdContent(rs.getString("md_content"))
                    .postedOn(new Date(rs.getTimestamp("posted_on").getTime()))
                    .answerId(rs.getLong("answer_id"))
                    .build()
    );

    private final RowMapper<Post> postRowMapper = ((rs, rowNum) -> new Post(
            rs.getLong("id"),
            rs.getString("op_name"),
            rs.getString("md_content"),
            new Date(rs.getTimestamp("posted_on").getTime())
    ));
}
