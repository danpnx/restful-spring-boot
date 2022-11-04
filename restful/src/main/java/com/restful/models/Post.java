package com.restful.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "POSTS")
public class Post implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Type(type = "org.hibernate.type.UUIDCharType")
    @Column(name = "POST_ID", columnDefinition = "CHAR(36)", unique = true)
    private UUID id;

    @Column(name = "CONTENT", nullable = false)
    private String content;

    @Column(name = "LIKES", nullable = false)
    private long likes;

    @ManyToOne
    @JoinTable(name = "USER_ID")
    @JsonIgnoreProperties("posts")
    private User user;

    public Post() {
    }

    public Post(String content) {
        this.id = UUID.randomUUID();
        this.content = content;
        this.likes = 0;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getLikes() {
        return likes;
    }

    public void setLikes(long likes) {
        this.likes = likes;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
