package com.example.learnspring.service;


import com.example.learnspring.exception.UserIdExistsException;
import com.example.learnspring.model.User;

import java.util.Optional;

public interface IUserService {
    Optional<User> findByUserId(String userId);
    Optional<User> registerNewUserAccountAfterCheckingUserId(User user) throws UserIdExistsException;
}
