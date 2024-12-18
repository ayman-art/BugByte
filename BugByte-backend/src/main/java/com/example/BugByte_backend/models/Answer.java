package com.example.BugByte_backend.models;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.parameters.P;

import java.util.Date;


@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class Answer extends Post {

    private Long questionId;

    private Long upVotes;

    private Long downVotes;
}
