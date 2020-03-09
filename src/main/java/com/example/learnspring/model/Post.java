package com.example.learnspring.model;



import com.example.learnspring.model.template.AuditModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
public class Post extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotBlank
    private String title;
    @Lob
    @NotBlank
    private String body;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Transient
    private String authorId;


    @Override
    public String toString() {
        return String.format("Post(" +
                " id: %d," +
                " title: %s," +
                " body: %s," +
                " published_date: %s," +
                " updated_date: %s" +
                ")", this.id, this.title, this.body, this.getCreatedAt(), this.getUpdatedAt());
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    @JsonIgnore
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        this.authorId = user.getUserId();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getAuthorId() {
        return this.user.getUserId();
    }

    public void setAuthorId() {
        this.authorId = this.user.getUserId();
    }
}
