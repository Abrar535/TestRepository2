package com.cefalo.backend.repository;

import com.cefalo.backend.model.Post;
import com.cefalo.backend.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PostRepository extends PagingAndSortingRepository<Post, Long> {
    Page<Post> findAllByDraft(Pageable pageable, Boolean isDraft);
    Page<Post> findAllByUserAndDraft(Pageable pageable, User user, Boolean isDraft);
}
