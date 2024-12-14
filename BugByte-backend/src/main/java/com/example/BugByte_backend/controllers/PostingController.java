package com.example.BugByte_backend.controllers;
import com.example.BugByte_backend.facades.InteractionFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
@RestController()
@RequestMapping("/posts")
public class PostingController {
    //     @Autowired
//     private InteractionFacade interactionFacade;
    @PostMapping("questions")
    public ResponseEntity<?> postQuestion(@RequestHeader("Authorization") String token, @RequestParam("communityId") Long communityId, @RequestBody Map<String, Object> question) {
        token = token.replace("Bearer ", "");
        question.put("jwt", token);
        question.put("communityId", communityId);
        try {
//            return new ResponseEntity<>(interactionFacade.postQuestion(question), HttpStatus.OK);
            return new ResponseEntity<>(Map.of("message", "Question created successfully"), HttpStatus.OK);
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
//            return new ResponseEntity<>(interactionFacade.postAnswer(answer), HttpStatus.OK);
            return new ResponseEntity<>(Map.of("message", "Answer created successfully"), HttpStatus.OK);
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
//            return new ResponseEntity<>(interactionFacade.postReply(reply), HttpStatus.OK);
            return new ResponseEntity<>(Map.of("message", "Reply created successfully"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
    @GetMapping("questions")
    public ResponseEntity<?> getQuestions(@RequestHeader("Authorization") String token, @RequestParam("questionId") Long questionId) {
        token = token.replace("Bearer ", "");
        Map<String, Object> questionData = Map.of("jwt", token, "questionId", questionId);
        try {
//            return new ResponseEntity<>(interactionFacade.getQuestions(questionData), HttpStatus.OK);
            return new ResponseEntity<>(Map.of("message", "Questions fetched successfully"), HttpStatus.OK);
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
//            return new ResponseEntity<>(interactionFacade.getAnswers(answerData), HttpStatus.OK);
            return new ResponseEntity<>(Map.of("message", "Answers fetched successfully"), HttpStatus.OK);
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
//            return new ResponseEntity<>(interactionFacade.getReplies(replyData), HttpStatus.OK);
            return new ResponseEntity<>(Map.of("message", "Replies fetched successfully"), HttpStatus.OK);
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
//            return new ResponseEntity<>(interactionFacade.editQuestion(question), HttpStatus.OK);
            return new ResponseEntity<>(Map.of("message", "Question updated successfully"), HttpStatus.OK);
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
//            return new ResponseEntity<>(interactionFacade.editAnswer(answer), HttpStatus.OK);
            return new ResponseEntity<>(Map.of("message", "Answer updated successfully"), HttpStatus.OK);
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
//            return new ResponseEntity<>(interactionFacade.editReply(reply), HttpStatus.OK);
            return new ResponseEntity<>(Map.of("message", "Reply updated successfully"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
    @DeleteMapping("questions")
    public ResponseEntity<?> deleteQuestion(@RequestHeader("Authorization") String token, @RequestParam("questionId") Long questionId) {
        token = token.replace("Bearer ", "");
        Map<String, Object> questionData = Map.of("jwt", token, "questionId", questionId);
        try {
//            return new ResponseEntity<>(interactionFacade.deleteQuestion(questionData), HttpStatus.OK);
            return new ResponseEntity<>(Map.of("message", "Question deleted successfully"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
    @DeleteMapping("answers")
    public ResponseEntity<?> deleteAnswer(@RequestHeader("Authorization") String token, @RequestParam("answerId") Long answerId) {
        token = token.replace("Bearer ", "");
        Map<String, Object> answerData = Map.of("jwt", token, "answerId", answerId);
        try {
//            return new ResponseEntity<>(interactionFacade.deleteAnswer(answerData), HttpStatus.OK);
            return new ResponseEntity<>(Map.of("message", "Answer deleted successfully"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
    @DeleteMapping("replies")
    public ResponseEntity<?> deleteReply(@RequestHeader("Authorization") String token, @RequestParam("replyId") Long replyId) {
        token = token.replace("Bearer ", "");
        Map<String, Object> replyData = Map.of("jwt", token, "replyId", replyId);
        try {
//            return new ResponseEntity<>(interactionFacade.deleteReply(replyData), HttpStatus.OK);
            return new ResponseEntity<>(Map.of("message", "Reply deleted successfully"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
    @PutMapping("upvote")
    public ResponseEntity<?> upvote(@RequestHeader("Authorization") String token, @RequestParam("postId") Long postId) {
        token = token.replace("Bearer ", "");
        Map<String, Object> voteData = Map.of("jwt", token, "postId", postId);
        try {
//            return new ResponseEntity<>(interactionFacade.upvote(voteData), HttpStatus.OK);
            return new ResponseEntity<>(Map.of("message", "Upvoting Updated"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
    @PutMapping("downvote")
    public ResponseEntity<?> downvote(@RequestHeader("Authorization") String token, @RequestParam("postId") Long postId) {
        token = token.replace("Bearer ", "");
        Map<String, Object> voteData = Map.of("jwt", token, "postId", postId);
        try {
//            return new ResponseEntity<>(interactionFacade.downvote(voteData), HttpStatus.OK);
            return new ResponseEntity<>(Map.of("message", "Downvoting Updated"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("answers/verify")
    public ResponseEntity<?> verifyAnswer(@RequestHeader("Authorization") String token, @RequestParam("answerId") Long answerId) {
        token = token.replace("Bearer ", "");
        Map<String, Object> answerData = Map.of("jwt", token, "answerId", answerId);
        try {
//            return new ResponseEntity<>(interactionFacade.verifyAnswer(answerData), HttpStatus.OK);
            return new ResponseEntity<>(Map.of("message", "Answer verified successfully"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("answers/{answerId}")
    public ResponseEntity<?> getAnswer(@RequestHeader("Authorization") String token, @PathVariable("answerId") Long answerId) {
        token = token.replace("Bearer ", "");
        Map<String, Object> answerData = Map.of("jwt", token, "answerId", answerId);
        try {
//            return new ResponseEntity<>(interactionFacade.getAnswer(answerData), HttpStatus.OK);
            return new ResponseEntity<>(Map.of("message", "Answer fetched successfully"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("replies/{replyId}")
    public ResponseEntity<?> getReply(@RequestHeader("Authorization") String token, @PathVariable("replyId") Long replyId) {
        token = token.replace("Bearer ", "");
        Map<String, Object> replyData = Map.of("jwt", token, "replyId", replyId);
        try {
//            return new ResponseEntity<>(interactionFacade.getReply(replyData), HttpStatus.OK);
            return new ResponseEntity<>(Map.of("message", "Reply fetched successfully"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }


}