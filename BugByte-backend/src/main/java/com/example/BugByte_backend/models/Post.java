package com.example.BugByte_backend.models;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;


@Getter
@Setter
public class Post {
    private Long id;

    private User creator;

    private String mdContent;

    private Date postedOn;

    public Post() {
        this.id = 0L;
        this.creator = new User();
        this.mdContent = "";
        this.postedOn = new Date();
    }

    public Post(Long id, String creator, String mdContent, Date postedOn) {
        this.id = id;
        this.creator = new User();
        this.creator.setUserName(creator);
        this.mdContent = mdContent;
        this.postedOn = postedOn;
    }
}
