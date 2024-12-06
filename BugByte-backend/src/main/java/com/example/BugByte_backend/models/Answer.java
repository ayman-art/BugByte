package com.example.BugByte_backend.models;
import jakarta.persistence.*;


@Entity
@Table(name="answers")
public class Answer {
    @Id
    @OneToOne
    @JoinColumn(name = "id" ,nullable = false)
    private Post post;

    @ManyToOne
    @JoinColumn(name = "question_id" ,nullable = false)
    private Question question = new Question();

    @Column(name = "up_votes" ,nullable = false)
    private Long upVotes;

    @Column(name = "down_votes" ,nullable = false)
    private Long downVotes;

    public Answer(){
        this.post = new Post();
        this.question = new Question();
        this.upVotes = 0L;
        this.downVotes = 0L;
    }

    public Answer(Post post, Long question, Long upVotes, Long downVotes) {
        this.post = post;
        this.question.getPost().setId(question);
        this.upVotes = upVotes;
        this.downVotes = downVotes;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Long getUpVotes() {
        return upVotes;
    }

    public void setUpVotes(Long upVotes) {
        this.upVotes = upVotes;
    }

    public Long getDownVotes() {
        return downVotes;
    }

    public void setDownVotes(Long downVotes) {
        this.downVotes = downVotes;
    }
}
