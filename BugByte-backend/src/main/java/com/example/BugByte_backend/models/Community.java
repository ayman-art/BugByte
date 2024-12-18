package com.example.BugByte_backend.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Getter
@Setter

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(indexName = "community")
public class Community {
    @Id
    @Field(type = FieldType.Long)
    private Long id;

    @Field(type = FieldType.Text, analyzer = "standard", searchAnalyzer = "standard")
    private String name;

    @Field(type = FieldType.Text, analyzer = "standard", searchAnalyzer = "standard")
    private String description;

    @Field(type = FieldType.Long)
    private Long adminId;

    @Field(type = FieldType.Date)
    private Date creationDate = new Date();

    @Field(type = FieldType.Keyword)
    private List<String> tags = new ArrayList<>();

    public Community(Long id,String name, String description) {
        this.name = name;
        this.description = description;
        this.id =id;
    }

    public Community(String name, String description, Long adminId) {
        this.name = name;
        this.description = description;
        this.adminId = adminId;
        this.id = 0L;
    }

    public Community(String name,Long adminId) {
        this.name = name;
        this.description = "";
        this.adminId = adminId;
        this.creationDate = new Date();
        this.id = 0L;
    }

}
