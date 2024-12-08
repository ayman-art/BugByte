package com.example.BugByte_backend.models;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;


@Getter
@Setter
public class Community {
    private Long id;

    private String name;

    private String description;

    private User admin = new User();

    private Date creationDate;

    private List<CommunityMember> communityMembers;

    public Community(String name, String description, Long adminId, Date creationDate) {
        this.name = name;
        this.description = description;
        this.admin.setId(adminId);
        this.creationDate = creationDate;
        this.id = 0L;
    }

    public Community(String name, String description, Long adminId) {
        this.name = name;
        this.description = description;
        this.admin.setId(adminId);
        this.creationDate = new Date();
        this.id = 0L;
    }

    public Community(String name,Long adminId) {
        this.name = name;
        this.description = "";
        this.admin.setId(adminId);
        this.creationDate = new Date();
    }

    public Community(){
        this.name = "";
        this.description = "";
        this.admin.setId(0L);
        this.creationDate = new Date();
        this.id = 0L;
    }
}
