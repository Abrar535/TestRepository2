package com.example.learnspring.controller;


import com.example.learnspring.model.Post;
import com.example.learnspring.service.IPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.swing.text.html.Option;
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
        return post.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));

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
    public ResponseEntity<Long> createPost(@Valid Post post, BindingResult bindingResult) {
        Long postId = iPostService.save(post);
        return new ResponseEntity<>(postId, HttpStatus.CREATED);
    }

}
