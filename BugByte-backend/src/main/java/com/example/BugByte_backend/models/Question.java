package com.example.BugByte_backend.models;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;


@Getter
@Setter
@NoArgsConstructor
public class Question extends Post{
    public Question(Long id, String creatorUserName, String mdContent, Date postedOn, Long communityId, Long upVotes, Long downVotes, Long validatedAnswerId) {
        super(id, creatorUserName, mdContent, postedOn);
        this.communityId = communityId;
        this.upVotes = upVotes;
        this.downVotes = downVotes;
        this.validatedAnswerId = validatedAnswerId;
    }

    public Question(Long communityId, Long upVotes, Long downVotes, Long validatedAnswerId) {
        this.communityId = communityId;
        this.upVotes = upVotes;
        this.downVotes = downVotes;
        this.validatedAnswerId = validatedAnswerId;
    }

    private Long communityId;

    private Long upVotes;

    private Long downVotes;

    private Long validatedAnswerId;
}
