package com.cefalo.backend.service;

import com.cefalo.backend.model.Post;
import org.springframework.data.domain.Page;

import java.security.Principal;
import java.util.Optional;

public interface IPostService {
    Optional<Post> findById(Long id);
    Page<Post> getAllPostsByPage(int pageNumber, int pageSize);
    Optional<Post> createNewPost(Post post, Principal principal);
    Optional<Post> updatePost(Post requestPost, Principal principal);
    Boolean deletePost(Long postId, Principal principal);
}
