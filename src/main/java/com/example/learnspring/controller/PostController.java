package com.example.learnspring.controller;


import com.example.learnspring.model.Post;
import com.example.learnspring.model.User;
import com.example.learnspring.service.IPostService;
import com.example.learnspring.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Date;
import java.util.Optional;

@RestController
public class PostController {


    private final IPostService iPostService;
    private final IUserService iUserService;


    @Autowired
    public PostController(IPostService iPostService, IUserService iUserService)
    {
        this.iPostService = iPostService;
        this.iUserService = iUserService;
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
    public ResponseEntity<?> createPost(@RequestBody Post requestPost, Principal principal) {
        Optional<User> currentUser = iUserService.findByUserId(principal.getName());
        requestPost.setUser(currentUser.get());
        requestPost.setCreatedAt(new Date());
        requestPost.setUpdatedAt(new Date());
        Post newPost = iPostService.save(requestPost);
        return new ResponseEntity<>(newPost, HttpStatus.CREATED);
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
    public ResponseEntity<Post> editPost(@RequestBody Post requestPost, Principal principal) {
        Optional<Post> post = iPostService.findById(requestPost.getId());
        Optional<User> currentUser = iUserService.findByUserId(principal.getName());



        return post.map(p -> {

            p.setTitle(requestPost.getTitle());
            p.setBody(requestPost.getBody());
            p.setUpdatedAt(new Date());
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
