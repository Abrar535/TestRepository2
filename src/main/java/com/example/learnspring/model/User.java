package com.example.learnspring.model;


import com.example.learnspring.model.template.AuditModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@Entity
public class User extends AuditModel{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique = true)
    @NotBlank
    @Length(min = 5, message = "*Please make userId containing at least 5 alphanumeric characters")
    private String userId;
    @NotBlank
    private String name;
    @Length(min = 8)
    @NotNull
    @Lob
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @JsonIgnore
    @OneToMany(targetEntity=Post.class,cascade = CascadeType.ALL , fetch = FetchType.LAZY, mappedBy = "user")
    private Collection<Post> posts;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public Collection<Post> getPosts() {
        return posts;
    }

    public void setPosts(Collection<Post> posts) {
        this.posts = posts;
    }

    public void addPosts(Post post){
        posts.add(post);
        post.setUser(this);
    }

    public void removePosts(Post post){
        posts.remove(post);
        post.setUser(null);
    }

}
