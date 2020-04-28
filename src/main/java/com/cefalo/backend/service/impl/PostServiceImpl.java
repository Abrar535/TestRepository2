package com.cefalo.backend.service.impl;

import com.cefalo.backend.model.Post;
import com.cefalo.backend.model.User;
import com.cefalo.backend.repository.PostRepository;
import com.cefalo.backend.repository.UserRepository;
import com.cefalo.backend.service.IPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImpl implements IPostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Page<Post> getAllPostsByPage(int pageNumber, int pageSize) {
        Pageable pageable = (pageNumber != -1)?
            PageRequest.of(pageNumber, pageSize, Sort.by("createdAt").descending())
            : PageRequest.of(0, Integer.MAX_VALUE, Sort.by("createdAt").descending());
        return postRepository.findAllByPublished(pageable, true);
    }

    @Override
    public Page<Post> getAllUserDraftsByPage(int pageNumber, int pageSize, String userId) {
        User currentUser = userRepository.findByUserId(userId);
        Pageable pageable = (pageNumber != -1)?
                PageRequest.of(pageNumber, pageSize, Sort.by("createdAt").descending())
                : PageRequest.of(0, Integer.MAX_VALUE, Sort.by("createdAt").descending());
        return postRepository.findAllByUserAndDraft(pageable, currentUser, true);
    }

    @Override
    public Optional<Post> findById(Long id) {
        return postRepository.findById(id);
    }


    @Override
    public Optional<Post> createNewPost(Post post, String userId) {
        User currentUser = userRepository.findByUserId(userId);
        if(post.getScheduledPublishTime() != null){
            post.setPublished(false);
        }
        post.setUser(currentUser);
        post.setCreatedAt(new Date());
        post.setUpdatedAt(new Date());
        currentUser.addPosts(post);


        return save(post);

    }

    @Scheduled(fixedRateString = "${sample.schedule.string}")
    public void publishScheduledPost() throws InterruptedException {
        System.out.println("Scheduling trigerred");
        Date currentTime = new Date();

        List<Post> pendingScheduledPosts = postRepository.findAllByScheduledPublishTimeAfter(currentTime);

        for(Post post : pendingScheduledPosts){
            long diffMinutes = (post.getScheduledPublishTime().getTime() - currentTime.getTime()) / (60 * 1000) % 60;
            if(diffMinutes == 0){
                post.setPublished(true);
                System.out.println(post.getId());
                postRepository.save(post);
            }
        }

    }




    @Override
    public Optional<Post> updatePost(Post requestPost, String userId) {
        Optional<Post> post = postRepository.findById(requestPost.getId());
        User currentUser = userRepository.findByUserId(userId);

        if(!post.isPresent() || !isOriginalAuthor(post.get(), currentUser))
            return Optional.empty();

        Post tempPost = post.get();
        tempPost.setTitle(requestPost.getTitle());
        tempPost.setBody(requestPost.getBody());
        tempPost.setUpdatedAt(new Date());
        return save(tempPost);
    }

    @Override
    public Optional<Post> save(Post post) {
        try{
            return Optional.of(postRepository.save(post));
        } catch (Exception e){
            return Optional.empty();
        }
    }


    @Override
    public Boolean deletePost(Long postId, String userId) {
        Optional<Post> post = postRepository.findById(postId);
        User currentUser = userRepository.findByUserId(userId);

        if(!post.isPresent() || !isOriginalAuthor(post.get(), currentUser))
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
