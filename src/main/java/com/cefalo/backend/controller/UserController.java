package com.cefalo.backend.controller;


import com.cefalo.backend.exception.UserIdExistsException;
import com.cefalo.backend.model.User;
import com.cefalo.backend.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

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
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE,
            },
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE,
            }
    )
    public ResponseEntity<?> registerUser(@Valid @RequestBody User requestUser){
        Optional<User> newUser;
        try {
            newUser = iUserService.registerNewUserAccountAfterCheckingUserId(requestUser);
        } catch (UserIdExistsException e) {
            return  new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return (!newUser.isPresent())? ResponseEntity.badRequest().body("Violating user contraints"): new ResponseEntity<>(newUser, HttpStatus.CREATED);

    }

}

