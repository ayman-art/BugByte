package com.example.BugByte_backend.models;
import jakarta.persistence.*;
import java.util.Date;


@Entity
@Table(name="posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "op_id", nullable = false)
    private User creator;

    @Column(name = "md_content" ,nullable = false)
    private String md_content;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "posted_on" ,nullable = false)
    private Date posted_on;

    public Post(){
        this.id = 0L;
        this.creator = new User();
        this.md_content = "";
        this.posted_on = new Date();
    }
    public Post(Long id ,User creator ,String md_content) {
        this.id = id;
        this.creator = creator;
        this.md_content = md_content;
        this.posted_on = new Date();
    }
    public Post(User creator, String md_content) {
        this.creator = creator;
        this.md_content = md_content;
        this.posted_on = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public String getMd_content() {
        return md_content;
    }

    public void setMd_content(String md_content) {
        this.md_content = md_content;
    }

    public Date getPosted_on() {
        return posted_on;
    }

    public void setPosted_on(Date posted_on) {
        this.posted_on = posted_on;
    }

}
