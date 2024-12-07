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
    @JoinColumn(name = "op_name", referencedColumnName = "username", nullable = false)
    private User creator;

    @Column(name = "md_content", nullable = false)
    private String mdContent;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "posted_on", nullable = false)
    private Date postedOn;

    public Post(){
        this.id = 0L;
        this.creator = new User();
        this.mdContent = "";
        this.postedOn = new Date();
    }

    public Post(Long id , String creator, String mdContent, Date postedOn) {
        this.id = id;
        this.creator = new User();
        this.creator.setUserName(creator);
        this.mdContent = mdContent;
        this.postedOn = postedOn;
    }

    public Post(User creator, String mdContent) {
        this.creator = creator;
        this.mdContent = mdContent;
        this.postedOn = new Date();
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

    public String getMdContent() {
        return mdContent;
    }

    public void setMdContent(String mdContent) {
        this.mdContent = mdContent;
    }

    public Date getPostedOn() {
        return postedOn;
    }

    public void setPostedOn(Date postedOn) {
        this.postedOn = postedOn;
    }
}
