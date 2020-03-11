package com.cefalo.backend.service;


import com.cefalo.backend.exception.UserIdExistsException;
import com.cefalo.backend.model.User;

import java.util.Optional;

public interface IUserService {
    Optional<User> findByUserId(String userId);
    Optional<User> registerNewUserAccountAfterCheckingUserId(User user) throws UserIdExistsException;
}
