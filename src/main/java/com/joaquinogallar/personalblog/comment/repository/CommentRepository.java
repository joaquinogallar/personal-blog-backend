package com.joaquinogallar.personalblog.comment.repository;

import com.joaquinogallar.personalblog.comment.dto.CommentResponse;
import com.joaquinogallar.personalblog.comment.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<CommentResponse> findAllByPostId(Long postId,
                                          Pageable pageable);
}
