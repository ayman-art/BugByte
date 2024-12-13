package com.example.BugByte_backend.models;

import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Getter
@Setter
//
//@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Community {
    private Long id;

    private String name;

    private String description;

    private Long adminId;

    private Date creationDate = new Date();

    private List<String> tags = new ArrayList<>();

    public Community(String name, String description, Long adminId, Date creationDate) {
        this.name = name;
        this.description = description;
        this.adminId = adminId;
        this.creationDate = creationDate;
        this.id = 0L;
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

    public Community() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

//    public Long getAdminId() {
//        return admin.getId();
//    }
//
//    public void setAdminId(Long adminId) {
//        this.admin.setId(adminId);
//    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;

    }
}
