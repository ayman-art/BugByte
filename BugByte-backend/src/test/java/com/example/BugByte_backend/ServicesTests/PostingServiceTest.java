package com.example.BugByte_backend.ServicesTests;

import com.example.BugByte_backend.models.*;
import com.example.BugByte_backend.repositories.CommunityRepository;
import com.example.BugByte_backend.repositories.PostingRepository;
import com.example.BugByte_backend.repositories.UserRepositoryImp;
import com.example.BugByte_backend.services.PostingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class PostingServiceTest {
    @Mock
    PostingRepository postingRepositoryMock;

    @Mock
    CommunityRepository communityRepositoryMock;
    @Mock
    UserRepositoryImp userRepositoryImpMock;
    @InjectMocks
    PostingService postingService;
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void testGetPost_Success() throws Exception {
        long postId = 1L;
        Post mockPost = new Post(postId , "user1" , "hello" , new Date());
        when(postingRepositoryMock.getPostByID(postId)).thenReturn(mockPost);

        Post result = postingService.getPost(postId);

        assertNotNull(result);
        assertEquals(mockPost, result);
    }
    @Test
    void testGetPost_NullPost() {
        long postId = 1L;
        when(postingRepositoryMock.getPostByID(postId)).thenReturn(null);
        Exception exception = assertThrows(Exception.class, () -> {
            postingService.getPost(postId);
        });

        assertEquals("post is null", exception.getMessage());
    }
    @Test
    void testPostQuestion_Success() throws Exception {
        Question mockQuestion = new Question();
        mockQuestion.setMdContent("Test content");
        mockQuestion.setCreatorUserName("TestUser");
        mockQuestion.setCommunityId(101L);

        long generatedPostId = 1L;

        when(postingRepositoryMock.insertPost(mockQuestion.getMdContent(), mockQuestion.getCreatorUserName()))
                .thenReturn(generatedPostId);
        when(postingRepositoryMock.insertQuestion(generatedPostId, mockQuestion.getCommunityId()))
                .thenReturn(true);

        long result = postingService.postQuestion(mockQuestion);

        assertEquals(generatedPostId, result);
    }

    @Test
    void testPostQuestion_NullPostId() {
        Question mockQuestion = new Question();
        mockQuestion.setMdContent("Test content");
        mockQuestion.setCreatorUserName("TestUser");
        mockQuestion.setCommunityId(101L);

        when(postingRepositoryMock.insertPost(mockQuestion.getMdContent(), mockQuestion.getCreatorUserName()))
                .thenReturn(null);

        Exception exception = assertThrows(Exception.class, () -> {
            postingService.postQuestion(mockQuestion);
        });
        assertEquals("post id is null", exception.getMessage());
    }

    @Test
    void testPostQuestion_InsertQuestionFails() {
        Question mockQuestion = new Question();
        mockQuestion.setMdContent("Test content");
        mockQuestion.setCreatorUserName("TestUser");
        mockQuestion.setCommunityId(101L);

        long generatedPostId = 1L;

        when(postingRepositoryMock.insertPost(mockQuestion.getMdContent(), mockQuestion.getCreatorUserName()))
                .thenReturn(generatedPostId);
        when(postingRepositoryMock.insertQuestion(generatedPostId, mockQuestion.getCommunityId()))
                .thenReturn(false);

        Exception exception = assertThrows(Exception.class, () -> {
            postingService.postQuestion(mockQuestion);
        });
        assertEquals("error inserting a question", exception.getMessage());
    }
    @Test
    void testPostAnswer_Success() throws Exception {
        Answer mockAnswer = new Answer();
        mockAnswer.setMdContent("Test content");
        mockAnswer.setCreatorUserName("TestUser");
        mockAnswer.setQuestionId(101L);

        long generatedPostId = 1L;

        when(postingRepositoryMock.insertPost(mockAnswer.getMdContent(), mockAnswer.getCreatorUserName()))
                .thenReturn(generatedPostId);
        when(postingRepositoryMock.insertAnswer(generatedPostId, mockAnswer.getQuestionId()))
                .thenReturn(true);

        long result = postingService.postAnswer(mockAnswer);

        assertEquals(generatedPostId, result);
    }

    @Test
    void testPostAnswer_NullPostId() {
        Answer mockAnswer = new Answer();
        mockAnswer.setMdContent("Test content");
        mockAnswer.setCreatorUserName("TestUser");
        mockAnswer.setQuestionId(101L);

        when(postingRepositoryMock.insertPost(mockAnswer.getMdContent(), mockAnswer.getCreatorUserName()))
                .thenReturn(null);

        Exception exception = assertThrows(Exception.class, () -> {
            postingService.postAnswer(mockAnswer);
        });
        assertEquals("post id is null", exception.getMessage());
    }
    @Test
    void testPostReply_Success() throws Exception {
        Reply mockReply = new Reply();
        mockReply.setMdContent("Test content");
        mockReply.setCreatorUserName("TestUser");
        mockReply.setAnswerId(101L);

        long generatedPostId = 1L;

        when(postingRepositoryMock.insertPost(mockReply.getMdContent(), mockReply.getCreatorUserName()))
                .thenReturn(generatedPostId);
        when(postingRepositoryMock.insertReply(generatedPostId, mockReply.getAnswerId()))
                .thenReturn(true);

        long result = postingService.postReply(mockReply);

        assertEquals(generatedPostId, result);
    }

    @Test
    void testPostReply_NullPostId() {
        Reply mockReply = new Reply();
        mockReply.setMdContent("Test content");
        mockReply.setCreatorUserName("TestUser");
        mockReply.setAnswerId(101L);

        when(postingRepositoryMock.insertPost(mockReply.getMdContent(), mockReply.getCreatorUserName()))
                .thenReturn(null);

        Exception exception = assertThrows(Exception.class, () -> {
            postingService.postReply(mockReply);
        });
        assertEquals("post id is null", exception.getMessage());
    }

    @Test
    void testPostReply_InsertReplyFails() {
        Reply mockReply = new Reply();
        mockReply.setMdContent("Test content");
        mockReply.setCreatorUserName("TestUser");
        mockReply.setAnswerId(101L);

        long generatedPostId = 1L;

        when(postingRepositoryMock.insertPost(mockReply.getMdContent(), mockReply.getCreatorUserName()))
                .thenReturn(generatedPostId);
        when(postingRepositoryMock.insertReply(generatedPostId, mockReply.getAnswerId()))
                .thenReturn(false);

        Exception exception = assertThrows(Exception.class, () -> {
            postingService.postReply(mockReply);
        });
        assertEquals("error inserting a reply", exception.getMessage());
    }
    @Test
    void testDeleteQuestion_Success() throws Exception {
        long questionId = 101L;

        when(postingRepositoryMock.deleteQuestion(questionId)).thenReturn(true);

        boolean result = postingService.deleteQuestion(questionId);

        assertTrue(result);
    }

    @Test
    void testDeleteQuestion_Failure() throws Exception {
        long questionId = 101L;

        when(postingRepositoryMock.deleteQuestion(questionId)).thenReturn(false);

        boolean result = postingService.deleteQuestion(questionId);

        assertFalse(result);
    }

    @Test
    void testDeleteAnswer_Success() throws Exception {
        long answerId = 202L;

        when(postingRepositoryMock.deleteAnswer(answerId)).thenReturn(true);

        boolean result = postingService.deleteAnswer(answerId);

        assertTrue(result);
    }

    @Test
    void testDeleteAnswer_Failure() throws Exception {
        long answerId = 202L;

        when(postingRepositoryMock.deleteAnswer(answerId)).thenReturn(false);

        boolean result = postingService.deleteAnswer(answerId);

        assertFalse(result);
    }
    @Test
    void testDeleteReply_Success() throws Exception {
        long replyId = 303L;

        when(postingRepositoryMock.deleteReply(replyId)).thenReturn(true);

        boolean result = postingService.deleteReply(replyId);

        assertTrue(result);
    }

    @Test
    void testDeleteReply_Failure() throws Exception {
        long replyId = 303L;

        when(postingRepositoryMock.deleteReply(replyId)).thenReturn(false);

        boolean result = postingService.deleteReply(replyId);

        assertFalse(result);
    }
    @Test
    void testUpVoteQuestion_Success() throws Exception {
        long questionId = 404L;

        when(postingRepositoryMock.upVoteQuestion(questionId, 1)).thenReturn(true);

        boolean result = postingService.upVoteQuestion(questionId);

        assertTrue(result);
    }

    @Test
    void testUpVoteQuestion_Failure() throws Exception {
        long questionId = 404L;

        when(postingRepositoryMock.upVoteQuestion(questionId, 1)).thenReturn(false);

        boolean result = postingService.upVoteQuestion(questionId);

        assertFalse(result);
    }
    @Test
    void testRemoveUpVoteFromQuestion_Success() throws Exception {
        long questionId = 505L;

        when(postingRepositoryMock.upVoteQuestion(questionId, -1)).thenReturn(true);

        boolean result = postingService.removeUpVoteFromQuestion(questionId);

        assertTrue(result);
    }

    @Test
    void testRemoveUpVoteFromQuestion_Failure() throws Exception {
        long questionId = 505L;

        when(postingRepositoryMock.upVoteQuestion(questionId, -1)).thenReturn(false);

        boolean result = postingService.removeUpVoteFromQuestion(questionId);

        assertFalse(result);
    }
    @Test
    void testDownVoteQuestion_Success() throws Exception {
        long questionId = 606L;

        when(postingRepositoryMock.downVoteQuestion(questionId, 1)).thenReturn(true);

        boolean result = postingService.downVoteQuestion(questionId);

        assertTrue(result);
    }

    @Test
    void testDownVoteQuestion_Failure() throws Exception {
        long questionId = 606L;

        when(postingRepositoryMock.downVoteQuestion(questionId, 1)).thenReturn(false);

        boolean result = postingService.downVoteQuestion(questionId);

        assertFalse(result);
    }
    @Test
    void testRemoveDownVoteFromQuestion_Success() throws Exception {
        long questionId = 707L;

        when(postingRepositoryMock.downVoteQuestion(questionId, -1)).thenReturn(true);

        boolean result = postingService.removeDownVoteFromQuestion(questionId);

        assertTrue(result);
    }

    @Test
    void testRemoveDownVoteFromQuestion_Failure() throws Exception {
        long questionId = 707L;

        when(postingRepositoryMock.downVoteQuestion(questionId, -1)).thenReturn(false);

        boolean result = postingService.removeDownVoteFromQuestion(questionId);

        assertFalse(result);
    }
    @Test
    void testUpVoteAnswer_Success() throws Exception {
        long answerId = 808L;

        when(postingRepositoryMock.upVoteAnswer(answerId, 1)).thenReturn(true);

        boolean result = postingService.upVoteAnswer(answerId);

        assertTrue(result);
    }

    @Test
    void testUpVoteAnswer_Failure() throws Exception {
        long answerId = 808L;

        when(postingRepositoryMock.upVoteAnswer(answerId, 1)).thenReturn(false);

        boolean result = postingService.upVoteAnswer(answerId);

        assertFalse(result);
    }
    @Test
    void testRemoveUpFromVoteAnswer_Success() throws Exception {
        long answerId = 909L;

        when(postingRepositoryMock.upVoteAnswer(answerId, -1)).thenReturn(true);

        boolean result = postingService.removeUpFromVoteAnswer(answerId);

        assertTrue(result);
    }

    @Test
    void testRemoveUpFromVoteAnswer_Failure() throws Exception {
        long answerId = 909L;

        when(postingRepositoryMock.upVoteAnswer(answerId, -1)).thenReturn(false);

        boolean result = postingService.removeUpFromVoteAnswer(answerId);

        assertFalse(result);
    }

    @Test
    void testDownVoteAnswer_Success() throws Exception {
        long answerId = 1010L;

        when(postingRepositoryMock.downVoteAnswer(answerId, 1)).thenReturn(true);

        boolean result = postingService.downVoteAnswer(answerId);

        assertTrue(result);
    }

    @Test
    void testDownVoteAnswer_Failure() throws Exception {
        long answerId = 1010L;

        when(postingRepositoryMock.downVoteAnswer(answerId, 1)).thenReturn(false);

        boolean result = postingService.downVoteAnswer(answerId);

        assertFalse(result);
    }

    @Test
    void testRemoveDownVoteAnswer_Success() throws Exception {
        long answerId = 1111L;

        when(postingRepositoryMock.downVoteAnswer(answerId, -1)).thenReturn(true);

        boolean result = postingService.removeDownVoteAnswer(answerId);

        assertTrue(result);
    }

    @Test
    void testRemoveDownVoteAnswer_Failure() throws Exception {
        long answerId = 1111L;

        when(postingRepositoryMock.downVoteAnswer(answerId, -1)).thenReturn(false);

        boolean result = postingService.removeDownVoteAnswer(answerId);

        assertFalse(result);
    }

    @Test
    void testVerifyAnswer_Success() throws Exception {
        long answerId = 1212L;

        when(postingRepositoryMock.verifyAnswer(answerId)).thenReturn(true);

        boolean result = postingService.verifyAnswer(answerId);

        assertTrue(result);
    }

    @Test
    void testVerifyAnswer_Failure() throws Exception {
        long answerId = 1212L;

        when(postingRepositoryMock.verifyAnswer(answerId)).thenReturn(false);

        boolean result = postingService.verifyAnswer(answerId);

        assertFalse(result);
    }
    @Test
    void testEditPost_Success() throws Exception {
        long postId = 1313L;
        String mdContent = "Updated content";

        when(postingRepositoryMock.editPost(postId, mdContent)).thenReturn(true);

        boolean result = postingService.editPost(postId, mdContent);

        assertTrue(result);
    }

    @Test
    void testEditPost_Failure() throws Exception {
        long postId = 1313L;
        String mdContent = "Updated content";

        when(postingRepositoryMock.editPost(postId, mdContent)).thenReturn(false);

        boolean result = postingService.editPost(postId, mdContent);

        assertFalse(result);
    }
    @Test
    void testGetCommunityQuestions_Success() throws Exception {
        long communityId = 1L;
        int limit = 5;
        int offset = 0;
        Community mockCommunity = new Community();
        List<Question> mockQuestions = Arrays.asList(new Question(), new Question());

        when(communityRepositoryMock.findCommunityById(communityId)).thenReturn(mockCommunity);
        when(postingRepositoryMock.getQuestionsByCommunity(communityId, limit, offset)).thenReturn(mockQuestions);

        List<Question> result = postingService.getCommunityQuestions(communityId, limit, offset);

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testGetCommunityQuestions_CommunityNotFound() {
        long communityId = 1L;
        int limit = 5;
        int offset = 0;
        when(communityRepositoryMock.findCommunityById(communityId)).thenReturn(null);

        Exception exception = assertThrows(Exception.class, () ->
                postingService.getCommunityQuestions(communityId, limit, offset));

        assertEquals("Community is null", exception.getMessage());
    }
    @Test
    void testGetUserQuestions_Success() throws Exception {
        String userName = "user1";
        int limit = 5;
        int offset = 0;
        User mockUser = new User();
        List<Question> mockQuestions = Arrays.asList(new Question(), new Question());

        when(userRepositoryImpMock.findByIdentity(userName)).thenReturn(mockUser);
        when(postingRepositoryMock.getQuestionsByUserName(userName, limit, offset)).thenReturn(mockQuestions);

        List<Question> result = postingService.getUserQuestions(userName, limit, offset);

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testGetUserQuestions_UserNotFound() {
        String userName = "user1";
        int limit = 5;
        int offset = 0;

        when(userRepositoryImpMock.findByIdentity(userName)).thenReturn(null);

        Exception exception = assertThrows(Exception.class, () ->
                postingService.getUserQuestions(userName, limit, offset));

        assertEquals("user is null", exception.getMessage());
    }
    @Test
    void testGetUserAnswers_Success() throws Exception {
        String userName = "user1";
        int limit = 5;
        int offset = 0;
        User mockUser = new User();
        List<Answer> mockAnswers = Arrays.asList(new Answer(), new Answer());

        when(userRepositoryImpMock.findByIdentity(userName)).thenReturn(mockUser);
        when(postingRepositoryMock.getAnswersByUserName(userName, limit, offset)).thenReturn(mockAnswers);

        List<Answer> result = postingService.getUserAnswers(userName, limit, offset);

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testGetUserAnswers_UserNotFound() {
        String userName = "user1";
        int limit = 5;
        int offset = 0;

        when(userRepositoryImpMock.findByIdentity(userName)).thenReturn(null);

        Exception exception = assertThrows(Exception.class, () ->
                postingService.getUserAnswers(userName, limit, offset));

        assertEquals("user is null", exception.getMessage());
    }
    @Test
    void testGetUserReplies_Success() throws Exception {
        String userName = "user1";
        int limit = 5;
        int offset = 0;
        User mockUser = new User();
        List<Reply> mockReplies = Arrays.asList(new Reply(), new Reply());

        when(userRepositoryImpMock.findByIdentity(userName)).thenReturn(mockUser);
        when(postingRepositoryMock.getRepliesByUserName(userName, limit, offset)).thenReturn(mockReplies);

        List<Reply> result = postingService.getUserReplies(userName, limit, offset);

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testGetUserReplies_UserNotFound() {
        String userName = "user1";
        int limit = 5;
        int offset = 0;

        when(userRepositoryImpMock.findByIdentity(userName)).thenReturn(null);

        Exception exception = assertThrows(Exception.class, () ->
                postingService.getUserReplies(userName, limit, offset));

        assertEquals("user is null", exception.getMessage());
    }
    @Test
    void testGetAnswersForQuestion_Success() throws Exception {
        long questionId = 1L;
        int limit = 5;
        int offset = 0;
        Question mockQuestion = new Question();
        List<Answer> mockAnswers = Arrays.asList(new Answer(), new Answer());

        when(postingRepositoryMock.getQuestionById(questionId)).thenReturn(mockQuestion);
        when(postingRepositoryMock.getAnswersForQuestion(questionId, limit, offset)).thenReturn(mockAnswers);

        List<Answer> result = postingService.getAnswersForQuestion(questionId, limit, offset);

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testGetAnswersForQuestion_QuestionNotFound() {
        long questionId = 1L;
        int limit = 5;
        int offset = 0;

        when(postingRepositoryMock.getQuestionById(questionId)).thenReturn(null);

        Exception exception = assertThrows(Exception.class, () ->
                postingService.getAnswersForQuestion(questionId, limit, offset));

        assertEquals("question is null", exception.getMessage());
    }
    @Test
    void testGetRepliesForAnswer_Success() throws Exception {
        long answerId = 1L;
        int limit = 5;
        int offset = 0;
        Answer mockAnswer = new Answer();
        List<Reply> mockReplies = Arrays.asList(new Reply(), new Reply());

        when(postingRepositoryMock.getAnswerById(answerId)).thenReturn(mockAnswer);
        when(postingRepositoryMock.getRepliesForAnswer(answerId, limit, offset)).thenReturn(mockReplies);

        List<Reply> result = postingService.getRepliesForAnswer(answerId, limit, offset);

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testGetRepliesForAnswer_AnswerNotFound() {
        long answerId = 1L;
        int limit = 5;
        int offset = 0;

        when(postingRepositoryMock.getAnswerById(answerId)).thenReturn(null);

        Exception exception = assertThrows(Exception.class, () ->
                postingService.getRepliesForAnswer(answerId, limit, offset));

        assertEquals("answer is null", exception.getMessage());
    }
}
