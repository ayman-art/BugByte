package com.example.BugByte_backend.models;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Question extends Post{

    private Long communityId;

    private Long upVotes;

    private Long downVotes;

    private Long validatedAnswerId;

    private List<String> tags;
}
