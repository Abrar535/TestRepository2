package com.example.learnspring.controller;


import com.example.learnspring.model.Post;
import com.example.learnspring.service.IPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class PostController {

    private final IPostService iPostService;


    @Autowired
    public PostController(IPostService iPostService) {
        this.iPostService = iPostService;
    }

    @RequestMapping(value = "/posts", method = RequestMethod.GET)
    public ResponseEntity<Iterable<Post>> getAllPosts(){
        return new ResponseEntity<>(iPostService.getAllPosts().get(), HttpStatus.OK);
    }

}
