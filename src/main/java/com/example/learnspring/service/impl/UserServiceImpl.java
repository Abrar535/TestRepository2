package com.example.learnspring.service.impl;

import com.example.learnspring.model.exception.UserIdExistsException;
import com.example.learnspring.model.User;
import com.example.learnspring.repository.UserRepository;
import com.example.learnspring.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Transactional
    @Override
    public User registerNewUserAccountAfterCheckingUserId(User user)
    throws UserIdExistsException {

        if(userIdExists(user.getUserId())){
            throw new UserIdExistsException("userID: " + user.getUserId() + " already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.saveAndFlush(user);
    }

    private boolean userIdExists(String userId) {
        User user = userRepository.findByUserId(userId);
        return user != null;
    }

    @Override
    public void delete(User user) {
        userRepository.delete(user);
    }
}
