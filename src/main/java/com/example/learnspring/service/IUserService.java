package com.example.learnspring.service;


import com.example.learnspring.model.exception.UserIdExistsException;
import com.example.learnspring.model.User;

import java.util.Optional;

public interface IUserService {
    Optional<User> findById(Long id);
    User registerNewUserAccountAfterCheckingUserId(User user) throws UserIdExistsException;
    void delete(User user);
}
