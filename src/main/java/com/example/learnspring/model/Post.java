package com.example.learnspring.model;


import com.fasterxml.jackson.databind.JsonNode;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Post {

    @Transient
    private JsonNode json;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String title;
    private String body;
    private Date published_date;

    protected Post() {
    }

    @Override
    public String toString() {
        return String.format("Post(" +
                " id: %d," +
                " title: %s," +
                " body: %s," +
                " published_date: %s" +
                ")", this.id, this.title, this.body, this.published_date.toString());
    }

    @Transient
    public JsonNode getJson() {
        return json;
    }

    public void setJson(JsonNode json) {
        this.json = json;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public Date getPublished_date() {
        return published_date;
    }

    public void setPublished_date(Date published_date) {
        this.published_date = published_date;
    }
}