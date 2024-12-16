package com.example.BugByte_backend.controllers;
import com.example.BugByte_backend.facades.InteractionFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
@RestController()
@RequestMapping("/posts")
public class PostingController {
         @Autowired
     private InteractionFacade interactionFacade;
    @PostMapping("questions")
    public ResponseEntity<?> postQuestion(@RequestHeader("Authorization") String token, @RequestParam("communityId") Long communityId, @RequestBody Map<String, Object> question) {
        token = token.replace("Bearer ", "");
        question.put("jwt", token);
        question.put("communityId", communityId);
        try {
            return new ResponseEntity<>(interactionFacade.postQuestion(question), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
    @PostMapping("answers")
    public ResponseEntity<?> postAnswer(@RequestHeader("Authorization") String token, @RequestParam("questionId") Long questionId, @RequestBody Map<String, Object> answer) {
        token = token.replace("Bearer ", "");
        answer.put("jwt", token);
        answer.put("questionId", questionId);
        try {
            return new ResponseEntity<>(interactionFacade.postAnswer(answer), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
    @PostMapping("replies")
    public ResponseEntity<?> postReply(@RequestHeader("Authorization") String token, @RequestParam("answerId") Long answerId, @RequestBody Map<String, Object> reply) {
        token = token.replace("Bearer ", "");
        reply.put("jwt", token);
        reply.put("answerId", answerId);
        try {
            return new ResponseEntity<>(interactionFacade.postReply(reply), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
    @GetMapping("questions")
    public ResponseEntity<?> getQuestions(@RequestHeader("Authorization") String token, @RequestParam("questionId") Long questionId) {
        token = token.replace("Bearer ", "");
        Map<String, Object> questionData = Map.of("jwt", token, "questionId", questionId);
        try {
            return new ResponseEntity<>(interactionFacade.getQuestion(questionData), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
    @GetMapping("answers")
    public ResponseEntity<?> getAnswers(@RequestHeader("Authorization") String token, @RequestParam("questionId") Long questionId,
                                        @RequestParam(value = "offset", defaultValue = "0") int offset, @RequestParam(value = "limit", defaultValue = "10") int limit) {
        token = token.replace("Bearer ", "");
        Map<String, Object> answerData = Map.of("jwt", token, "questionId", questionId, "offset", offset, "limit", limit);
        try {
            return new ResponseEntity<>(interactionFacade.getAnswersForQuestion(answerData), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
    @GetMapping("replies")
    public ResponseEntity<?> getReplies(@RequestHeader("Authorization") String token, @RequestParam("answerId") Long answerId,
                                        @RequestParam(value = "offset", defaultValue = "0") int offset, @RequestParam(value = "limit", defaultValue = "10") int limit) {
        token = token.replace("Bearer ", "");
        Map<String, Object> replyData = Map.of("jwt", token, "answerId", answerId, "offset", offset, "limit", limit);
        try {
            return new ResponseEntity<>(interactionFacade.getRepliesForAnswer(replyData), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
    @PutMapping("questions")
    public ResponseEntity<?> updateQuestion(@RequestHeader("Authorization") String token, @RequestParam("questionId") Long questionId, @RequestBody Map<String, Object> question) {
        token = token.replace("Bearer ", "");
        question.put("jwt", token);
        question.put("questionId", questionId);
        try {
            return new ResponseEntity<>(interactionFacade.editPost(question), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
    @PutMapping("answers")
    public ResponseEntity<?> updateAnswer(@RequestHeader("Authorization") String token, @RequestParam("answerId") Long answerId, @RequestBody Map<String, Object> answer) {
        token = token.replace("Bearer ", "");
        answer.put("jwt", token);
        answer.put("answerId", answerId);
        try {
            return new ResponseEntity<>(interactionFacade.editPost(answer), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
    @PutMapping("replies")
    public ResponseEntity<?> updateReply(@RequestHeader("Authorization") String token, @RequestParam("replyId") Long replyId, @RequestBody Map<String, Object> reply) {
        token = token.replace("Bearer ", "");
        reply.put("jwt", token);
        reply.put("replyId", replyId);
        try {
            return new ResponseEntity<>(interactionFacade.editPost(reply), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
    @DeleteMapping("questions")
    public ResponseEntity<?> deleteQuestion(@RequestHeader("Authorization") String token, @RequestParam("questionId") Long questionId) {
        token = token.replace("Bearer ", "");
        Map<String, Object> questionData = Map.of("jwt", token, "questionId", questionId);
        try {
            return new ResponseEntity<>(interactionFacade.deleteQuestion(questionData), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
    @DeleteMapping("answers")
    public ResponseEntity<?> deleteAnswer(@RequestHeader("Authorization") String token, @RequestParam("answerId") Long answerId) {
        token = token.replace("Bearer ", "");
        Map<String, Object> answerData = Map.of("jwt", token, "answerId", answerId);
        try {
            return new ResponseEntity<>(interactionFacade.deleteAnswer(answerData), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
    @DeleteMapping("replies")
    public ResponseEntity<?> deleteReply(@RequestHeader("Authorization") String token, @RequestParam("replyId") Long replyId) {
        token = token.replace("Bearer ", "");
        Map<String, Object> replyData = Map.of("jwt", token, "replyId", replyId);
        try {
            return new ResponseEntity<>(interactionFacade.deleteReply(replyData), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
    @PutMapping("upvoteQuestion")
    public ResponseEntity<?> upvoteQuestion(@RequestHeader("Authorization") String token, @RequestParam("postId") Long postId) {
        token = token.replace("Bearer ", "");
        Map<String, Object> postData = Map.of("jwt", token, "questionId", postId);

        try {
            interactionFacade.upVoteQuestion(postData);
            return new ResponseEntity<>(Map.of("message", "Upvoted question successfully"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @PutMapping("removeUpvoteQuestion")
    public ResponseEntity<?> removeUpvoteQuestion(@RequestHeader("Authorization") String token, @RequestParam("postId") Long postId) {
        token = token.replace("Bearer ", "");
        Map<String, Object> postData = Map.of("jwt", token, "questionId", postId);

        try {
            interactionFacade.removeUpVoteQuestion(postData);
            return new ResponseEntity<>(Map.of("message", "Upvote removed from question successfully"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @PutMapping("downvoteQuestion")
    public ResponseEntity<?> downvoteQuestion(@RequestHeader("Authorization") String token, @RequestParam("postId") Long postId) {
        token = token.replace("Bearer ", "");
        Map<String, Object> postData = Map.of("jwt", token, "questionId", postId);

        try {
            interactionFacade.downVoteQuestion(postData);
            return new ResponseEntity<>(Map.of("message", "Downvoted question successfully"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @PutMapping("removeDownvoteQuestion")
    public ResponseEntity<?> removeDownvoteQuestion(@RequestHeader("Authorization") String token, @RequestParam("postId") Long postId) {
        token = token.replace("Bearer ", "");
        Map<String, Object> postData = Map.of("jwt", token, "questionId", postId);

        try {
            interactionFacade.removeDownVoteQuestion(postData);
            return new ResponseEntity<>(Map.of("message", "Downvote removed from question successfully"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @PutMapping("upvoteAnswer")
    public ResponseEntity<?> upvoteAnswer(@RequestHeader("Authorization") String token, @RequestParam("postId") Long postId) {
        token = token.replace("Bearer ", "");
        Map<String, Object> postData = Map.of("jwt", token, "answerId", postId);

        try {
            interactionFacade.upVoteAnswer(postData);
            return new ResponseEntity<>(Map.of("message", "Upvoted answer successfully"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @PutMapping("removeUpvoteAnswer")
    public ResponseEntity<?> removeUpvoteAnswer(@RequestHeader("Authorization") String token, @RequestParam("postId") Long postId) {
        token = token.replace("Bearer ", "");
        Map<String, Object> postData = Map.of("jwt", token, "answerId", postId);

        try {
            interactionFacade.removeUpVoteAnswer(postData);
            return new ResponseEntity<>(Map.of("message", "Upvote removed from answer successfully"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @PutMapping("downvoteAnswer")
    public ResponseEntity<?> downvoteAnswer(@RequestHeader("Authorization") String token, @RequestParam("postId") Long postId) {
        token = token.replace("Bearer ", "");
        Map<String, Object> postData = Map.of("jwt", token, "answerId", postId);

        try {
            interactionFacade.downVoteAnswer(postData);
            return new ResponseEntity<>(Map.of("message", "Downvoted answer successfully"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @PutMapping("removeDownvoteAnswer")
    public ResponseEntity<?> removeDownvoteAnswer(@RequestHeader("Authorization") String token, @RequestParam("postId") Long postId) {
        token = token.replace("Bearer ", "");
        Map<String, Object> postData = Map.of("jwt", token, "answerId", postId);

        try {
            interactionFacade.removeDownVoteAnswer(postData);
            return new ResponseEntity<>(Map.of("message", "Downvote removed from answer successfully"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }



    @PostMapping("answers/verify")
    public ResponseEntity<?> verifyAnswer(@RequestHeader("Authorization") String token, @RequestParam("answerId") Long answerId) {
        token = token.replace("Bearer ", "");
        Map<String, Object> answerData = Map.of("jwt", token, "answerId", answerId);
        try {
            return new ResponseEntity<>(interactionFacade.verifyAnswer(answerData), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("answers/{answerId}")
    public ResponseEntity<?> getAnswer(@RequestHeader("Authorization") String token, @PathVariable("answerId") Long answerId) {
        token = token.replace("Bearer ", "");
        Map<String, Object> answerData = Map.of("jwt", token, "answerId", answerId);
        try {
            return new ResponseEntity<>(interactionFacade.getAnswer(answerData), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("replies/{replyId}")
    public ResponseEntity<?> getReply(@RequestHeader("Authorization") String token, @PathVariable("replyId") Long replyId) {
        token = token.replace("Bearer ", "");
        Map<String, Object> replyData = Map.of("jwt", token, "replyId", replyId);
        try {
            return new ResponseEntity<>(interactionFacade.getReply(replyData), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("userQuestions")
    public ResponseEntity<?> getUserQuestions(@RequestHeader("Authorization") String token,
                                              @RequestParam("limit") int limit,
                                              @RequestParam("offset") int offset) {
        Map<String, Object> userData = Map.of("jwt", token.replace("Bearer ", ""),
                "limit", limit,
                "offset", offset);
        try {
            List<Map<String, Object>> questions = interactionFacade.getUserQuestions(userData);
            return new ResponseEntity<>(questions, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("message", "Error fetching user questions"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("communityQuestions")
    public ResponseEntity<?> getCommunityQuestions(@RequestHeader("Authorization") String token,
                                                   @RequestParam("communityId") Long communityId,
                                                   @RequestParam("limit") int limit,
                                                   @RequestParam("offset") int offset) {
        Map<String, Object> communityData = Map.of("jwt", token.replace("Bearer ", ""),
                "communityId", communityId,
                "limit", limit,
                "offset", offset);
        try {
            List<Map<String, Object>> questions = interactionFacade.getCommunityQuestions(communityData);
            return new ResponseEntity<>(questions, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("message", "Error fetching community questions"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }





}