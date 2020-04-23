package com.cefalo.backend.service.impl;

import com.cefalo.backend.model.Post;
import com.cefalo.backend.model.User;
import com.cefalo.backend.repository.PostRepository;
import com.cefalo.backend.repository.UserRepository;
import com.cefalo.backend.service.IPostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@RunWith(SpringRunner.class)
public class PostServiceImplTest {


    @MockBean
    private UserRepository userRepository;
    @MockBean
    private PostRepository postRepository;

    @TestConfiguration
    class PostServiceImplTestContextConfiguration {
        @Bean
        public IPostService postService() {
            return new PostServiceImpl(postRepository, userRepository);
        }
    }

    @Autowired
    private IPostService postService;


    @BeforeEach
    public void setUp() {
        User user = new User(0L, "fuad", "fuadmmnf", "fuadqwer1234");
        Post post = new Post(0L, "title", "body", user);



        Mockito.when(userRepository.findByUserId(user.getUserId()))
                .thenReturn(user);

        Mockito.when(postRepository.save(any(Post.class))).thenReturn(post);
        Mockito.when(postRepository.findById(post.getId())).thenReturn(Optional.ofNullable(post));
    }


    @Test
    public void createNewPost_whenValidInput_thenReturnPost() {
        User testUser = new User(0L, "fuad", "fuadmmnf", "fuadqwer1234");
        Post testPost = new Post(0L, "title", "body", testUser);

        Optional<Post> actualPost = postService.createNewPost(testPost, testUser.getUserId());

        assertEquals(testPost.getId(), actualPost.get().getId());
    }

    @Test
    public void updatePost_whenValidInput_thenReturnPost() {
        User testUser = new User(0L, "fuad", "fuadmmnf", "fuadqwer1234");
        Post testPost = new Post(0L, "New Title", "New Body", testUser);

        Optional<Post> actualPost = postService.updatePost(testPost, testUser.getUserId());

        assertEquals(testPost.getId(), actualPost.get().getId());
        assertEquals(testPost.getTitle(), actualPost.get().getTitle());
        assertEquals(testPost.getBody(), actualPost.get().getBody());
    }

    @Test
    public void deletePost_whenValidUser_thenReturnTrue() {
        User testUser = new User(0L, "fuad", "fuadmmnf", "fuadqwer1234");
        Post testPost = new Post(0L, "New Title", "New Body", testUser);

        boolean isDeleted = postService.deletePost(testPost.getId(), testUser.getUserId());
        assertEquals(true, isDeleted);
    }
}