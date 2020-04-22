package com.cefalo.backend.service.impl;

import com.cefalo.backend.model.Post;
import com.cefalo.backend.model.User;
import com.cefalo.backend.repository.PostRepository;
import com.cefalo.backend.service.IPostService;
import com.cefalo.backend.service.IUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@RunWith(SpringRunner.class)
public class PostServiceImplTest {
    @MockBean
    private PostRepository postRepository;
    @MockBean
    private IUserService userService;

    @TestConfiguration
    class PostServiceImplTestContextConfiguration {
        @Bean
        public IPostService postService() {
            return new PostServiceImpl(postRepository, userService);
        }
    }
    @Autowired
    private IPostService postService;



    @BeforeEach
    public void setUp() {
        User user1 = new User("nafis", "nafismmnf", "nafisqwer1234");
        User user2 = new User("fuad", "fuadmmnf", "fuadqwer1234");
        List<Post> posts = new ArrayList<>();

        for(int i=0; i<10; i++){
            posts.add(new Post((long)i, "title"+i, "body"+i, (i%2==0)? user1: user2));
            Mockito.when(postRepository.findById(posts.get(i).getId())).thenReturn(Optional.ofNullable(posts.get(i)));
        }



    }

}