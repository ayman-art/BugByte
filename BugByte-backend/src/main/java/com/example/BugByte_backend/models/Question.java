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

    @Field(type = FieldType.Text, analyzer = "standard", searchAnalyzer = "standard")
    private String title;

    @Field(type = FieldType.Long)
    private Long communityId;

    @Field(type = FieldType.Long)
    private Long upVotes;

    @Field(type = FieldType.Long)
    private Long downVotes;

    @Field(type = FieldType.Long)
    private Long validatedAnswerId;

    @Field(type = FieldType.Keyword)
    private List<String> tags;
}
