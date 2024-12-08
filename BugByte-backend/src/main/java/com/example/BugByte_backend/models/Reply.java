package com.example.BugByte_backend.models;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;


@Getter
@Setter
@NoArgsConstructor
public class Reply extends Post{
    public Reply(Long id, String creatorUserName, String mdContent, Date postedOn, Long answerId) {
        super(id, creatorUserName, mdContent, postedOn);
        this.answerId = answerId;
    }

    public Reply(Long answerId) {
        this.answerId = answerId;
    }

    private Long answerId;
}
