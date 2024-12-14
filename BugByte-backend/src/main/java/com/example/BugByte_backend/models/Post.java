package com.example.BugByte_backend.models;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Date;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Post {
    private Long id;

    private String creatorUserName;

    private String mdContent;

    private Date postedOn = new Date();
}
