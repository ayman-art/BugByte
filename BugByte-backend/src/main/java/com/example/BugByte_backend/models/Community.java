package com.example.BugByte_backend.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Entity
@Table(name="communities")
@Getter
@Setter
public class Community {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="name",nullable = false, unique = true)
    private String name;

    @Column(name="description",nullable = true)
    private String description;

    @ManyToOne
    @JoinColumn(name = "admin_id", nullable = false)
    private User admin = new User();

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="creation_date",nullable = false)
    private Date creationDate;

    @OneToMany(mappedBy = "community", cascade = CascadeType.ALL, orphanRemoval = true)
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
