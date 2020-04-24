package com.cefalo.backend.service;

import com.cefalo.backend.model.Post;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface IPostService {
    Optional<Post> findById(Long id);
    Page<Post> getAllPostsByPage(int pageNumber, int pageSize);
    Page<Post> getAllUserDraftsByPage(int pageNumber, int pageSize, String userId);
    Optional<Post> createNewPost(Post post, String userId);
    Optional<Post> updatePost(Post requestPost, String userId);
    Boolean deletePost(Long postId, String userId);
    Optional<Post> save(Post post);
}
