package com.example.BugByte_backend.models;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Document(indexName = "question")
public class Question extends Post{

    private Long communityId;

    private Long upVotes;

    private Long downVotes;

    private Long validatedAnswerId;

    @Field(type = FieldType.Keyword)
    private List<String> tags;
}
