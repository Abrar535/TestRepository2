package com.example.learnspring.controller;


import com.example.learnspring.model.auth.AuthenticationRequest;
import com.example.learnspring.model.exception.UserIdExistsException;
import com.example.learnspring.model.User;
import com.example.learnspring.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class UserController {

    private final IUserService iUserService;

    @Autowired
    public UserController(IUserService iUserService) {
        this.iUserService = iUserService;
    }

    @RequestMapping(
            value = "/user/registration",
            method = RequestMethod.POST,
            consumes = {
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_JSON_VALUE
            },
            produces = {
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_JSON_VALUE
            }
    )
    public ResponseEntity<Void> registerUser(@Valid User requestUser, BindingResult bindingResult){
        User newUser = null;
        if(!bindingResult.hasErrors()){
            newUser = createUserAccount(requestUser, bindingResult);
        }
        if(newUser == null){
            bindingResult.rejectValue("userId", "message.regError", "userID: " + requestUser.getUserId() + " already exists");
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    private User createUserAccount(User requestUser, BindingResult result) {
        User newUser = null;
        try {
            newUser = iUserService.registerNewUserAccountAfterCheckingUserId(requestUser);
        } catch (UserIdExistsException e) {
            return null;
        }
        return newUser;
    }





}

