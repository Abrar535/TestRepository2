package com.cefalo.backend.service.impl;

import com.cefalo.backend.model.Post;
import com.cefalo.backend.model.User;
import com.cefalo.backend.service.IUserService;
import com.cefalo.backend.repository.PostRepository;
import com.cefalo.backend.service.IPostService;
import org.hibernate.exception.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Date;
import java.util.Optional;

@Service
public class PostServiceImpl implements IPostService {

    private final PostRepository postRepository;
    private final IUserService iUserService;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, IUserService iUserService) {
        this.postRepository = postRepository;
        this.iUserService = iUserService;
    }

    @Override
    public Page<Post> getAllPostsByPage(int pageNumber, int pageSize) {
        Pageable pageable = (pageNumber != -1)?
            PageRequest.of(pageNumber, pageSize, Sort.by("createdAt").descending())
            : PageRequest.of(0, Integer.MAX_VALUE, Sort.by("createdAt").descending());
        return postRepository.findAll(pageable);
    }

    @Override
    public Optional<Post> findById(Long id) {
        return postRepository.findById(id);
    }


    @Override
    public Optional<Post> createNewPost(Post post, Principal principal) {
        Optional<User> currentUser = iUserService.findByUserId(principal.getName());

        post.setUser(currentUser.get());
        post.setCreatedAt(new Date());
        post.setUpdatedAt(new Date());
        currentUser.get().addPosts(post);


        return save(post);

    }

    @Override
    public Optional<Post> updatePost(Post requestPost, Principal principal) {
        Optional<Post> post = postRepository.findById(requestPost.getId());
        Optional<User> currentUser = iUserService.findByUserId(principal.getName());

        if(!post.isPresent() || !isOriginalAuthor(post.get(), currentUser.get()))
            return Optional.empty();

        Post tempPost = post.get();
        tempPost.setTitle(requestPost.getTitle());
        tempPost.setBody(requestPost.getBody());
        tempPost.setUpdatedAt(new Date());
        return save(tempPost);
    }

    public Optional<Post> save(Post post) {
        try{
            return Optional.of(postRepository.save(post));
        } catch (Exception e){
            return Optional.empty();
        }
    }


    @Override
    public Boolean deletePost(Long postId, Principal principal) {
        Optional<Post> post = postRepository.findById(postId);
        Optional<User> currentUser = iUserService.findByUserId(principal.getName());

        if(!post.isPresent() || !isOriginalAuthor(post.get(), currentUser.get()))
            return false;
        delete(post.get());
        return true;
    }

    public void delete(Post post) {
        postRepository.delete(post);
    }


    private Boolean isOriginalAuthor(Post post, User currentUser){
        return currentUser.getId() == post.getUser().getId();
    }
}
