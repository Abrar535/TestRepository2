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


    @Autowired
    public PostController(IPostService iPostService, IUserService iUserService)
    {
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
    public ResponseEntity<?> createPost(@RequestBody Post requestPost, Principal principal) {
        Optional<Post> post = iPostService.createNewPost(requestPost, principal);

        return (post.isPresent())? new ResponseEntity<>(post, HttpStatus.CREATED)
                : new ResponseEntity<>("Unable to save post", HttpStatus.UNPROCESSABLE_ENTITY);

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
    public ResponseEntity<?> editPost(@RequestBody Post requestPost, Principal principal) {
        Optional<Post> post = iPostService.updatePost(requestPost, principal);
        return (post.isPresent())? new ResponseEntity<>(post, HttpStatus.NO_CONTENT)
                : new ResponseEntity<>("Unauthorized access to post", HttpStatus.UNAUTHORIZED);
    }




    @RequestMapping(
            value = "/posts/{id}",
            method = RequestMethod.DELETE
    )
    public ResponseEntity<?> deletePost(@PathVariable Long id, Principal principal) {
        return (iPostService.deletePost(id, principal))? new ResponseEntity<>(true, HttpStatus.NO_CONTENT)
                : new ResponseEntity<>("Unauthorized access to post", HttpStatus.UNAUTHORIZED);
    }

}
