package com.example.learnspring.service;

import com.example.learnspring.model.Post;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface IPostService {
    Optional<Post> findById(Long id);
    List<Post> getAllPosts();
    Post save(Post post);
    void delete(Post post);
}
