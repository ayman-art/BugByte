package com.example.BugByte_backend.models;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.parameters.P;

import java.util.Date;


@Getter
@Setter
@NoArgsConstructor
public class Answer extends Post {

    public Answer(Long id, String creatorUserName, String mdContent, Date postedOn, Long questionId, Long upVotes, Long downVotes) {
        super(id, creatorUserName, mdContent, postedOn);
        this.questionId = questionId;
        this.upVotes = upVotes;
        this.downVotes = downVotes;
    }

    public Answer(Long questionId, Long upVotes, Long downVotes) {
        this.questionId = questionId;
        this.upVotes = upVotes;
        this.downVotes = downVotes;
    }

    private Long questionId;

    private Long upVotes;

    private Long downVotes;
}
