package com.cefalo.backend.service;

import com.cefalo.backend.model.User;
import com.cefalo.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@SpringBootTest
@RunWith(SpringRunner.class)
public class MyUserDetailsServiceTest {
    @MockBean
    private UserRepository userRepository;

    @TestConfiguration
    class MyUserDetailsServiceTestContextConfiguration {
        @Bean
        public MyUserDetailsService myUserDetailsService() {
            return new MyUserDetailsService(userRepository);
        }
    }
    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @BeforeEach
    public void setUp() {
        User user = new User("fuad", "fuadmmnf", "fuadqwer1234");

        Mockito.when(userRepository.findByUserId(user.getUserId()))
                .thenReturn(user);
    }


    @Test
    public void loadUserByUsername_WhenValidUserId_thenReturnUserDetail() {
        String testUserId = "fuadmmnf";
        UserDetails found = myUserDetailsService.loadUserByUsername(testUserId);
        assertEquals(found.getUsername(), testUserId);
    }

}