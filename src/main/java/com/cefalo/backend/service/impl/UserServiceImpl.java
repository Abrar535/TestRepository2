package com.cefalo.backend.service.impl;

import com.cefalo.backend.exception.UserIdExistsException;
import com.cefalo.backend.model.User;
import com.cefalo.backend.repository.UserRepository;
import com.cefalo.backend.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
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

    private Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Transactional
    @Override
    public Optional<User> registerNewUserAccountAfterCheckingUserId(User user)
            throws UserIdExistsException {

        if (userIdExists(user.getUserId())) {
            throw new UserIdExistsException("userID: " + user.getUserId() + " already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());
        try {
            User newUser = userRepository.saveAndFlush(user);
            return Optional.of(newUser);
        } catch (Exception e){
            return Optional.empty();
        }
    }

    private boolean userIdExists(String userId) {
        Optional<User> user = Optional.ofNullable(userRepository.findByUserId(userId));
        return user.isPresent();
    }

    @Override
    public Optional<User> findByUserId(String userId) {
        return Optional.ofNullable(userRepository.findByUserId(userId));
    }



    private void delete(User user) {
        userRepository.delete(user);
    }
}
