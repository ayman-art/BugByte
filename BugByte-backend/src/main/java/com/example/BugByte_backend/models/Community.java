package com.example.BugByte_backend.models;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="communities")
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
        this.id = 0L;
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

    public Long getAdminId() {
        return admin.getId();
    }

    public void setAdminId(Long adminId) {
        this.admin.setId(adminId);
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}
