package com.cefalo.backend.service.impl;

import com.cefalo.backend.model.User;
import com.cefalo.backend.repository.UserRepository;
import com.cefalo.backend.service.IUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.Optional;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserServiceImplTest {

    @MockBean
    private UserRepository userRepository;
    @MockBean
    private PasswordEncoder passwordEncoder;

    @TestConfiguration
    class UserServiceImplTestContextConfiguration {
        @Bean
        public IUserService userService() {
            return new UserServiceImpl(userRepository, passwordEncoder);
        }
    }
    @Autowired
    private IUserService userService;


    @BeforeEach
    public void setUp() {
        User user = new User();
        user.setUserId("fuadmmnf");
        user.setName("fuad");
        user.setPassword("fuadqwer1234");
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());

        Mockito.when(userRepository.findByUserId(user.getUserId()))
                .thenReturn(user);
    }


    @Test
    public void whenValidUserId_thenUserShouldBefound() {
        String testUserId = "fuadmmnf";
        Optional<User> found = userService.findByUserId(testUserId);
        assertEquals(found.get().getUserId(), testUserId);
    }

    @Test
    public void whenInvalidUserId_thenUserShouldNotBefound() {
        String testUserId = "fuadmf";
        Optional<User> found = userService.findByUserId(testUserId);
        assertEquals(found.isPresent(), false);
    }

}