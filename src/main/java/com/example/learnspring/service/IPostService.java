package com.example.learnspring.service;

import com.example.learnspring.model.Post;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

public interface IPostService {
    Optional<Post> findById(Long id);
    List<Post> getAllPosts();
    Optional<Post> createNewPost(Post post, Principal principal);
    Optional<Post> updatePost(Post requestPost, Principal principal);
    Boolean deletePost(Long postId, Principal principal);
}
