package com.cefalo.backend.repository;

import com.cefalo.backend.model.Post;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PostRepository extends PagingAndSortingRepository<Post, Long> {
}
