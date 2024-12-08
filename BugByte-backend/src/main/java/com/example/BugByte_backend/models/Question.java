package com.example.BugByte_backend.models;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Question {
    private Long postId;

    private Long communityId;

    private Long upVotes;

    private Long downVotes;

    private Answer validatedAnswer;
}
