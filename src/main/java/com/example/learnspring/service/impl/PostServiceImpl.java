package com.example.learnspring.service.impl;

import com.example.learnspring.model.Post;
import com.example.learnspring.model.User;
import com.example.learnspring.repository.PostRepository;
import com.example.learnspring.repository.UserRepository;
import com.example.learnspring.service.IPostService;
import com.example.learnspring.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Date;
import java.util.List;
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
    public Page<Post> getAllPostsByPage(int pageNumber) {
        Pageable pageable = (pageNumber != -1)?
            PageRequest.of(pageNumber, 8, Sort.by("createdAt").descending())
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
        return Optional.ofNullable(save(post));
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
        return Optional.ofNullable(save(tempPost));
    }

    public Post save(Post post) {
        return postRepository.save(post);
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
