package com.example.BugByte_backend.models;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Post {

    @Id
    @Field(type = FieldType.Long)
    private Long id;

    @Field(type = FieldType.Keyword)
    private String creatorUserName;

    @Field(type = FieldType.Text, analyzer = "standard", searchAnalyzer = "standard")
    private String mdContent;

    @Field(type = FieldType.Date)
    private Date postedOn = new Date();
}
