package com.example.BugByte_backend.models;

import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Getter
@Setter

@NoArgsConstructor
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
