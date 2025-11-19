package com.joaquinogallar.personalblog.comment.repository;

import com.joaquinogallar.personalblog.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
