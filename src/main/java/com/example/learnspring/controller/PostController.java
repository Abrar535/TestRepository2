package com.example.learnspring.controller;


import com.example.learnspring.model.Post;
import com.example.learnspring.service.IPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
public class PostController {


    private final IPostService iPostService;


    @Autowired
    public PostController(IPostService iPostService) {
        this.iPostService = iPostService;
    }

    @RequestMapping(
            value = "/posts",
            method = RequestMethod.GET,
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE,
            }
    )
    public ResponseEntity<Iterable<Post>> getAllPosts() {
        return new ResponseEntity<>(iPostService.getAllPosts(), HttpStatus.OK);
    }

    @RequestMapping(
            value = "/posts/{id}",
            method = RequestMethod.GET,
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE,
            }
    )
    public ResponseEntity<Post> getPostById(@PathVariable Long id) {
        Optional<Post> post = iPostService.findById(id);
        return post.map(p -> new ResponseEntity<>(p, HttpStatus.OK))
                .orElseGet(() -> ResponseEntity.notFound().build());

    }


    @RequestMapping(
            value = "/posts",
            method = RequestMethod.POST,
            consumes = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE,
            },
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE,
            }
    )
    public ResponseEntity<Post> createPost(@Valid Post newPost) {
        Post post = iPostService.save(newPost);
        return new ResponseEntity<>(post, HttpStatus.CREATED);
    }


    @RequestMapping(
            value = "/posts",
            method = RequestMethod.PUT,
            consumes = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE,
            },
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE,
            }
    )
    public ResponseEntity<Post> editPost(@Valid Post newPost) {
        Optional<Post> post = iPostService.findById(newPost.getId());

        return post.map(p -> {
            p.setTitle(newPost.getTitle());
            p.setBody(newPost.getBody());
            Post editedPost = iPostService.save(p);
            return new ResponseEntity<>(editedPost, HttpStatus.OK);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @RequestMapping(
            value = "/posts/{id}",
            method = RequestMethod.DELETE
    )
    public ResponseEntity<Object> deletePost(@PathVariable Long id) {
        Optional<Post> post = iPostService.findById(id);

        return post.map(p -> {
            iPostService.delete(p);
            return ResponseEntity.noContent().build();
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

}
