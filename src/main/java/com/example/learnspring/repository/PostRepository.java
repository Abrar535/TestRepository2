package com.example.learnspring.repository;

import com.example.learnspring.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PostRepository extends PagingAndSortingRepository<Post, Long> {
//    Page<Post> findAll(Pageable pageable);
}
